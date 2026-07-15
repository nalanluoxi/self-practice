# yangjiajun 代码改动分析报告

> 统计时间：2026-05-27
> 涉及分支：`dev/20260526-init-browser`、`dev/add-req-header-20260424`（已合入 release）

---

## 一、改动总览

| 分支 | 提交时间 | 提交标题 | 核心方向 |
|------|---------|---------|---------|
| `dev/20260526-init-browser` | 2026-05-25 | 初始化浏览器 | 浏览器环境检测接口 |
| `dev/add-req-header-20260424` | 2026-05-11 | 添加antibot灰度流量 | Antibot 下载链路接入 |
| `dev/add-req-header-20260424` | 2026-04-25 | 测试快代理请求头&camoufox策略挖掘&不缓存配置化 | 请求头灰度收窄 + headless 恢复 |
| `dev/add-req-header-20260424` | 2026-04-24 | 添加请求头&无缓存配置化 | HTTP 请求头仿真 + Playwright 拦截器配置化 |
| `dev/add-js-proxy-no-cache-config` | 2026-04-17 | playwright错误分类 | Playwright 错误码精细化 |

---

## 二、各提交详细分析

---

### 2.1 初始化浏览器（2026-05-25）

**分支**：`dev/20260526-init-browser`
**文件**：`CrawlerController.java`

#### 实现方式

新增 HTTP GET 接口 `/init/browser`，在接口内部依次创建 Playwright 实例，并顺序启动 Chromium、Firefox、WebKit 三个浏览器（均使用 headless 模式）：

```java
@GetMapping("/init/browser")
public ResponseBean<String> initBrowser() {
    try (Playwright playwright = Playwright.create()) {
        playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
        playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }
    return ResponseBean.ok("ok");
}
```

每个浏览器启动前后通过 `logger.warn` 打印日志节点，失败时返回错误信息。

#### 结果

- 提供了一个运维可调用的浏览器环境探活接口
- 部署后可通过 curl 直接验证机器上三种浏览器驱动是否安装正常
- 排查浏览器缺失问题时不再需要登机器手动验证

---

### 2.2 添加 Antibot 灰度流量（2026-05-11）

**分支**：`dev/add-req-header-20260424`
**文件**：`CrawlerRequestConsumer.java`、`CrawlerController.java`、`AntibotDownloader.java`（新增）、`Downloader.java`、`HttpResponseError.java`、`DownloaderTypeEnum.java`、`SiteStrategyConfig.java`、`AntibotDownloadRequest.java`（新增）、`AntibotDownloadResponse.java`（新增）、`CrawlerService.java`、`PlaywrightService.java`、`SiteStrategyService.java`、`ProxySetProvider.java`、`strategy.html`

#### 实现方式

**1. 新增下载器类 `AntibotDownloader`（296行）**

专为 Antibot 服务设计的独立下载器，封装了与远程 Antibot 服务的 HTTP 通信逻辑，包括：
- 构建 `AntibotDownloadRequest` 请求体（含 URL、代理 IP、超时等参数）
- 调用 Antibot 服务接口并解析 `AntibotDownloadResponse` 响应
- 将响应结果转换为统一 `CrawledResult` 对象

**2. 灰度判断逻辑 `isAntibotGray()`**

在 `CrawlerRequestConsumer` 中新增方法，判断某个请求是否命中 Antibot 灰度，优先级如下：
1. `reqId` 以 `antibot_test` 开头 → 直接命中（测试入口）
2. `antibotClusterIp` 为空 → 不灰度
3. Lion 配置 `ALL_UNIT` → 100% 灰度
4. 按优先级查找 domain / topDomain 的灰度比例（Lion 配置 > Redis 配置），随机数命中比例则路由

**3. 消费者路由分支**

在请求处理主流程中，在 camoufox 灰度判断之后、JS 下载判断之前插入 Antibot 分支：

```
camoufox 灰度 → antibot 灰度（新增）→ JS 下载 → HTTP 下载
```

命中时：
- reqId 追加 `_ab` 后缀用于区分
- 设置 `downloader = ANTIBOT_DOWNLOADER`
- 随机选一个 Antibot 集群 IP 写入 ext 字段
- CAT 打点 `antibot.gray.hit`

**4. Mock 接口 `/antibot/mock`**

新增 POST 接口，绑定到 Antibot 下载器，使用海外代理（proxyWay=5），不发 Mafka（isSendMafka=-1），供手动测试直接调用 Antibot 链路：

```
POST /antibot/mock  →  AntibotDownloader  →  Antibot 服务
```

**5. 前端 strategy.html 批量提交功能**

新增「批量提交」按钮，弹出表单支持填入多个站点（逗号/换行/空格分隔），统一设置以下字段后批量写入 Redis：
- JS灰度（jsSpiderGray）
- Camoufox灰度比例
- 自定义代理列表
- JS无缓存代理列表
- 加载状态（0-4）
- 浏览器引擎（firefox/chromium）

