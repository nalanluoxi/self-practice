# Screenshot 并发控制改造执行计划

---

## 一、背景与问题描述

**当前架构存在的问题：**

集群下载机上的 `ScreenshotController` 接收到消费者机器的 HTTP 转发请求后，直接在 Tomcat 线程中同步调用 `screenshotService.submit()` → `screenshotPlaywrightService.crawlScreenshot()`，没有任何并发控制机制。

**后果：**
- Chrome 进程并发数完全不受控制，磁盘被打满
- Tomcat 线程池的线程全被 screenshot 任务长时间占用
- 当前仅靠 `OneLimiter`（`screenshot_limit`）做粗粒度限流，但 OneLimiter 是吞吐限速而非并发控制，无法有效约束瞬时并发

**消费者机器端（`ScreenshotRequestConsumer`）已有外层线程池控制**：`EXECUTOR`（`screenshot_thread_num`，默认5线程），负责将任务提交到 `forwardToCluster()` 发 HTTP。但下载机端完全没有对应的并发控制。

---

## 二、目标架构

改造后双层线程池架构，对齐 `PlaywrightService` 的 `SPIDER_TASK_POOL` 模式：

```
【消费者机器】
  MQ → ScreenshotRequestConsumer.receive()
    → preFilterProcess() → addRequestToThreadPool()
    → EXECUTOR（外层，screenshot_thread_num，默认5）
    → forwardToCluster() → HTTP POST（带响应解析）
         ↓ 成功 → CAT打点 success
         ↓ 失败 → resendMsg() 重投 MQ

【下载机】
  HTTP POST → ScreenshotController.submitScreenShotAndSend()
    → screenshotService.submit()
    → screenshotService.download()
    → screenshotPlaywrightService.submitTask()（新增）
    → DOWNLOAD_TASK_POOL（内层，screenshot_download_thread_num，建议默认10）
    → CompletableFuture.supplyAsync → crawlScreenshot()（真正执行）

【并发控制原理】
  DOWNLOAD_TASK_POOL 满时：
    → RejectedExecutionException → Controller 返回 "threadpool_full"
    → HTTP 响应返回 → 消费者感知到失败
    → resendMsg() 重投 MQ，消息不丢

  DOWNLOAD_TASK_POOL 有空闲时（正常情况）：
    → HTTP 阻塞等待结果（60~120秒）
    → 消费者线程被占住 → 停止拉新消息
    → 天然背压，匹配下游处理速度
```

---

## 三、各文件改造点（精确到方法级别）

### 3.1 ScreenshotPlaywrightService.java

**改造点1：新增静态线程池字段**

在类字段区（`@MdpConfig` 区域附近）新增：

```java
@MdpConfig("screenshot_download_thread_num")
private Integer downloadThreadNum;

public static ThreadPoolExecutor DOWNLOAD_TASK_POOL;
private static final String DOWNLOAD_POOL_NAME_PREFIX = "screenshot-download-pool-";
```

**改造点2：`setup()` 方法中初始化 `DOWNLOAD_TASK_POOL`**

在现有 `@PostConstruct setup()` 末尾追加（仿照 `ScreenshotRequestConsumer.setup()` 的线程池初始化模式）：

```java
int downloadThreadNumValue = (downloadThreadNum != null && downloadThreadNum > 0) ? downloadThreadNum : 10;
AtomicInteger downloadCounter = new AtomicInteger(1);
DOWNLOAD_TASK_POOL = new ThreadPoolExecutor(
    downloadThreadNumValue, downloadThreadNumValue,
    0L, TimeUnit.MILLISECONDS,
    new SynchronousQueue<>(),
    r -> new Thread(r, DOWNLOAD_POOL_NAME_PREFIX + downloadCounter.getAndIncrement()),
    new ThreadPoolExecutor.AbortPolicy()
);
log.info("screenshot内层下载线程池初始化完成 线程数: {}", downloadThreadNumValue);
```

同时在 ShutdownHook 中追加 `DOWNLOAD_TASK_POOL.shutdown()`。

**改造点3：新增 `updateDownloadThreadNum()` 配置监听方法**

