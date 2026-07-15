# Sitemap 线程池优化方案

> 文档背景：当前 `SitemapRequestConsumer` 使用固定 35 线程 + `SynchronousQueue` 处理 MQ 消息，
> 任务链为 HTTP下载（10~15s）→ XML解析 → 结果投递。MQ 存量千万级消息，服务器 8核16G。

---

## 一、现状分析

### 当前线程池配置

```java
// SitemapRequestConsumer.java:104
EXECUTOR = new ThreadPoolExecutor(
    threadNumValue, threadNumValue,   // core = max = 35（动态配置）
    0L, TimeUnit.MILLISECONDS,
    new SynchronousQueue<>()          // 无缓冲，线程满即阻塞调用方
);





        <<<<<<< HEAD
            <artifactId>kms-java-client</artifactId>
            <version>0.16.1</version>
        =======
            <artifactId>kms-tls-sdk</artifactId>
            <version>0.7.1</version>
        >>>>>>> c0d0d60872b808081fdba5c272753edd2c90c50b


        <dependency>
            <groupId>com.dianping.lion</groupId>
            <artifactId>lion-client</artifactId>
            <version>0.14.0</version>
        </dependency>
    </dependencies></project>




```HEAD
        <dependency>
            <groupId>com.dianping.lion</groupId>
            <artifactId>lion-client</artifactId>
            <version>0.14.0</version>
        </dependency>
    </dependencies></project>
=======
    </dependencies></project>

### 核心问题

| 问题 | 位置 | 说明 |
|------|------|------|
| 线程数严重偏少 | `sitemap_thread_num = 35` | IO密集型任务，CPU空转严重 |
| 消费者线程被阻塞 | `addRequestToThreadPool` L434 | `synchronized + sleep` 轮询，MQ消费暂停 |
| 连接池无总量限制 | `HttpClientGenerator.java:63` | 只设 `defaultMaxPerRoute=100`，无 `maxTotal` |

### 当前吞吐量

```
吞吐量 = 线程数 / 平均耗时 = 35 / 12.5s = 2.8 条/s = 168 条/min

千万条消息消费完毕预估时间 = 10,000,000 / 168 ≈ 41 天
```

---

## 二、方案一：压测提高线程池线程数

### 目标

找到**吞吐量不再随线程数增长**的临界点，确定合理的 `sitemap_thread_num` 配置值。

### 前置准备

**1. 补充 Cat 耗时监控埋点**

在 `SitemapService.java` 的 `invokeCrawl` 调用处补充 RT 统计：

```java
long start = System.currentTimeMillis();
CrawledResult result = invokeCrawl(request, url);
Cat.logMetricForDuration("sitemap.download.rt", System.currentTimeMillis() - start);
```

**2. 补充连接池总量上限**

在 `HttpClientGenerator.java:62` 处新增：

```java
this.connectionManager = new PoolingHttpClientConnectionManager(reg);
this.connectionManager.setDefaultMaxPerRoute(100);
this.connectionManager.setMaxTotal(1000); // 新增，随线程数同步调大
```

**3. 记录压测基线（35线程）**

压测前观察 30 分钟，记录：
- MQ offset 增长速率（条/min）
- `Cat: sitemap.threadpool.activeCount` 均值
- 机器 CPU 使用率
- 机器内存使用率
- TCP 连接数：`ss -s | grep estab`

---

### 压测执行计划

每档操作流程：
1. 动态配置中心修改 `sitemap_thread_num`
2. 等待 **5 分钟**让线程池稳定
3. 观察 **30 分钟**记录各项指标
4. 对照判断标准决定是否继续扩

| 阶段 | 线程数 | 预期吞吐量 | 栈内存占用 |
|------|--------|----------|---------|
| 基线 | 35     | 168 条/min | ~18MB  |
| 第1档 | 100   | 480 条/min | ~50MB  |
| 第2档 | 200   | 960 条/min | ~100MB |
| **第3档** | **300** | **1440 条/min** | **~150MB** |
| 第4档 | 400   | 1920 条/min | ~200MB |
| 第5档 | 500   | 2400 条/min | ~250MB |

---

### 每档观察指标与判断标准

