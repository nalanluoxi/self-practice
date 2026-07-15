# Screenshot 消费者背压改造执行计划

## 一、背景与问题描述

### 1.1 现象

`ScreenshotRequestConsumer`（截图抓取 MQ 消费者）线上出现周期性消费停止问题：

- 服务运行一段时间后，Mafka 停止推送消息，消费积压持续增长
- 重启服务后恢复，但过段时间再次停止
- 无代码报错，服务本身未崩溃

### 1.2 根本原因

当前 `addRequestToThreadPool` 方法中使用 `synchronized(EXECUTOR) + while(Thread.sleep(1000))` 等待线程池出现空闲槽位：

```java
// 问题代码（ScreenshotRequestConsumer.java）
public void addRequestToThreadPool(ScreenshotRequest request) {
    synchronized (EXECUTOR) {
        while (EXECUTOR.getCorePoolSize() <= EXECUTOR.getActiveCount()) {
            Thread.sleep(SLEEP_TIME);   // Mafka consumer-thread 在此阻塞！
            Cat.logEvent("screenshot.threadpool.download", "threadpool.isfull");
        }
        EXECUTOR.execute(() -> { forwardToCluster(request); });
    }
}
```

**阻塞链路：**

```
Mafka consumer-thread
  → receive() → preFilterProcess() → addRequestToThreadPool()
    → synchronized(EXECUTOR) 获取锁
      → while(sleep) 持续阻塞（线程池满时持续数分钟）
        → Mafka 心跳超时（默认30秒）
          → Broker 判定 consumer 无响应
            → 触发 rebalance → 停止向该实例推送消息
```

**关键区别：**

| 线程类型 | 是否可以阻塞 | 原因 |
|---------|------------|------|
| Mafka consumer-thread（调用 receive() 的线程） | **不能** | 阻塞会触发心跳超时 → rebalance → 停止消费 |
| EXECUTOR 线程（screenshot-crawler-pool-N-thread） | **可以** | 不是 Mafka 管理的线程，不影响心跳 |

---

## 二、现状架构分析

### 2.1 完整调用链

```
Mafka → ScreenshotRequestConsumer.receive(msg)
          → preFilterProcess(msg)
            → 参数校验 / 黑名单过滤（快速，不阻塞）
            → addRequestToThreadPool(request)
              → synchronized(EXECUTOR) + while sleep  ← 问题根源
              → EXECUTOR.execute(() -> forwardToCluster(request))
                  ├── 灰度路径（isGray && random < sentGray）：
                  │     → 从 screenshotClusterIp 随机选一个 IP
                  │     → HTTP POST 到 http://{ip}:8080/screenshot/submitScreenShotAndSend
                  │     → 同步等待 HTTP 响应（EXECUTOR 线程阻塞，可接受）
                  │     → 解析响应：success / reject_limit / other_error
                  │     → 失败时 resendMsg() 重投 MQ
                  └── 本地路径（非灰度）：
                        → screenshotService.submit(request, true)
                        → 本地执行截图（EXECUTOR 线程阻塞，可接受）
```

### 2.2 重要约束

Consumer 机器 和 Screenshot 执行机器是**不同 IP 的机器**，无法直接注入 `ScreenshotPlaywrightService` 的线程池对象来观测。因此不能用"观测下游线程池容量"的方式实现背压，只能依赖 HTTP 响应中的 `threadpool_full` 标识 + 重投 MQ 实现软性背压。

### 2.3 EXECUTOR 存在的意义

控制消费者侧的并发转发数，**不能删除**，只需改变"线程池满时的处理策略"：从"死等"改为"立即重投"。

---

## 三、改造方案

### 3.1 核心思路

**原则：Mafka consumer-thread 绝不阻塞。**

| 场景 | 改造前 | 改造后 |
|------|--------|--------|
| EXECUTOR 有空闲 | 提交任务，立即返回 | 提交任务，立即返回（行为不变） |
| EXECUTOR 已满 | while(sleep) 死等，阻塞 Consumer 线程 | 直接 resendMsg() 重投 MQ，立即返回 |
| RejectedExecutionException | 抛出（极端情况） | catch 后 resendMsg()，不再向上传播 |

### 3.2 改造点一：`addRequestToThreadPool` 方法

**改前：**

