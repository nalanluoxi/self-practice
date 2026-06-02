# ScreenshotPlaywrightService TODO 分析报告

> 整理人：小聂
> 文件：`llm-crawler-server/src/main/java/com/sankuai/llm/spider/crawler/service/ScreenshotPlaywrightService.java`
> 共发现 TODO 注释 11 处（含重复行共 13 处注释行）

---

## 一、需立即修复（3 处）

### 1.1 Line 90 — instanceMap 内存泄漏

**注释：** `//TODO 检查是否会超时删除，是否正常续期`

**上下文代码：**
```java
static final ConcurrentHashMap<PlaywrightDownLoader, Long> instanceMap = new ConcurrentHashMap<>();
//TODO 检查是否会超时删除，是否正常续期
```

**问题描述：**
`instanceMap` 记录截图专用 Playwright 实例的存活截止时间戳（key=实例，value=超时时间戳 ms）。写入逻辑存在于 `crawlScreenshot` 中：
```java
instanceMap.put(instance, System.currentTimeMillis() + timeout + 10000L);
```
但当前代码**只写入，从不清理**。没有后台扫描线程移除超时条目，也没有续期逻辑（实例复用时更新时间戳）。

**结论：** 真实 Bug。随着时间推移，`instanceMap` 条目只增不减，造成内存泄漏。

**修复建议：**
1. 补一个后台定时线程（如每 30s 执行一次），扫描 `instanceMap`，移除 `value < System.currentTimeMillis()` 的超时条目，并对对应实例调用 `close()`；
2. 每次 `crawlScreenshot` 开始时，更新当前线程实例的存活截止时间（续期）：`instanceMap.put(instance, System.currentTimeMillis() + timeout + 10000L)`（当前只在末尾写一次，应在方法入口就续期）。

---

### 1.2 Line 291 — onResponse 回调中死代码

**注释：** `//todo 这个respUrl没有使用，获取有什么意义?`

**上下文代码：**
```java
page.onResponse(response -> {
    try {
        String respUrl = response.url();
        //todo 这个respUrl没有使用，获取有什么意义?
        String contentType = "";
        try {
            contentType = response.headers().getOrDefault("content-type", "");
        } catch (Exception ignored) { }
        if (contentType.contains("javascript") || ...) {
            collectedResponses.add(response);
        }
    } catch (Exception e) { ... }
});
```

**问题描述：**
`respUrl` 被赋值后在 `onResponse` 回调内从未使用。资源 URL 的处理逻辑已移到外层 `for (Response resp : collectedResponses)` 循环，该行是遗留死代码。

**结论：** 死代码，直接删除 `String respUrl = response.url();` 这一行。

---

### 1.3 Lines 358/364 — page/context 关闭异常被完全吞掉

**注释：** `//TODO 补充日志和打点`（两处）

**上下文代码：**
```java
} finally {
    try {
        page.close();
    } catch (Exception ignored) {
        //TODO 补充日志和打点
    }
    try {
        context.close();
    } catch (Exception ignored) {
        //TODO 补充日志和打点
    }
}
```

**问题描述：**
Phase 1 的 `page` 和 `context` 关闭时若抛出异常，当前代码完全静默吞掉，Playwright 资源发生泄漏时无任何感知手段。

**结论：** 需补充日志和 CAT 打点，否则资源泄漏完全不可观测。

**修复建议：**
```java
try {
    page.close();
} catch (Exception e) {
    log.warn("screenshot phase1 page close error, url={}", url, e);
    Cat.logEvent("screenshot.close.error", "page");
}
try {
    context.close();
} catch (Exception e) {
    log.warn("screenshot phase1 context close error, url={}", url, e);
    Cat.logEvent("screenshot.close.error", "context");
}
```

---

## 二、可直接删除 TODO 注释（4 处）

### 2.1 Line 84 — Lion 监听同步机制无问题

**注释：** `//TODO 检查是否更新后会同步监听同步`

**上下文代码：**
```java
private volatile double[][] cachedViewportTable;
private volatile double[][] cachedDprTable;
private volatile double[][] cachedNumViewportsTable;
//TODO 检查是否更新后会同步监听同步
```

**分析：**
三个 `@MdpConfigListener` 方法（`updateViewportTable`、`updateDprTable`、`updateNumViewportsTable`）监听的 Lion key 与 `@MdpConfig` 注入字段完全对应，MDP 框架在配置变更时会可靠回调 listener，`cached*` 字段会被同步更新。`volatile` 关键字保证了多线程可见性。机制完整，无问题。

**处理：** 直接删除此 TODO 注释。

---

### 2.2 Lines 206/208 — CAT 采样打点有明确意义

**注释：** `//TODO 意义?`（两处）

**上下文代码：**
```java
Cat.logEvent("screenshot.sample.viewport", viewportWidth + "x" + viewportHeight);
//TODO 意义?
Cat.logEvent("screenshot.sample.numViewports", String.valueOf(numViewports));
//TODO 意义?
```

**分析：**
这两个打点记录每次抓取采样到的视口分辨率和视口数，用于在 CAT 监控平台上观测**概率表的实际采样分布**。通过 Event 统计可以验证 Lion 概率表配置是否生效、各分辨率档位的实际占比是否符合预期，是调试和运营阶段的重要可观测手段。

