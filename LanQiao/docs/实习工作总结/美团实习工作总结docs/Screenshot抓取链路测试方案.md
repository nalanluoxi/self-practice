# Screenshot 抓取链路测试方案

> 整理人：小夏
> 适用模块：`llm-crawler-server` Screenshot 截图抓取功能
> 测试范围：从单元模块测试到完整链路集成测试，由简到难分层覆盖

---

## 一、测试分层概览

| 层级 | 类型 | 覆盖范围 | 触发方式 |
|------|------|----------|----------|
| L1 | 单元测试 | 过滤逻辑、工具方法、消息构建 | JUnit / Mock |
| L2 | 模块集成测试 | Consumer 过滤链路、Controller 接口 | HTTP 接口 / 本地启动 |
| L3 | 端到端链路测试 | 完整抓取 → S3 上传 → MQ 结果发送 | MQ 投递 / HTTP 触发 |
| L4 | 异常与边界测试 | 各阶段失败、边界输入 | 构造异常场景 |

---

## 二、L1 单元测试

### 2.1 ScreenshotRequestConsumer — preFilterProcess 各过滤阶段

#### TC-01：反序列化失败
- **目标**：非法 JSON 消息被正确丢弃
- **输入**：`msg = "not-json"`
- **预期**：返回 `CONSUME_SUCCESS`，CAT 记录 `jsonToObjectError`，不抛异常

#### TC-02：URL 为空
- **目标**：反序列化成功但 url 字段为空时丢弃
- **输入**：`msg = {"url":"", "domain":"example.com"}`
- **预期**：返回 `CONSUME_SUCCESS`，CAT 记录 `jsonToObjectError`

#### TC-03：消息过期丢弃
- **目标**：`dispatchTs` 早于阈值的消息被丢弃
- **前置**：Lion `screenshot_stale_threshold_hours = 48`
- **输入**：ext 中 `dispatchTs = now - 72小时`
- **预期**：返回 `CONSUME_SUCCESS`，CAT 记录 `stale`，日志含 `lagHours`

#### TC-04：消息未过期通过
- **输入**：ext 中 `dispatchTs = now - 1小时`
- **预期**：不被过期拦截，继续后续过滤

#### TC-05：非法 URL 格式
- **输入**：`url = "not-a-url"`
- **预期**：返回 `CONSUME_SUCCESS`，CAT 记录 `errorUrl`

#### TC-06：合法 URL 通过格式校验
- **输入**：`url = "https://www.example.com"`
- **预期**：通过格式校验，继续后续阶段

#### TC-07：黑名单域名被过滤
- **前置**：Lion `screenshot_black_domain_list` 包含 `"blocked.com"`
- **输入**：`domain = "blocked.com"`, `host = "www.blocked.com"`
- **预期**：返回 `CONSUME_SUCCESS`，CAT 记录 `blacklist`

#### TC-08：Rhino 限流触发重投
- **前置**：Mock `oneLimiter.run("screenshot_limit")` 返回 reject
- **预期**：调用 `screenshotPublishService.sendRequest(request)`，CAT 记录 `isLimited`，返回 `CONSUME_SUCCESS`

#### TC-09：Redis 去重命中
- **前置**：Mock `redisStoreClient.get(...)` 返回 `"1"`
- **预期**：返回 `CONSUME_SUCCESS`，CAT 记录 `hasBeenCrawled`，不提交线程池

#### TC-10：Redis 异常丢弃
- **前置**：Mock `redisStoreClient.get(...)` 抛异常
- **预期**：返回 `CONSUME_SUCCESS`，CAT 记录 `getRedisError`，消息被丢弃

#### TC-11：全部过滤通过，投入线程池
- **前置**：所有过滤条件均通过，线程池有空闲
- **预期**：`screenshotService.download(request)` 被调用，CAT 记录 `threadpool.enter`

---

### 2.2 ScreenshotService — download 逻辑