```java
@MdpConfigListener(value = "screenshot_download_thread_num")
public void updateDownloadThreadNum(ConfigEvent configEvent) {
    try {
        int newSize = Integer.parseInt(configEvent.getNewValue());
        if (newSize > DOWNLOAD_TASK_POOL.getMaximumPoolSize()) {
            DOWNLOAD_TASK_POOL.setMaximumPoolSize(newSize);
            DOWNLOAD_TASK_POOL.setCorePoolSize(newSize);
        } else {
            DOWNLOAD_TASK_POOL.setCorePoolSize(newSize);
            DOWNLOAD_TASK_POOL.setMaximumPoolSize(newSize);
        }
        log.info("screenshot_download_thread_num 被修改 新值为:{} -> {}", configEvent.getOldValue(), configEvent.getNewValue());
    } catch (Exception e) {
        log.error("screenshot_download_thread_num 修改失败", e);
    }
}
```

**改造点4：新增 `submitTask()` 方法**

仿照 `PlaywrightService.submitTask()` 模式，新增：

```java
public CompletableFuture<ScreenshotCrawlResult> submitTask(
        ScreenshotRequest request, Proxy proxy, String proxyType, int timeout, String s3DirKey) {
    // 线程池满时抛出 RejectedExecutionException，由调用方处理
    return CompletableFuture.supplyAsync(
        () -> crawlScreenshot(request, proxy, proxyType, timeout, s3DirKey),
        DOWNLOAD_TASK_POOL
    );
}
```

---

### 3.2 ScreenshotService.java

**改造点1：`download()` 方法内部调用链改造**

现有代码（`crawlScreenshot` 直接调用）：
```java
result = screenshotPlaywrightService.crawlScreenshot(request, proxy, proxyType, DEFAULT_TIMEOUT_MS, s3DirKey);
```

改造后（提交到内层线程池，阻塞等待结果）：
```java
CompletableFuture<ScreenshotCrawlResult> future =
    screenshotPlaywrightService.submitTask(request, proxy, proxyType, DEFAULT_TIMEOUT_MS, s3DirKey);
result = future.get(); // 阻塞等待，保持同步语义不变
```

**改造点2：`download()` 捕获 `RejectedExecutionException` 并向上抛出**

在 `download()` 方法的 catch 块中新增（在 `catch (Throwable e)` 之前）：

```java
} catch (RejectedExecutionException e) {
    log.warn("screenshot download task rejected, threadpool full, url={}", request.getUrl());
    Cat.logEvent("screenshot.download.rejected", request.getDomain());
    downloadTx.setStatus("threadpool_full");
    throw e; // 向上抛，让 Controller 返回 threadpool_full
```

---

### 3.3 ScreenshotController.java

**改造点1：`submitScreenShotAndSend` 移除 OneLimiter 检查**

删除以下代码（第46-49行）：
```java
String key = "screenshot_limit";
LimitResult run = oneLimiter.run(key);
if (run == null || run.isReject()) {
    return ResponseBean.error("the current limit has been reached");
}
```

**改造点2：`submitScreenShotAndSend` 新增 `RejectedExecutionException` 捕获**

在现有 `catch (Throwable e)` 块之前新增：
```java
} catch (RejectedExecutionException e) {
    log.warn("ScreenshotController: threadpool full, url={}", request.getUrl());
    Cat.logEvent("screenshot.controller.rejected", request.getDomain());
    return ResponseBean.error("threadpool_full");
```

**改造点3：`submitScreenShotNotSend` 同样处理**

与 `submitScreenShotAndSend` 相同，移除 OneLimiter，增加 `RejectedExecutionException` 捕获。

**改造点4：清理无用字段**

`OneLimiter oneLimiter` 字段在两个接口都移除 OneLimiter 后不再被使用，一并删除。

---

### 3.4 ScreenshotRequestConsumer.java

**改造点1：恢复 `resendMsg()` 方法（去掉注释）**

当前第285~305行的 `resendMsg()` 方法已被注释，恢复如下：

```java
private void resendMsg(ScreenshotRequest request) {
    if (request == null) {
        log.error("screenshot resendMsg: request is null");
        return;
    }
    if (request.getRetryTimes() >= MAX_RETRY_COUNT) {
        log.info("screenshot resendMsg retry times exceed limit, url={}", request.getUrl());
        Cat.logEvent("screenshot.consumer.exception", "retryTimesExceed");
        return;
    }
    try {
        request.setRetryTimes(request.getRetryTimes() + 1);
        screenshotPublishService.sendRequest(request);
        Cat.logEvent("screenshot.forwardToCluster.resend", "resend.success");
    } catch (Throwable e) {
        log.error("screenshot resendMsg error, url={}", request.getUrl(), e);
        Cat.logEvent("screenshot.consumer.exception", "resendMsgError");
    }
}
```

**改造点2：`forwardToCluster()` 改造，解析响应 + CAT 打点 + 失败重投**