#### 结果

- 完整打通了从消费者入口到 Antibot 服务的下载链路
- 支持按域名、顶级域名维度配置灰度比例，配置热更新（Lion + Redis 双通道）
- CAT 打点可观测灰度命中情况
- Mock 接口方便快速验证 Antibot 服务的抓取效果
- 批量提交功能大幅减少大规模站点配置的操作成本

---

### 2.3 测试快代理请求头效果（2026-04-25）

**分支**：`dev/add-req-header-20260424`
**文件**：`PlaywrightDownLoader.java`、`HttpClientGenerator.java`、`strategy.html`

#### 实现方式

**1. 请求头灰度收窄**

上一提交（4-24）对所有请求补全了浏览器仿真请求头（Accept、Sec-Ch-Ua 等），本次收窄为**仅对快代理（kuai）生效**：

```java
// 仅快代理走请求头仿真逻辑
if (proxy != null && proxy.getBrand().equals("kuai")) {
    addHeaderIfAbsent(requestBuilder, "Accept", ...);
    addHeaderIfAbsent(requestBuilder, "Sec-Ch-Ua", ...);
    // ... 其他浏览器头
}
```

原因：隧道代理（快代理）与请求头配合效果好，其他代理效果未知，先灰度快代理验证。

**2. 浏览器恢复 headless 模式**

将 Firefox 和 Chromium 的 `setHeadless(false)` 改回 `setHeadless(true)`（上次提交为本地调试临时改动，本次还原）。

**3. 删除 strategy.html 冗余前端代码**

移除了约 80 行不再使用的旧版站点策略配置 UI 代码。

#### 结果

- 请求头仿真范围精准收窄到快代理，避免对其他代理链路产生不确定影响
- 还原了生产环境浏览器 headless 配置

---

### 2.4 添加请求头 & 无缓存配置化（2026-04-24）

**分支**：`dev/add-req-header-20260424`
**文件**：`CrawlerController.java`、`PlaywrightDownLoader.java`、`PlaywrightService.java`、`HttpClientGenerator.java`、`strategy.html`

#### 实现方式

**1. HTTP 客户端浏览器请求头仿真（`HttpClientGenerator`）**

重构 `buildHttpUniRequest()` 方法的请求头构建逻辑，新增 `addHeaderIfAbsent()` 工具方法，按浏览器类型补全以下请求头：

| 请求头 | 值 | 说明 |
|------|---|-----|
| User-Agent | 随机 UA | 原有逻辑保留 |
| Accept | text/html,application/xhtml+xml,... | 模拟浏览器 Accept |
| Accept-Language | zh-CN,zh;q=0.9,en;q=0.8,... | 中英文语言偏好 |
| Cache-Control | no-cache | 禁用缓存 |
| Pragma | no-cache | 兼容旧协议无缓存 |
| Upgrade-Insecure-Requests | 1 | 模拟浏览器升级 HTTP→HTTPS |
| Sec-Ch-Ua | Chromium 版本信息 | 仅 Chrome UA 添加，自动解析版本号 |
| Sec-Ch-Ua-Mobile | ?0 | 非移动端 |
| Sec-Ch-Ua-Platform | Windows/macOS/Linux | 从 UA 自动推断 |
| Sec-Fetch-Dest | document | 现代浏览器请求元信息 |
| Sec-Fetch-Mode | navigate | |
| Sec-Fetch-Site | none | |
| Sec-Fetch-User | ?1 | |

**2. Playwright 拦截器域名配置化（`PlaywrightService`）**

原代码硬编码判断 reddit.com / stackoverflow.com 跳过 `page.onResponse` / `onDownload` / `route` 拦截器，改为从 Lion 配置 `playwright_skip_interceptor_domain` 读取，支持热更新：

```java
// 旧：硬编码
if (request.getDomain().endsWith("reddit.com") || request.getDomain().endsWith("stackoverflow.com")) {

// 新：配置化
if (isSkipInterceptorDomain(request.getDomain())) {
```

**3. 页面加载状态等待优化（`PlaywrightService`）**

`document.readyState` 等待逻辑新增对 `interactive` 状态的处理：当页面进入 `interactive` 状态后等待 10 秒，超时后视为加载完成，不再死等 `complete` 状态。解决部分站点 `complete` 状态长时间不到达导致超时的问题。

```java
// interactive 状态计时，10s 后强制通过
if (LOAD_STATE_INTERACTIVE.equals(evaluate) && waitStateTime.get() == 0) {
    waitStateTime.set(System.currentTimeMillis());
}
if (waitStateTime.get() > 0 && System.currentTimeMillis() - waitStateTime.get() > 10000) {
    return true;
}
```