#### TC-12：proxy 获取失败终止
- **前置**：Mock `proxySetService.getJsNoCacheProxy` 返回 null
- **预期**：抛 `RuntimeException("proxy_is_null")`，发送 FAIL 结果消息，`errorReason = "RuntimeException: proxy_is_null"`

#### TC-13：proxy 获取异常终止
- **前置**：Mock `proxySetService.getJsNoCacheProxy` 抛异常
- **预期**：proxy 为 null，后续同 TC-12

#### TC-14：抓取成功 → S3 上传 → Redis 写入 → MQ 发送
- **前置**：Mock crawlResult.status="SUCCESS", httpCode=200
- **预期**：
  - `s3Service.uploadScreenshotResult(url, crawlResult)` 被调用，返回非空 s3DirKey
  - `redisStoreClient.set(...)` 被调用，key 为 `llm_screenshot_url:url`
  - `screenshotPublishService.sendResult(message)` 被调用
  - message.status="SUCCESS"，message.html 非空，message.reqId 以 `_result` 结尾

#### TC-15：httpCode 非 2xx 时 status 改为 FAIL
- **前置**：Mock crawlResult.status="SUCCESS", httpCode=404
- **预期**：最终 message.status="FAIL"，不调用 S3 上传和 Redis 写入

#### TC-16：S3 上传异常时仍发送 FAIL 消息
- **前置**：Mock `s3Service.uploadScreenshotResult` 抛异常
- **预期**：发送 FAIL 结果消息，errorReason 不为空，不抛出到调用方

#### TC-17：MQ 发送失败仅记录日志
- **前置**：Mock `screenshotPublishService.sendResult` 返回 false
- **预期**：日志记录 `screenshot result mq send failed`，CAT 打点，不抛异常

#### TC-18：Redis 写入失败不影响主流程
- **前置**：Mock `redisStoreClient.set(...)` 抛异常
- **预期**：仅 log.warn，结果消息正常发送

---

### 2.3 ScreenshotPlaywrightService — 工具方法

#### TC-19：proxy 为 null 时方法入口抛异常
- **输入**：`crawlScreenshot(url, null, 30000)`
- **预期**：抛 `IllegalArgumentException`

#### TC-20：proxy.server 为空时抛异常
- **输入**：proxy.server = `""`
- **预期**：抛 `IllegalArgumentException`

#### TC-21：非法 URL 时抛异常
- **输入**：`url = "not-a-url"`（proxy 有效）
- **预期**：抛 `IllegalArgumentException("invalid url: ...")`

#### TC-22：isMainlandDomain — 国内域名识别
- **输入**：`"www.baidu.com"`, `"example.cn"`, `"shop.taobao.com"`
- **预期**：均返回 `true`

#### TC-23：isMainlandDomain — 国际域名识别
- **输入**：`"www.google.com"`, `"github.com"`, `"stackoverflow.com"`
- **预期**：均返回 `false`

#### TC-24：加权概率采样分布验证
- **目标**：验证 `weightedSampleIndex` 采样结果在统计上符合概率表配置
- **方法**：构造概率表 `[[1280,800,0.5],[1920,1080,1.0]]`，采样 10000 次，验证两档占比在 45%~55% 之间

---

### 2.4 ScreenshotResultMessage — 字段完整性

#### TC-25：成功结果消息字段验证
- **前置**：crawlResult.status="SUCCESS", httpCode=200, htmlBytes 非空，proxy 有效
- **预期**：
  - `status = "SUCCESS"`
  - `html` 非空（Phase 1 页面源码）
  - `reqId` = `{原始reqId}_result`
  - `s3DirKey` 格式：`screenshot/{yyyyMMdd}/{dirName}/`
  - `errorReason = null`
  - `batchId` 字段不存在（已删除）
  - `htmlS3Key` 字段不存在（已删除）

#### TC-26：失败结果消息字段验证
- **前置**：crawl 异常
- **预期**：`status="FAIL"`，`errorReason` 非空，`s3DirKey=null`，`html=null`