| 指标 | 观察方式 | 可继续扩条件 | 停止扩容条件 |
|------|---------|------------|------------|
| MQ消费速率 | MQ控制台 offset 增长 | 每档线性增长 | 增长停滞不再涨 |
| activeCount | `Cat: sitemap.threadpool.activeCount` | 均值 > coreSize × 80% | 均值 < 50% |
| CPU使用率 | `top` / 监控平台 | < 60% | > 75% |
| 内存使用率 | `jstat -gcutil <pid>` | < 75% | > 85% |
| submit.locked | `Cat: sitemap.submit.locked` | 仍有告警 | 告警消失 |
| TCP连接数 | `ss -s \| grep estab` | 随线程数增长 | 不再增长 |
| GC频率 | `jstat -gcutil <pid> 5000` | FullGC < 1次/min | FullGC频繁 |
| resend告警 | `Cat: msg.resend.threadpool` | 仍有出现 | 消失 |

---

### 临界点判断逻辑

```
MQ消费速率不再随线程数增长
    ├─ CPU > 75%          → CPU 是瓶颈，当前档即上限
    ├─ TCP连接数不再增长   → 网络/连接池瓶颈，先调大 setMaxTotal 再继续
    ├─ GC 频繁            → 内存瓶颈，调大 -Xmx 或降线程数
    └─ activeCount 下降   → 线程已够用，当前档位即合理值
```

### 预期结论

| 资源 | 预估临界点 | 说明 |
|------|---------|------|
| CPU | ~2000线程 | IO等待占比>99%，CPU基本空转 |
| 内存 | ~800线程 | 500线程栈仅~250MB，远低于16G |
| 连接池 | 取决于 maxTotal 配置 | 需同步调整 |
| **网络/目标服务器** | **实测决定** | **最可能的真实瓶颈** |

**建议直接落地值：300**，消费时间从 41 天压缩到约 4.8 天。

---

## 三、方案二：阻塞线程池改为异步线程池

### 改造动机

当前 `addRequestToThreadPool` 存在：

```java
// SitemapRequestConsumer.java:430
synchronized (EXECUTOR) {
    while (EXECUTOR.getCorePoolSize() <= EXECUTOR.getActiveCount()) {
        sleep(SLEEP_TIME);  // ← MQ消费者线程被阻塞在这里
    }
    EXECUTOR.execute(...);
}
```

**MQ 消费者线程被 sleep 阻塞 → 消费暂停 → 消息堆积**，即使线程池有空余，消费者也可能在等锁。

---

### 改造架构

```
改造前：
MQ消费者线程
    → synchronized sleep 等待线程池
    → EXECUTOR 单线程池（HTTP下载 + 解析 + 投递全在同一线程）

改造后：
MQ消费者线程
    → 直接提交（不阻塞）
    → HTTP_EXECUTOR（大线程数，专门做IO阻塞等待）
           ↓ HTTP 完成
    → CompletableFuture 回调
           ↓
    → PARSE_EXECUTOR（小线程数，CPU计算：解析+投递）
```

---

### 新增两个线程池

```java
// 线程池1：HTTP下载，大线程数，专门承受IO阻塞
// 动态配置项：sitemap_http_thread_num，建议初始值 300
private static ThreadPoolExecutor HTTP_EXECUTOR;

// 线程池2：解析+投递，CPU密集，线程数小
// 固定：CPU核数 × 2 = 16
private static ThreadPoolExecutor PARSE_EXECUTOR;
```

**初始化代码：**

```java
// HTTP线程池：LinkedBlockingQueue 允许排队，替代 sleep 轮询
HTTP_EXECUTOR = new ThreadPoolExecutor(
    httpThreadNum, httpThreadNum,
    0L, TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<>(500),        // 队列满才触发拒绝，不再sleep等待
    new ThreadFactory() { /* 命名 */ },
    new ThreadPoolExecutor.AbortPolicy()   // 满了抛异常 → 走 resendMsg
);

// 解析线程池
PARSE_EXECUTOR = new ThreadPoolExecutor(
    16, 16,
    60L, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(1000),
    new ThreadFactory() { /* 命名 */ }
);
```

---

### addRequestToThreadPool 改造

```java
public void addRequestToThreadPool(SitemapRequest request) {
    try {
        HTTP_EXECUTOR.execute(() -> {
            // HTTP 下载（阻塞在此，但不影响 MQ 消费者线程）
            CrawledResult result = sitemapService.invokeCrawlOnly(request);

            // 下载完成，异步提交解析+投递
            CompletableFuture
                .supplyAsync(() -> sitemapService.parseOnly(request, result), PARSE_EXECUTOR)
                .thenAcceptAsync(parsed -> sitemapService.sendOnly(request, parsed), PARSE_EXECUTOR)
                .exceptionally(e -> {
                    log.error("async parse/send error url:{}", request.getUrl(), e);
                    Cat.logEvent("sitemap.consumer.exception", "async_error");
                    return null;
                });
        });
    } catch (RejectedExecutionException e) {
        // 队列满时直接重投，不再循环等待
        log.error("HTTP_EXECUTOR queue full, resend url:{}", request.getUrl());
        Cat.logEvent("sitemap.msg.consume.statistics", "msg.resend.threadpool");
        resendMsg(request);
    }
}
```