**4. 删除硬编码的 `isStackoverflowOrReddit()` 方法**

该方法已被上述配置化逻辑替代，标注 `@Deprecated` 并删除。

**5. 新增 Camoufox Mock 接口 `/camoufox/mock`**

仿照现有模式，新增直接走 Camoufox 下载链路的 POST 接口，供手动测试 Camoufox 效果：

```
POST /camoufox/mock  →  CamoufoxDownloader  →  Camoufox 服务（海外代理）
```

**6. strategy.html 新增配置项**

前端页面新增 Antibot 相关配置字段的展示与编辑能力。

#### 结果

- HTTP 客户端发出的请求与真实浏览器高度一致，降低被反爬识别的概率
- 跳过拦截器的站点列表不再需要发版修改，可通过 Lion 热更新
- 部分因 `complete` 状态等待过长导致的超时问题得到缓解
- 新增 Camoufox Mock 接口方便独立验证 Camoufox 链路

---

### 2.5 Playwright 错误分类（2026-04-17）

**分支**：`dev/add-js-proxy-no-cache-config`
**文件**：`HttpResponseError.java`、`PlaywrightService.java`

#### 实现方式

**1. 新增 12 种 Playwright 网络错误码枚举（`HttpResponseError`）**

原有 Playwright 相关错误码仅 3 个（-501 限流、-502 未知、-503 未知、-504 Camoufox限流），全部网络异常统一归为 `-503 unknown`。本次细分 12 种网络错误：

| 错误码 | 枚举名 | 触发条件 |
|------|------|--------|
| -601 | PLAYWRIGHT_ERR_TIMED_OUT | `net::ERR_TIMED_OUT` |
| -602 | PLAYWRIGHT_ERR_TUNNEL_CONNECTION_FAILED | `net::ERR_TUNNEL_CONNECTION_FAILED` |
| -603 | PLAYWRIGHT_TIMEOUT | `Timeout exceeded` |
| -604 | PLAYWRIGHT_ERR_EMPTY_RESPONSE | `net::ERR_EMPTY_RESPONSE` |
| -605 | PLAYWRIGHT_ERR_ABORTED | `net::ERR_ABORTED` |
| -606 | PLAYWRIGHT_CONTEXT_DESTROYED | `Execution context was destroyed` |
| -607 | PLAYWRIGHT_PAGE_NAVIGATING | `page is navigating and changing the content` |
| -608 | PLAYWRIGHT_ERR_CONNECTION_RESET | `net::ERR_CONNECTION_RESET` |
| -609 | PLAYWRIGHT_ERR_CONNECTION_CLOSED | `net::ERR_CONNECTION_CLOSED` |
| -610 | PLAYWRIGHT_ERR_CONNECTION_REFUSED | `net::ERR_CONNECTION_REFUSED` |
| -611 | PLAYWRIGHT_ERR_NAME_NOT_RESOLVED | `net::ERR_NAME_NOT_RESOLVED` |
| -612 | PLAYWRIGHT_ERR_SSL | `net::ERR_SSL` |

**2. 新增静态解析方法 `parsePlaywrightError(String message)`**

通过 `message.contains()` 顺序匹配异常消息，自动返回对应错误枚举，兜底返回 `PLAYWRIGHT_UNKNOWN_ERROR`。

**3. `PlaywrightService` 接入精细化错误码**

将原先 catch 块中直接返回 `PLAYWRIGHT_UNKNOWN_ERROR` 的逻辑，替换为调用 `HttpResponseError.parsePlaywrightError(e.getMessage())`，实现自动分类。

#### 结果

- 原本全部归类为 `-503` 的 Playwright 网络异常，现在可以精确区分 12 种错误类型
- CAT 打点和告警可按错误类型聚合，方便定位是代理问题（隧道连接失败）、超时问题还是 SSL 问题
- 后续可按不同错误类型制定差异化重试策略

---

## 三、改动方向汇总

| 方向 | 涉及提交 | 核心价值 |
|------|---------|---------|
| **Antibot 链路接入** | 2026-05-11 | 完整新增第三条下载链路，支持灰度路由、CAT 监控 |
| **HTTP 请求头仿真** | 2026-04-24 / 04-25 | 模拟真实浏览器请求，降低反爬识别率（灰度快代理生效） |
| **Playwright 配置化** | 2026-04-24 | 拦截器跳过域名由 Lion 热配置，消除硬编码 |
| **页面加载容错** | 2026-04-24 | `interactive` 状态 10s 超时兜底，减少等待超时 |
| **错误码精细化** | 2026-04-17 | Playwright 网络错误从 1 种扩展到 12 种，可观测性提升 |
| **运维工具** | 2026-05-25 | 浏览器环境检测接口，方便部署验证 |
| **前端批量操作** | 2026-05-11 | strategy.html 批量提交，大幅降低站点配置操作成本 |