---

## 三、L2 模块集成测试（HTTP 接口）

### 3.1 前置条件

1. 服务本地启动，Lion 三个概率表配置已注入（否则启动失败）
2. `screenshot_thread_num` 已配置（或使用默认 5）
3. Redis `llm_screenshot_url` category 已注册（或 Mock）
4. 以下测试不需要真实 MQ 和 S3，结果通过接口返回值验证

---

### 3.2 `POST /screenshot/testConsumer` — 单条请求测试

#### TC-27：正常请求进入流程
```json
POST /screenshot/testConsumer
{
  "url": "https://www.example.com",
  "domain": "example.com",
  "host": "www.example.com",
  "reqId": "test-001",
  "ext": "{\"batchId\":\"testBatch\",\"dispatchTs\":当前时间戳}"
}
```
- **预期**：`{"success":true, "consumeStatus":"CONSUME_SUCCESS", "url":"https://www.example.com"}`
- **验证**：日志出现 `ScreenshotRequestConsumer receive msg`，threadpool 状态 activeCount 增加

#### TC-28：URL 非法被过滤
```json
{"url": "not-a-url", "domain": "test", "host": "test"}
```
- **预期**：`consumeStatus = "CONSUME_SUCCESS"`，日志含 `非法url丢弃`

#### TC-29：已去重的 URL 被跳过
- **前置**：向 Redis 写入 `llm_screenshot_url:https://www.example.com = "1"`
- **预期**：`consumeStatus = "CONSUME_SUCCESS"`，日志含 `redis去重命中`，不新增 activeCount

#### TC-30：reqId 未填时正常处理
```json
{"url": "https://www.example.com", "domain": "example.com"}
```
- **预期**：正常返回，reqId 为空不影响流程

---

### 3.3 `POST /screenshot/testBatch` — 批量请求测试

#### TC-31：批量 URL 正常投递
```json
{
  "urls": ["https://www.google.com", "https://www.baidu.com", "https://www.github.com"],
  "batchId": "testBatch001"
}
```
- **预期**：`total=3`，`entered` 为 0~3（取决于 Redis 去重和限流）
- **验证**：`details` 中每个 URL 对应状态非空

#### TC-32：空 URL 列表返回错误
```json
{"urls": [], "batchId": "test"}
```
- **预期**：`{"success":false, "error":"urls 不能为空"}`

#### TC-33：包含空白 URL 被跳过
```json
{"urls": ["https://www.example.com", "", "  "], "batchId": "test"}
```
- **预期**：`total=3`，空白条目被 `continue` 跳过，不计入 details

#### TC-34：dispatchTs 为 0 时使用当前时间
```json
{"urls": ["https://www.example.com"], "batchId": "test", "dispatchTs": 0}
```
- **预期**：不被过期过滤拦截

---

### 3.4 `GET /screenshot/s3Path` — S3 路径查询

#### TC-35：正常 URL 返回路径
```
GET /screenshot/s3Path?url=https://www.example.com&dt=20250513
```
- **预期**：
  ```json
  {
    "bucket": "llm-web-sitemap",
    "s3DirKey": "screenshot/20250513/{safeName_md5前8位}/"
  }
  ```
- **验证**：`s3DirKey` 以 `screenshot/20250513/` 开头，以 `/` 结尾

#### TC-36：不传 dt 时使用当天日期
```
GET /screenshot/s3Path?url=https://www.example.com
```
- **预期**：`s3DirKey` 中日期部分 = 今天 `yyyyMMdd`

#### TC-37：URL 为空返回错误
```
GET /screenshot/s3Path?url=
```
- **预期**：`{"success":false, "error":"url 不能为空"}`

#### TC-38：相同 URL 不同时间返回一致 dirName
- **目标**：验证 `buildScreenshotDirName(url)` 确定性（基于 MD5）
- **方法**：对同一 URL 调用两次，`s3DirKey` 中的 dirName 部分相同