```java
public void addRequestToThreadPool(ScreenshotRequest request) {
    synchronized (EXECUTOR) {
        while (EXECUTOR.getCorePoolSize() <= EXECUTOR.getActiveCount()) {
            try {
                Thread.sleep(SLEEP_TIME);
                Cat.logEvent("screenshot.threadpool.download", "threadpool.isfull");
            } catch (InterruptedException e) {
                log.error("screenshot addRequestToThreadPool sleep error", e);
            }
        }
        try {
            EXECUTOR.execute(() -> {
                try {
                    Cat.logEvent("screenshot.threadpool.download", "threadpool.succEnter");
                    forwardToCluster(request);
                } catch (Throwable e) {
                    log.error("ScreenshotRequestConsumer thread error, url={}", request.getUrl(), e);
                    Cat.logEvent("screenshot.consumer.exception", "thread_error");
                }
            });
        } catch (Throwable e) {
            log.error("screenshot 加入线程池异常 url={}", request.getUrl(), e);
            Cat.logEvent("screenshot.threadpool.download", "executor.failed");
            throw e;
        }
    }
}
```

**改后：**

```java
public void addRequestToThreadPool(ScreenshotRequest request) {
    // 快速检查线程池是否已满，满则直接重投，Consumer线程不阻塞
    if (EXECUTOR.getActiveCount() >= EXECUTOR.getCorePoolSize()) {
        Cat.logEvent("screenshot.threadpool.download", "threadpool.isfull");
        resendMsg(request);
        return;
    }
    try {
        EXECUTOR.execute(() -> {
            Cat.logEvent("screenshot.threadpool.download", "threadpool.succEnter");
            try {
                forwardToCluster(request);
            } catch (Throwable e) {
                log.error("ScreenshotRequestConsumer thread error, url={}", request.getUrl(), e);
                Cat.logEvent("screenshot.consumer.exception", "thread_error");
            }
        });
    } catch (RejectedExecutionException e) {
        // 极端竞争：getActiveCount 检查通过后，刚好被其他线程占满
        log.warn("screenshot 线程池提交拒绝（竞争兜底），重投MQ url={}", request.getUrl());
        Cat.logEvent("screenshot.threadpool.download", "threadpool.rejected");
        resendMsg(request);
    }
}
```

**改动说明：**
1. 移除 `synchronized(EXECUTOR)` 锁，彻底解除 Consumer 线程阻塞
2. 移除 `while(sleep)` 死等逻辑
3. 用 `getActiveCount >= getCorePoolSize` 快速检查替代阻塞等待
4. 检查发现已满直接调 `resendMsg`，立即返回
5. 增加 `RejectedExecutionException` catch 作为竞争兜底（因为 `getActiveCount` 非原子性）

### 3.3 改造点二：`forwardToCluster` 中 `threadpool_full` 单独识别

当 HTTP 转发到下载机时，下载机线程池满会返回含 `threadpool_full` 的响应体。当前代码将此归入 `other_error`，需单独识别以便精细监控。

**改前：**

```java
} else {
    // threadpool_full、other_error、response 为 null 均归入此分支
    log.warn("screenshot forwardToCluster failed, response={}, url={}", response, request.getUrl());
    Cat.logEvent("screenshot.forwardToCluster.result", "other_error");
    resendMsg(request);
}
```

**改后：**

```java
} else if (response != null && response.contains("threadpool_full")) {
    log.warn("screenshot forwardToCluster: remote threadpool_full, url={}", request.getUrl());
    Cat.logEvent("screenshot.forwardToCluster.result", "threadpool_full");
    resendMsg(request);
} else {
    log.warn("screenshot forwardToCluster failed, response={}, url={}", response, request.getUrl());
    Cat.logEvent("screenshot.forwardToCluster.result", "other_error");
    resendMsg(request);
}
```

### 3.4 清理无用常量

以下字段在改造后不再被使用，一并删除：

| 字段 | 可删原因 |
|------|---------|
| `MAX_WAIT_TIME`（`@MdpConfig("screenshot-threadpool-max-wait-time")`） | while-sleep 逻辑删除后不再使用 |
| `SLEEP_TIME`（`1000L`） | while-sleep 逻辑删除后不再使用 |

`MAX_RETRY_COUNT` **保留**，`resendMsg()` 方法中仍在使用（防止无限重投）。

---

## 四、改造前后对比