**处理：** 删除 TODO 注释，补充说明性注释，例如：
```java
// 记录采样结果，用于 CAT 监控概率表实际分布是否符合 Lion 配置预期
Cat.logEvent("screenshot.sample.viewport", viewportWidth + "x" + viewportHeight);
Cat.logEvent("screenshot.sample.numViewports", String.valueOf(numViewports));
```

---

### 2.3 Line 217 — URL 解析失败无需抛异常

**注释：** `//TODO 是否应该抛出异常终止流程?`

**上下文代码：**
```java
String domain = url;
try {
    java.net.URL parsedUrl = new java.net.URL(url);
    domain = parsedUrl.getHost();
} catch (Exception ignored) {
    //TODO 是否应该抛出异常终止流程?
}
```

**分析：**
`domain` 变量仅用于 CAT 打点的 key，不参与核心抓取逻辑。URL 合法性校验在上游 `ScreenshotRequestConsumer.preFilterProcess` 中已通过 `UrlUtils.isValidUrl` 完成，走到这里的 URL 几乎不会解析失败。即便解析失败，只是 CAT 打点的 domain 显示为完整 URL 字符串，对抓取无影响。

**处理：** 直接删除此 TODO 注释，无需修改逻辑。

---

### 2.4 Lines 349/351 — Phase 1 完成打点有意义

**注释：** `//TODO 这个打点是什么意义?`

**上下文代码：**
```java
Cat.logEvent("screenshot.crawl.phase1", domain);
//TODO 这个打点是什么意义?
```

**分析：**
该打点记录 Phase 1（HTML 抓取）成功完成的事件，配合同文件中 `Cat.logEvent("screenshot.crawl.phase1.error", domain)` 共同构成**成功/失败双边打点**，可在 CAT 上按域名统计 Phase 1 成功率和各站点抓取健康度。

**处理：** 删除 TODO 注释，补充说明注释：
```java
// Phase 1 成功完成打点，配合 phase1.error 计算成功率
Cat.logEvent("screenshot.crawl.phase1", domain);
```

---

## 三、后续优化项（4 处）

### 3.1 Line 224 — UA 缓存策略优化

**注释：** `//TODO 这里是不是参考playwright的把不用ua的分别缓存`

**当前代码：**
```java
SCREENSHOT_INSTANCES.set(new PlaywrightDownLoader(RandomUserAgent.getRandomChromeUserAgent()));
```

**建议：**
`PlaywrightService` 按 UA 类型（国内/国外）维护了多个 `ThreadLocal` 实例缓存。截图服务目前对所有域名统一使用随机 Chrome UA，可能导致中文 UA 访问国际站被识别为异常请求。后续可参考 `PlaywrightService` 的实现，根据 URL 归属地选择对应 UA 类型并分别缓存。当前不影响功能，**优先级低**。

---

### 3.2 Line 286 — CopyOnWriteArrayList 性能优化

**注释：** `//TODO 这里会不会有性能问题?`

**当前代码：**
```java
CopyOnWriteArrayList<Response> collectedResponses = new CopyOnWriteArrayList<>();
```

**建议：**
Playwright 的 `onResponse` 回调虽然在 IO 线程触发，但同一 Page 的回调是串行的，实际上不需要线程安全容器。可直接改用 `new ArrayList<>()`，避免 `CopyOnWriteArrayList` 每次写入时的数组复制开销。**优先级低**。

---

### 3.3 Line 378 — 两次获取 browser 引用

**注释：** `//TODO 上面已经获取一次browser，为什么这里还要再获取一次?`

**当前代码：**
```java
// Phase 1
Browser browser = instance.getBrowser();
// ... 若干代码 ...
// Phase 2
Browser browser = instance.getBrowser();  // 再次获取
```

**建议：**
`getBrowser()` 只是返回内部持有的 Browser 引用，无副作用，两次调用语义安全。但可以在方法开头统一获取并传给两个阶段，代码更清晰。这是纯代码风格问题，**优先级最低**。

---

### 3.4 Line 385 — proxy 防御性非空检查

**注释：** `//TODO 是否应该兜底一下，在最开始，如果没有传入proxy就抛出异常?`

**上下文代码：**
```java
if (proxy != null) {
    contextOptions2.setProxy(proxy);
}
//TODO 是否应该兜底一下，在最开始，如果没有传入proxy就抛出异常?
```

**建议：**
`ScreenshotService.download()` 已在调用 `crawlScreenshot` 前做了 `proxy == null` 时抛 `RuntimeException("proxy_is_null")` 的强制拦截，不会裸跑到这里。但作为防御性编程，可在 `crawlScreenshot` 方法入口加：
```java
Objects.requireNonNull(proxy, "proxy must not be null");
```
使方法契约自闭合，与调用方的校验形成双重保险。**优先级低，可选**。

---

## 四、汇总表

| 类别 | 数量 | 涉及行号 |
|------|------|----------|
| 需立即修复 | 3 处 | Line 90、Line 291、Lines 358/364 |
| 可直接删除 TODO | 4 处 | Line 84、Lines 206/208、Line 217、Lines 349/351 |
| 后续优化项 | 4 处 | Line 224、Line 286、Line 378、Line 385 |
| **合计** | **11 处** | — |

> **优先处理顺序：** 内存泄漏（Line 90）> 死代码（Line 291）> 异常吞掉（Lines 358/364）> 其余可按迭代节奏处理