---

### 3.5 `GET /screenshot/threadpool/status` — 线程池状态

#### TC-39：线程池正常状态
```
GET /screenshot/threadpool/status
```
- **预期**：返回 `corePoolSize`、`activeCount`、`queueSize`、`completedTaskCount`、`taskCount`，全部为数字
- **验证**：`corePoolSize` 与 Lion `screenshot_thread_num` 配置值一致

#### TC-40：并发请求时 activeCount 增加
- **方法**：同时发送 3 条 testConsumer 请求，立即查询 threadpool/status
- **预期**：`activeCount > 0`

---

## 四、L3 端到端链路测试

> 需要完整环境：Mafka Topic 已申请、Redis Category 已注册、S3 bucket 已授权、代理池可用

### 4.1 完整抓取链路

#### TC-41：正常 URL 完整链路
- **步骤**：
  1. 通过 `POST /screenshot/testConsumer` 投入一条真实 URL（如 `https://www.example.com`）
  2. 等待 30~60s
- **验证**：
  - S3 bucket `llm-web-sitemap` 下路径 `screenshot/{today}/{dirName}/` 存在以下文件：
    - `index.html`（Phase 1 HTML 内容）
    - `resources.tsv`（资源清单）
    - `viewport_0/screenshot.png`（截图）
    - `viewport_0/annotated_screenshot.png`（标注截图）
    - `viewport_0/clickable_elements.json`（可点击元素）
  - 结果 MQ（`screenshotResultProducer` topic）收到一条消息：`status="SUCCESS"`, `html` 非空, `s3DirKey` 非空, `reqId` 以 `_result` 结尾
  - Redis `llm_screenshot_url:URL` 值为 `"1"`，TTL ≈ 7 天

#### TC-42：相同 URL 二次投递被去重
- **步骤**：TC-41 成功后，再次投递相同 URL
- **预期**：日志出现 `redis去重命中`，不触发新的抓取，S3 不产生新文件

#### TC-43：国内域名使用正确 UA
- **步骤**：投递 `https://www.baidu.com`
- **验证**：日志或 Phase 1 response headers 中 UA 包含 `Chrome/130`

#### TC-44：国际域名使用正确 UA
- **步骤**：投递 `https://www.github.com`
- **验证**：日志或 response headers 中 UA 包含 `Chrome/136`

#### TC-45：多视口采样验证
- **步骤**：投递一个高度超过 3 个视口的页面（如长文章页）
- **验证**：S3 中存在多个 `viewport_N/` 目录（数量 ≤ Lion `screenshot_num_viewports_table` 配置上限）

---

### 4.2 MQ 直接投递测试

#### TC-46：通过 MQ 消费触发抓取
- **步骤**：向 screenshotRequestConsumer 订阅的 topic 发送消息：
  ```json
  {
    "url": "https://www.example.com",
    "domain": "example.com",
    "host": "www.example.com",
    "reqId": "mq-test-001",
    "ext": "{\"batchId\":\"mqTest\",\"dispatchTs\":当前时间戳}"
  }
  ```
- **验证**：同 TC-41

---

## 五、L4 异常与边界测试

### 5.1 代理相关

#### TC-47：代理池为空时发送 FAIL 消息
- **前置**：Mock 代理池返回 null
- **预期**：结果 MQ 收到 `status="FAIL"`, `errorReason="RuntimeException: proxy_is_null"`

#### TC-48：代理连接超时
- **前置**：使用无效代理地址
- **预期**：`ScreenshotPlaywrightService.crawlScreenshot` 抛异常，结果消息 `status="FAIL"`，`errorReason` 包含超时信息

---

### 5.2 线程池满载

#### TC-49：线程池满时阻塞等待
- **前置**：`screenshot_thread_num = 1`，发送 3 条请求
- **预期**：第 2、3 条进入等待，CAT 记录 `threadpool.isfull`，最终按序处理

