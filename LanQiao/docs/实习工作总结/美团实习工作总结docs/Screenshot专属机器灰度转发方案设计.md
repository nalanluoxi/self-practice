# Screenshot 专属机器灰度转发方案设计

> 文档作者：哈吉夏
> 创建时间：2026-05-26
> 目标：让 Screenshot 抓取流量只打到专属机器，不影响大环 Crawler / Sitemap 等其他服务的负载均衡

---

## 一、现状分析

### 1.1 当前架构

```
Mafka Topic（screenshot 消息）
        │
        ▼
ScreenshotRequestConsumer（所有机器都在消费）
        │
        ├─ screenshotClusterIp 非空 + isGray + random < sentGray
        │       └─→ HTTP 转发到 screenshot_cluster_ip 中随机一台机器的 /screenshot/submitScreenShotAndSend
        │
        └─ 否则
                └─→ screenshotService.submit(request, true)  ← 本地执行（CPU/内存密集）
```

### 1.2 现存问题

| 问题 | 描述 |
|------|------|
| 所有机器都消费 screenshot topic | 每台机器都在跑 Playwright，抢占大环 Crawler 的线程和资源 |
| 灰度转发逻辑不完整 | `isGray` + `sentGray` 双开关控制，非灰度机器仍然本地执行 |
| 负载无法隔离 | Screenshot 是 CPU/内存/网络密集型，与普通 HTTP 抓取混跑影响互相 |
| 无法做到按机器角色路由 | 没有"我是 screenshot 专属机器"的概念 |

---

## 二、目标

1. **角色分离**：将机器分为两类：`screenshot 专属机器` 和 `大环通用机器`
2. **流量隔离**：screenshot 抓取只在专属机器上执行，通用机器不执行任何 screenshot 本地抓取
3. **灰度上线**：支持按比例灰度扩大 screenshot 专属机器的流量承接比例
4. **不停服**：所有变更通过 Lion 配置动态生效，无需重启

---

## 三、方案设计

### 3.1 核心思路

采用 **"角色标识 + 消费者行为分叉"** 模式：

- 每台机器通过 Lion 配置读取自己的角色（`screenshot_only_mode`）
- **screenshot 专属机器**：`ScreenshotRequestConsumer` 正常消费并本地执行；`CrawlerRequestConsumer` / `SitemapRequestConsumer` 等其他消费者**停止消费或直接丢弃**
- **通用机器**：`ScreenshotRequestConsumer` 消费后**强制转发**到专属机器 IP，不再本地执行

### 3.2 新增 Lion 配置项

| 配置 Key | 类型 | 说明 | 示例值 |
|---------|------|------|--------|
| `screenshot_only_mode` | `Boolean` | 当前机器是否为 screenshot 专属机器（**每台机器独立配置，通过 instance 级别配置**） | `true` / `false` |
| `screenshot_cluster_ip` | `ArrayList<String>` | screenshot 专属机器 IP 列表（已存在，继续复用） | `["10.x.x.1","10.x.x.2"]` |
| `screenshot_Sent_isGray` | `Boolean` | 是否开启灰度转发总开关（已存在） | `true` |
| `screenshot_Sent_Gray` | `Double` | 灰度转发比例（已存在，**通用机器侧设为 1.0 表示全量转发**） | `1.0` |
| `screenshot_forward_fallback` | `Boolean` | 转发失败时是否允许本地降级执行（默认 false，专属机器上线稳定后可关闭） | `false` |

### 3.3 机器行为矩阵

| 机器角色 | ScreenshotRequestConsumer 行为 | CrawlerRequestConsumer 行为 |
|---------|-------------------------------|----------------------------|
| **screenshot 专属机器**（`screenshot_only_mode=true`） | 正常消费 + **本地执行**（不转发） | 消费后**直接返回 CONSUME_SUCCESS 不执行**（通过开关丢弃） |
| **通用机器**（`screenshot_only_mode=false`） | 消费后**100% 转发**到专属机器（`sentGray=1.0`） | 正常执行 crawler 逻辑 |

### 3.4 详细流程图

#### 通用机器（大环机器）的 ScreenshotRequestConsumer 流程

```
收到 screenshot MQ 消息
        │
        ▼
  preFilterProcess（基础过滤：黑名单、URL校验）
        │
        ▼
  addRequestToThreadPool
        │
        ▼
  forwardToCluster
        │
  screenshot_only_mode=false
  + screenshotClusterIp 非空
  + sentGray=1.0（全量转发）
        │
        ▼
  HTTP POST → screenshot 专属机器 /screenshot/submitScreenShotAndSend
        │
        ├─ 成功(code=1) → 结束
        ├─ threadpool_full → resendMsg 重投 MQ
        ├─ screenshot_qps_limited → resendMsg 重投 MQ
        └─ 失败（fallback=false）→ 仅记录日志，丢弃
```

#### 专属机器的 ScreenshotRequestConsumer 流程

```
收到 screenshot MQ 消息（来自 MQ 或 HTTP 转发）
        │
        ▼
  preFilterProcess
        │
        ▼
  addRequestToThreadPool
        │
        ▼
  forwardToCluster
        │
  screenshot_only_mode=true → 直接 screenshotService.submit(request, true)
  （不再经过 isGray/sentGray 判断，强制本地执行）
```

#### 专属机器的 CrawlerRequestConsumer 行为

```
收到大环 crawler MQ 消息
        │
        ▼
  handleSpiderMsg
        │
  screenshot_only_mode=true → 直接 return CONSUME_SUCCESS（丢弃，不执行抓取）
```

---

