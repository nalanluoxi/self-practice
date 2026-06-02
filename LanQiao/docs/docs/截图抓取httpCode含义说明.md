# 截图抓取 httpCode 含义说明

## 一、正常状态码

| httpCode | 含义 | 说明 |
|----------|------|------|
| 200 | 页面正常加载 | navigate 成功，页面返回 HTTP 200 |
| 301 / 302 | 重定向 | Playwright 跟随重定向后返回的最终状态码 |
| 404 | 页面不存在 | 目标 URL 已失效 |
| 403 | 访问被拒绝 | 目标站点拒绝爬虫访问 |
| 其他 4xx / 5xx | 服务端错误 | 目标站点返回的 HTTP 错误码 |

---

## 二、特殊状态码（非标准 HTTP）

| httpCode | 含义 | 触发原因 | 是否继续抓取 |
|----------|------|----------|-------------|
| **-1** | 无响应 | Playwright `navigate()` 返回了 null response，通常发生在目标页面无任何 HTTP 响应（如纯 JS 跳转、本地缓存命中） | 是，继续执行 Phase1/Phase2 |
| **-2** | LOAD 超时 | `navigate()` 等待 `window.load` 事件超时（超过 `screenshot_timeout_ms`，默认 30s），页面主体大概率已渲染完成 | 是，继续执行 Phase1/Phase2 |

---

## 三、LOAD 等待策略说明

代码使用的等待策略是 `WaitUntilState.LOAD`，对应浏览器的 `window.load` 事件。

**触发条件**：页面所有资源（HTML + CSS + JS + 图片）全部加载完毕后触发。

**与其他策略对比**：

| 策略 | 等待条件 | 超时风险 |
|------|----------|----------|
| `COMMIT` | 收到 HTTP 响应头即完成 | 最低 |
| `DOMCONTENTLOADED` | DOM 解析完成（不含图片等资源） | 低 |
| `LOAD` | 所有资源加载完毕（含图片、CSS、JS） | **高**（当前使用） |
| `NETWORKIDLE` | 网络空闲 500ms | 最高 |

**选用 LOAD 的原因**：需要等待 CSS、JS、字体等静态资源加载完成后，`onResponse` 回调才能收集到完整的资源列表，用于 Phase1 资源收集。

**超时为何继续执行**：第三方埋点、广告脚本等异步请求可能导致 `window.load` 迟迟不触发，但页面主体内容已渲染完成，强行中断会丢失有效数据，因此超时后继续执行并标记 `httpCode=-2`。

---

## 四、httpCode 与 Phase 状态组合解读

| httpCode | phase1Status | phase2Status | 解读 |
|----------|-------------|-------------|------|
| 200 | SUCCESS | SUCCESS | 完全成功 |
| -2 | SUCCESS | SUCCESS | LOAD 超时但页面已渲染，内容完整抓取 |
| -1 | SUCCESS | SUCCESS | Playwright 未返回响应，但页面实际加载成功 |
| 200 | FAIL | FAIL | navigate 成功但后续处理异常（S3 上传失败、Playwright 实例被清理等） |
| -1 | FAIL | FAIL | 页面无响应且抓取失败 |
| 任意 | SUCCESS | FAIL | Phase1 资源收集成功，Phase2 截图失败（phase2Enabled=false 或截图异常） |