#### TC-50：线程池满且等待超时后重投
- **前置**：`screenshot_thread_num = 1`，`screenshot-threadpool-max-wait-time` 设小，`screenshot-max-retry-count` 设小
- **预期**：等待超时后调用 `screenshotPublishService.sendRequest(request)`，CAT 记录 `threadpool.isfull.retryDelivery`

---

### 5.3 边界输入

#### TC-51：超长 URL（2000+ 字符）
- **输入**：构造一个 2000 字符以上的合法 URL
- **预期**：流程不崩溃，S3 dirName 截断为 60 字符 + MD5 前 8 位

#### TC-52：特殊字符 URL
- **输入**：`https://www.example.com/path?a=1&b=中文&c=<script>`
- **预期**：URL encode 处理后 S3 路径合法，不包含非法文件名字符

#### TC-53：无 ext 字段的请求
- **输入**：`ext = null` 或不传
- **预期**：
  - 过期检查跳过
  - `reqId` 在结果消息中为空（不追加后缀）
  - 不抛异常

#### TC-54：reqId 为空时结果消息 reqId 为 null
- **输入**：`reqId = null`
- **预期**：结果消息 `reqId = null`，不出现 `"null_result"` 字符串

#### TC-55：页面 httpCode 为 5xx
- **前置**：目标 URL 返回 500
- **预期**：`ScreenshotService.download` 将 status 改为 "FAIL"，不上传 S3

#### TC-56：Phase 1 成功但 Phase 2 失败
- **前置**：Phase 2 抓取抛异常（如超时）
- **预期**：`viewportResults` 为空列表，但整体返回 SUCCESS（Phase 1 数据保留），S3 中只有 `index.html` 和 `resources.tsv`，无 `viewport_N/` 目录

---

### 5.4 实例管理

#### TC-57：Playwright 实例超时后被自动清理
- **方法**：等待 instanceMap 中实例超时（或手动设置极短超时），观察 `Screenshot-Cleanup-Thread` 日志
- **预期**：日志出现 `screenshot 清理过期实例完成`，`instanceMap size` 减少

#### TC-58：服务关闭时优雅释放资源
- **方法**：正常 JVM shutdown（`kill -15`）
- **预期**：日志出现 `ScreenshotPlaywrightService shutdown 完成`，无 Playwright 进程残留

---

## 六、回归检查清单

上线前或重大变更后必须通过以下核心用例：

| 编号 | 用例名称 | 最低通过标准 |
|------|----------|-------------|
| TC-11 | 正常消息投入线程池 | 100% |
| TC-14 | 完整成功流程 | 100% |
| TC-12 | proxy 为空终止 | 100% |
| TC-09 | Redis 去重命中 | 100% |
| TC-41 | 端到端 S3 文件验证 | 100% |
| TC-25 | 结果消息字段完整性 | 100% |
| TC-27 | /testConsumer 接口正常 | 100% |
| TC-35 | /s3Path 接口路径格式 | 100% |
| TC-39 | 线程池状态接口正常 | 100% |
| TC-51 | 超长 URL 不崩溃 | 100% |

---

## 七、测试环境配置

### 最小测试环境（L1+L2）
```
Lion 配置（必须）：
  screenshot_viewport_table = [[1280,800,0.5],[1920,1080,1.0]]
  screenshot_dpr_table = [[1,0.7],[2,1.0]]
  screenshot_num_viewports_table = [[1,0.6],[2,1.0]]
  screenshot_thread_num = 2
  screenshot_playwright_thread_num = 2

Redis（必须）：
  Category llm_screenshot_url 已注册

Rhino（必须）：
  入口 screenshot_limit 已注册，QPS 设为较大值（如 100）
```

### 完整测试环境（L3+L4）
```
以上配置 +
Mafka Topic 已申请并填入 mafka.properties
S3 bucket llm-web-sitemap 写权限已授权
代理池服务可用（getJsNoCacheProxy 返回非空）
Playwright 浏览器依赖已安装
```