## 四、代码改动点

### 4.1 `ScreenshotRequestConsumer.java` 改动

**改动位置**：`forwardToCluster` 方法

```java
// 新增 Lion 配置
@MdpConfig(key = "screenshot_only_mode:false")
private Boolean screenshotOnlyMode;

@MdpConfig(key = "screenshot_forward_fallback:false")
private Boolean forwardFallback;

private void forwardToCluster(ScreenshotRequest request) {
    // screenshot 专属机器：直接本地执行，不走转发逻辑
    if (Boolean.TRUE.equals(screenshotOnlyMode)) {
        Cat.logEvent("screenshot.forwardToCluster", "local.dedicated");
        screenshotService.submit(request, true);
        return;
    }

    // 通用机器：走原有灰度转发逻辑（sentGray 设为 1.0 即全量转发）
    List<String> ips = screenshotClusterIp;
    boolean isGray = screenShotSentisGray;
    Double sentGray = screenShotSentGray;
    if (ips != null && !ips.isEmpty() && isGray && Math.random() < sentGray) {
        // ... 原有 HTTP 转发逻辑（不变）
        // 失败降级：只有 forwardFallback=true 时才允许本地执行
        // 否则仅记录日志
    } else {
        // 通用机器且未配置转发（兜底）
        if (Boolean.TRUE.equals(forwardFallback)) {
            screenshotService.submit(request, true);
        } else {
            Cat.logEvent("screenshot.forwardToCluster", "skip_no_cluster");
            log.warn("screenshot no cluster configured, skip url={}", request.getUrl());
        }
    }
}
```

### 4.2 `CrawlerRequestConsumer.java` 改动

**改动位置**：`handleSpiderMsg` 方法头部

```java
// 新增 Lion 配置
@MdpConfig(key = "screenshot_only_mode:false")
private Boolean screenshotOnlyMode;

public ConsumeStatus handleSpiderMsg(String msg, boolean checkJs) {
    // screenshot 专属机器不处理 crawler 任务
    if (Boolean.TRUE.equals(screenshotOnlyMode)) {
        Cat.logEvent("crawler.skip.screenshot_only_mode", "skip");
        return ConsumeStatus.CONSUME_SUCCESS;
    }
    // ... 原有逻辑不变
}
```

### 4.3 其他消费者（Sitemap 等）

如果专属机器上也部署了 `SitemapRequestConsumer`，同样在消费方法头部加同样的 `screenshot_only_mode` 判断。

---

## 五、上线步骤（灰度方案）

### 第一步：准备专属机器（不动任何代码，先配置）

1. 选取 N 台机器作为 screenshot 专属机器（建议先选 1 台灰度）
2. 在 Lion 上为这些机器配置 instance 级别：
   - `screenshot_only_mode = true`
   - `screenshot_thread_num` = 按机器规格调整（Playwright 线程数）
3. 在 Lion 上为**通用机器**配置：
   - `screenshot_Sent_isGray = true`
   - `screenshot_Sent_Gray = 0.1`（先 10% 转发到专属机器，观察效果）
   - `screenshot_cluster_ip = ["专属机器IP"]`

### 第二步：验证专属机器

- 观察专属机器的 CAT 指标：`screenshot.forwardToCluster = local.dedicated`
- 观察专属机器不再有 `crawler.*` 指标上报
- 验证 screenshot 结果正确写入 S3

### 第三步：逐步扩大灰度

```
sentGray: 0.1 → 0.3 → 0.5 → 0.8 → 1.0
```

每次放大后观察：
- 专属机器线程池使用率（`screenshot.threadpool.activeCount`）
- 通用机器 screenshot 本地执行量下降趋势
- 转发成功率（`screenshot.forwardToCluster.result = success`）

### 第四步：全量切换

- `screenshot_Sent_Gray = 1.0`（通用机器全量转发）
- `screenshot_forward_fallback = false`（关闭降级，通用机器不再本地执行）

---

## 六、监控指标

| CAT 指标 | 含义 | 告警阈值 |
|---------|------|---------|
| `screenshot.forwardToCluster = local.dedicated` | 专属机器本地执行量 | 正常有值 |
| `screenshot.forwardToCluster = local` | 通用机器本地执行量（应趋近0） | 全量后应为0 |
| `screenshot.forwardToCluster.result = threadpool_full` | 专属机器线程池满 | > 5% 需扩容 |
| `screenshot.forwardToCluster.result = success` | 转发成功率 | < 95% 告警 |
| `crawler.skip.screenshot_only_mode` | 专属机器丢弃 crawler 任务量 | 正常有值 |

---

## 七、回滚方案

任何时候出现问题，只需 Lion 配置回滚：

1. 将所有机器的 `screenshot_only_mode` 设为 `false`
2. 将 `screenshot_Sent_Gray` 设为 `0`（关闭转发）

所有机器恢复到原来的"本地执行"模式，无需重启。

---

## 八、风险与应对

| 风险 | 应对 |
|------|------|
| 专属机器故障，所有 screenshot 消息积压 | `screenshot_cluster_ip` 配置多台机器；`forwardFallback=true` 允许降级到通用机器 |
| 转发 HTTP 调用增加延迟 | screenshot 任务本身是秒级任务，毫秒级 HTTP 延迟可忽略 |
| 专属机器 Playwright 资源耗尽 | `screenshot_thread_num` 动态调整；`screenshot_limit` Rhino 限流兜底 |
| 通用机器 screenshotClusterIp 配置为空 | 当 `screenshot_only_mode=false` 且 IP 列表为空时，日志告警不执行，避免浪费资源 |