### 4.1 行为对比

| 维度 | 改造前 | 改造后 |
|------|--------|--------|
| Consumer 线程（线程池满时） | while(sleep) 死等，阻塞数分钟 | 立即 resendMsg，立即返回 |
| Mafka 心跳 | 线程池满时超时断联，触发 rebalance | 始终正常，不阻塞 |
| 消息背压机制 | 依赖 Consumer 阻塞（错误方式） | 依赖 resendMsg 重投 + retryTimes 限制（正确方式） |
| EXECUTOR 线程行为 | 不变（可阻塞等待 HTTP 响应） | 不变（仍可阻塞等待 HTTP 响应） |
| 极端并发保护 | `synchronized` 锁序列化 | `RejectedExecutionException` catch 兜底 |

### 4.2 消息流向对比

**改造前（线程池满时）：**
```
MQ → Consumer线程 → while sleep（阻塞30秒+）→ Mafka rebalance → 停止消费
```

**改造后（线程池满时）：**
```
MQ → Consumer线程 → getActiveCount >= corePoolSize → resendMsg → MQ队列末尾
              ↑________________________________（延迟后重新消费）___|
```

---

## 五、涉及文件

| 文件 | 改动内容 |
|------|---------|
| `ScreenshotRequestConsumer.java` | 主要改动：`addRequestToThreadPool`、`forwardToCluster`、删除无用常量 |

**不需要改动的文件：**
- `ScreenshotService.java`：在 EXECUTOR 线程内调用，允许阻塞
- `ScreenshotPlaywrightService.java`：截图执行层，不受影响
- `ScreenshotPublishService.java`：`sendRequest` 重投方法，直接复用

---

## 六、风险点与缓解措施

### 6.1 重投风暴（主要风险）

**场景：** 下游持续过载，EXECUTOR 长期满载，消息快速在 Consumer 和 MQ 之间循环。

**缓解措施：**
- `retryTimes >= MAX_RETRY_COUNT(2)` 时丢弃，防止单条消息无限循环
- Mafka 本身有消费速率控制，不会无限速消费
- 可通过 Lion 降低 `screenshot_thread_num` 主动限流
- 监控 `screenshot.forwardToCluster.result:threadpool_full` 打点，可感知过载程度

### 6.2 getActiveCount 非原子性（次要风险）

**场景：** 检查通过但提交前刚好被占满，抛 `RejectedExecutionException`。

**缓解措施：** 已在 catch 中调 `resendMsg`，行为与"检查时已满"一致，不丢消息。

### 6.3 消息顺序变化（可接受）

重投后消息排到队列末尾，URL 处理顺序改变。截图抓取场景本身不依赖处理顺序，可接受。

---

## 七、测试验证要点（哈吉霞参考）

1. **线程池满时不阻塞：** mock `EXECUTOR.getActiveCount() == corePoolSize`，验证 Consumer 线程立即返回且调用 `resendMsg`
2. **线程池有空闲时提交成功：** mock `getActiveCount() < corePoolSize`，验证任务成功提交，`resendMsg` 不被调用
3. **RejectedExecutionException 兜底：** mock `EXECUTOR.execute()` 抛出该异常，验证 `resendMsg` 被调用
4. **retryTimes 超限时丢弃：** 构造 `retryTimes = MAX_RETRY_COUNT` 的请求，验证 `resendMsg` 不再重投
5. **CAT 打点验证：** `threadpool.isfull`、`threadpool.rejected`、`threadpool.succEnter` 在对应场景下正确打点
6. **forwardToCluster threadpool_full 识别：** 构造含 `threadpool_full` 的 HTTP 响应，验证打 `threadpool_full` 而非 `other_error`

---

## 八、上线建议

1. 改动仅涉及 `ScreenshotRequestConsumer.java` 一个文件，改动量小，风险可控
2. 建议非高峰时段上线，上线后重点观察：
   - Mafka 消费是否持续正常（不再出现 rebalance 导致的停止消费）
   - CAT `screenshot.threadpool.download:threadpool.isfull` 打点频次（应降低或消失）
   - `screenshot.forwardToCluster.resend:resend.success` 打点（出现说明重投机制正常生效）
3. 若出现大量 `retryTimesExceed` 告警，说明下游持续过载，需排查 `screenshot_thread_num` 配置是否合理