---

### SitemapService 方法拆分

原 `download()` 拆为三段独立方法：

```java
// 1. 只做 HTTP 下载，返回原始结果
public CrawledResult invokeCrawlOnly(SitemapRequest request);

// 2. 只做解析（robots解析 / sitemap XML解析），返回解析产物
public ParsedResult parseOnly(SitemapRequest request, CrawledResult result);

// 3. 只做结果投递（写DB + 发MQ）
public void sendOnly(SitemapRequest request, ParsedResult parsed);
```

---

### 动态配置项变更

| 配置项 | 说明 | 建议初始值 |
|--------|------|---------|
| `sitemap_thread_num` | 废弃（或保留兼容旧模式） | - |
| `sitemap_http_thread_num` | HTTP下载线程数 | 300 |
| `sitemap_use_async_pool` | 特性开关，false=走旧逻辑 | false（灰度时开） |

---

### 改造效果对比

| 项目 | 改造前 | 改造后 |
|------|--------|--------|
| MQ消费者线程 | 被 sleep 阻塞，消费暂停 | 不阻塞，持续消费 |
| 线程池等待方式 | `synchronized + sleep` 轮询 | `LinkedBlockingQueue` 自然排队 |
| 队列满处理 | 36s 超时后 resendMsg | 立即 RejectedExecution → resendMsg |
| HTTP与解析耦合 | 同一线程串行执行 | 两个线程池解耦，互不影响 |
| 线程数规划 | 1个池承担所有工作 | HTTP池大（IO）+ 解析池小（CPU） |

---

## 四、测试方案

### 方案一（提高线程数）测试

直接利用生产 MQ 消息压测，观察 Cat 指标，无需额外测试环境。

**验收标准：**
- `sitemap.submit.locked` 告警消失
- `msg.resend.threadpool` 告警消失
- MQ 消费速率线性提升
- CPU < 70%，内存 < 80%

---

### 方案二（异步改造）测试

#### 第一步：单元测试（改造前）

```
测试用例1：HTTP 正常返回 → 验证解析和投递被正确调用
测试用例2：HTTP 超时异常 → 验证 exceptionally 捕获，不影响其他任务
测试用例3：HTTP_EXECUTOR 队列满 → 验证 resendMsg 被触发一次
测试用例4：parseOnly 抛异常 → 验证 exceptionally 捕获，sendOnly 不执行
测试用例5：并发 100 个请求 → 验证无死锁、无数据丢失
```

#### 第二步：灰度验证（单机）

1. 新增配置 `sitemap_use_async_pool = false`，部署上线（此时走旧逻辑）
2. 动态改为 `true`，切换到异步逻辑
3. 对比改造前后 30 分钟内：

| 验收指标 | 标准 |
|---------|------|
| MQ 消费速率 | ≥ 改造前 |
| 成功率（sitemap.msg.consume.statistics） | ≥ 改造前 |
| msg.resend 比例 | ≤ 改造前 |
| GC 停顿时间 | 无明显恶化 |
| HTTP_EXECUTOR queueSize | 稳定不持续增长 |
| 异常日志 async_error | 无大量出现 |

#### 第三步：全量推送

灰度 24 小时无问题，全量推送所有实例。

#### 回滚方案

动态配置 `sitemap_use_async_pool = false` 即可回滚，**无需重新部署**。

---

## 五、推荐执行顺序

```
第1步（立即）：setMaxTotal(1000) 防止连接池成为瓶颈
                                ↓
第2步（本周）：压测方案一，sitemap_thread_num 梯度调节至 300
                                ↓
第3步（观察1周）：确认临界点，稳定运行
                                ↓
第4步（下个迭代）：实施方案二异步改造，彻底解决消费者线程阻塞问题
```

> **核心收益**：仅执行第2步，MQ 消费速度即可从 168条/min 提升至 1440条/min，
> 千万级消息消费时间从 41天 压缩至约 5天。