```java
private void forwardToCluster(ScreenshotRequest request) {
    List<String> ips = screenshotClusterIp;
    if (ips == null || ips.isEmpty()) {
        log.warn("screenshot forwardToCluster: screenshotClusterIp 未配置，fallback 本机执行 url={}", request.getUrl());
        Cat.logEvent("screenshot.forwardToCluster", "local");
        screenshotService.submit(request, true);
        return;
    }
    String ip = ips.get((int) (Math.random() * ips.size()));
    Cat.logEvent("screenshot.forwardToCluster", "resend");
    String targetUrl = String.format("http://%s:8080/screenshot/submitScreenShotAndSend", ip);
    log.info("screenshot forwardToCluster url={} targetIp={}", request.getUrl(), ip);

    String response = null;
    try {
        response = CrawlerRequestConsumer.sendPostRequest(targetUrl, JackSonUtils.toJson(request));
    } catch (Throwable e) {
        log.error("screenshot forwardToCluster http exception url={}", request.getUrl(), e);
        Cat.logEvent("screenshot.forwardToCluster.result", "http_exception");
        resendMsg(request);
        return;
    }

    // 解析响应，基于 ResponseBean 格式：成功 code=1，失败 code=-1
    if (response != null && !response.contains("\"code\":-1")) {
        Cat.logEvent("screenshot.forwardToCluster.result", "success");
    } else if (response != null && response.contains("the current limit has been reached")) {
        Cat.logEvent("screenshot.forwardToCluster.result", "reject_limit");
        resendMsg(request);
    } else {
        // threadpool_full、other_error、response为null 均归入此分支
        log.warn("screenshot forwardToCluster failed, response={}, url={}", response, request.getUrl());
        Cat.logEvent("screenshot.forwardToCluster.result", "other_error");
        resendMsg(request);
    }
}
```

---

## 四、新增配置项说明

| 配置项 | 类型 | 建议默认值 | 说明 |
|--------|------|-----------|------|
| `screenshot_download_thread_num` | Integer | 10 | 下载机内层线程池并发数，控制 Chrome 最大同时执行截图任务数，支持 Lion 动态修改 |

**保留配置项：**

| 配置项 | 改造后用途 |
|--------|-----------|
| `screenshot_thread_num` | 消费者机器外层线程池数，保持不变 |

**废弃配置项：**

| 配置项 | 说明 |
|--------|------|
| `screenshot_limit` | OneLimiter key，改造后代码中不再使用，可在 Lion 中废弃 |
| `screenshot-threadpool-max-wait-time` | 旧版超时重投逻辑已被新架构替代，可废弃 |

---

## 五、注意事项与风险点

**1. ThreadLocal 与线程池兼容性（重要）**

`ScreenshotPlaywrightService` 内部使用 `ThreadLocal<PlaywrightDownLoader>`（`INSTANCES_137/136/130`）绑定 Playwright 实例到线程。新增 `DOWNLOAD_TASK_POOL` 后，ThreadLocal 仍绑定在 `DOWNLOAD_TASK_POOL` 的工作线程上（线程复用），行为与改造前完全一致，无需额外处理。

**2. `future.get()` 导致 Tomcat 线程阻塞**

`ScreenshotService.download()` 调用 `submitTask().get()` 会阻塞 Tomcat 线程等待结果，这是刻意设计（保持 HTTP 接口同步语义 + 背压控制）。需确保 `screenshot_download_thread_num` 不超过 Tomcat 可用线程数（Tomcat 默认200），建议配置 10~20。

**3. `sendPostRequest` 返回 null 的兜底**

`CrawlerRequestConsumer.sendPostRequest()` 在异常时捕获后返回 null，改造后 `forwardToCluster()` 中对 `response == null` 需归入 `other_error` 分支并 resendMsg，已在上述代码中处理。

**4. `retryTimes` 字段序列化**

`resendMsg` 通过 `retryTimes >= MAX_RETRY_COUNT (2)` 防止无限循环。需确认 `ScreenshotRequest` 的 `retryTimes` 字段在 MQ 序列化/反序列化时能正确传递（Jackson 默认支持，int 字段无需额外配置）。

**5. 上线顺序建议（两步灰度）**

- **第一步**：先上线下载机改造（`ScreenshotPlaywrightService` 新增线程池 + `ScreenshotService.download()` 改造 + `ScreenshotController` 移除 OneLimiter），观察内层线程池监控和磁盘使用情况
- **第二步**：待第一步稳定后，上线消费者侧改造（`forwardToCluster()` 响应解析 + `resendMsg()` 恢复），完成整条链路闭环