# Screenshot 抓取模块测试计划

## 一、整体链路说明

```
Mafka MQ (请求)
       ↓
ScreenshotRequestConsumer.receive()
       ↓
  preFilterProcess()  ← 可通过 HTTP 接口绕过 MQ 直接调用
  ├─ 反序列化
  ├─ 过期消息丢弃（dispatchTs + staleThresholdHours）
  ├─ URL 格式校验
  ├─ 黑名单过滤（blackDomainSet）
  ├─ Rhino 整体 QPS 限流（超限 → 重投请求 MQ）
  ├─ Redis 去重（llm_screenshot_url）
  └─ 投入线程池（EXECUTOR，SynchronousQueue + AbortPolicy）
       ↓
  ScreenshotService.download()
  ├─ 获取代理（ProxySetService）
  ├─ ScreenshotPlaywrightService.crawlScreenshot()
  │   ├─ Phase 1：HTML + JS/CSS/图片资源抓取
  │   └─ Phase 2：多视口截图 + 元素标注（numViewports 采样）
  ├─ HTTP Code 校验 → 决定 SUCCESS/FAIL
  ├─ S3Service.uploadScreenshotResult()（SUCCESS 时）
  ├─ Redis 写去重 key（TTL=7天）
  └─ ScreenshotPublishService.sendResult() → 结果 MQ
       ↓
Mafka MQ (结果)  →  Spark 消费 → Hive
```

**测试入口替代方案**（用 HTTP 接口代替 Mafka 投递）：

| 场景 | 接口 |
|------|------|
| 单条完整链路测试 | `POST /screenshot/testConsumer` |
| 批量 URL 测试 | `POST /screenshot/testBatch` |
| 查询 S3 目录路径 | `GET /screenshot/s3Path?url=&dt=` |
| 查看线程池状态 | `GET /screenshot/threadpool/status` |

---

## 二、测试分层结构

```
T1 - 单元级：过滤逻辑校验（不依赖任何外部服务）
T2 - 集成级：preFilterProcess 各过滤阶段联调（依赖 Redis/Rhino）
T3 - 链路级：完整抓取→S3→结果MQ（依赖全部外部服务）
T4 - 异常级：各组件异常降级/重试行为验证
T5 - 压力级：并发/线程池满载行为验证
```

---

## 三、T1 — 过滤逻辑单元测试

### T1-1 反序列化

| # | 输入 | 期望 consumeStatus | 日志关键字 |
|---|------|--------------------|-----------|
| 1 | 合法 JSON | CONSUME_SUCCESS（进入后续流程） | - |
| 2 | 空字符串 / null | CONSUME_SUCCESS | `jsonToObjectError` |
| 3 | 非 JSON 字符串 `hello` | CONSUME_SUCCESS | `jsonToObjectError` |
| 4 | JSON 中 url 字段为空 | CONSUME_SUCCESS | `jsonToObjectError` |

**测试方式**：直接 POST `/screenshot/testConsumer`，构造不同 body。

---

### T1-2 过期消息丢弃

| # | ext.dispatchTs | staleThresholdHours（MDP 默认48h） | 期望 |
|---|---------------|------------------------------------|------|
| 1 | 当前时间（不过期） | 48 | 进入后续流程 |
| 2 | 当前时间 - 47h | 48 | 进入后续流程 |
| 3 | 当前时间 - 49h | 48 | 丢弃，CAT `stale` |
| 4 | dispatchTs 为 0 | 任意 | 跳过过期判断，进入后续流程 |
| 5 | ext 字段为 null | 任意 | 跳过过期判断，进入后续流程 |

**测试方式**：
```bash
# T1-2-3：构造 49h 前的时间戳
curl -X POST http://<host>/screenshot/testConsumer \
  -H 'Content-Type: application/json' \
  -d '{
    "url": "https://www.example.com",
    "domain": "example.com",
    "host": "www.example.com",
    "ext": "{\"batchId\":\"test\",\"dispatchTs\": <currentMs - 180000000>}"
  }'
```

---

### T1-3 URL 格式校验

| # | url 值 | 期望 |
|---|--------|------|
| 1 | `https://www.example.com` | 通过 |
| 2 | `http://example.com/path?q=1` | 通过 |
| 3 | `ftp://example.com` | 通过（java.net.URL 可解析） |
| 4 | `not-a-url` | 丢弃，CAT `errorUrl` |
| 5 | `www.example.com`（无协议头） | 丢弃，CAT `errorUrl` |
| 6 | 超长 URL（>2000字符） | 验证是否能正常处理 |

**测试方式**：POST `/screenshot/testConsumer`，修改 `url` 字段。

---

### T1-4 黑名单过滤

| # | domain / host | blackDomainSet（MDP 配置） | 期望 |
|---|---------------|---------------------------|------|
| 1 | `example.com` | 不含该域名 | 通过 |
| 2 | `example.com` | 包含 `example.com` | 丢弃，CAT `blacklist` |
| 3 | domain=`a.com`，host=`www.b.com`，黑名单含 `b.com` | - | 通过（黑名单匹配精确 host） |
| 4 | domain=`a.com`，host=`b.com`，黑名单含 `b.com` | - | 丢弃 |

**测试前提**：在 MDP/Lion 控制台配置 `screenshot_black_domain_list` 包含测试域名。

---

## 四、T2 — preFilterProcess 集成阶段测试

> 以下测试依赖服务正常启动，Redis/Rhino 可用。

### T2-1 正常消息完整过滤流程

**请求体（基准 case）**：
```json
POST /screenshot/testConsumer
{
  "url": "https://www.meituan.com",
  "domain": "meituan.com",
  "host": "www.meituan.com",
  "reqId": "t2-001",
  "ext": "{\"batchId\":\"testBatch2\",\"dispatchTs\": <currentMs>}"
}
```

**验收标准**：
- 返回 `{"success": true, "consumeStatus": "CONSUME_SUCCESS"}`
- 日志中看到 `threadpool.enter` 日志
- CAT 埋点 `screenshot.msg.consume.statistics` 下出现 `threadpool.enter`

---

### T2-2 Redis 去重验证

1. 第一次发送某 URL → 过滤通过，进入线程池
2. 抓取完成后（SUCCESS）Redis 写入去重 key
3. 再次发送相同 URL → 被 Redis 去重拦截

**验收**：第二次调用日志输出 `redis去重命中`，CAT 出现 `hasBeenCrawled`

**注意**：如需重置去重状态，直接用 Redis 客户端执行：
```
DEL llm_screenshot_url:<url>
```

---

### T2-3 Rhino 限流触发

**前提**：在 Rhino 控制台将 `screenshot_limit` 入口 QPS 调为 0（或极小值 1）。

**步骤**：
1. 快速调用 `POST /screenshot/testBatch`，传入 10 个 URL
2. 预期部分请求被 Rhino 拒绝，触发重投请求 MQ

**验收**：日志出现 `Rhino限流拦截`，CAT 出现 `isLimited`，`screenshotRequestProducer` 发送成功。

---

### T2-4 线程池满载等待

**前提**：将 MDP 配置 `screenshot_thread_num` 调为 1。

**步骤**：
1. 发送 3 个 URL（第一个会占满线程池）
2. 第 2、3 个请求应等待线程池释放，超过 MAX_WAIT_TIME（36s）后重投 MQ

**验收**：日志出现 `线程池已满等待超时，重新发送消息`，CAT `threadpool.isfull.retryDelivery`。

---

### T2-5 批量接口测试

```bash
POST /screenshot/testBatch
{
  "urls": [
    "https://www.meituan.com",
    "https://www.dianping.com",
    "not-a-url",
    ""
  ],
  "batchId": "testBatch003",
  "dispatchTs": 0
}
```

**验收**：
- `total`: 4（含空字符串）
- `entered`: 2（meituan/dianping 成功进入线程池）
- `details` 中 `not-a-url` 的状态体现 CONSUME_SUCCESS（URL 校验失败丢弃）
- 空字符串 URL 被 `continue` 跳过，不计入 details

---

## 五、T3 — 完整抓取链路测试

> 依赖：代理服务、Playwright、S3、结果 MQ 均正常。

### T3-1 基础抓取成功路径

**输入**：
```json
POST /screenshot/testConsumer
{
  "url": "https://www.example.com",
  "domain": "example.com",
  "host": "www.example.com",
  "ext": "{\"batchId\":\"e2eTest\",\"dispatchTs\": <currentMs>}"
}
```

**验收检查清单**：
- [ ] consumeStatus 返回 CONSUME_SUCCESS
- [ ] 线程池 activeCount +1（通过 `GET /screenshot/threadpool/status` 轮询）
- [ ] 日志出现 `screenshot download done, url=..., status=SUCCESS`
- [ ] S3 目录存在（通过 `GET /screenshot/s3Path?url=https://www.example.com` 获取路径后到 S3 控制台验证）
  - `screenshot/{yyyyMMdd}/{dirName}/index.html`
  - `screenshot/{yyyyMMdd}/{dirName}/resources.tsv`
  - `screenshot/{yyyyMMdd}/{dirName}/assets/` 下存在 JS/CSS 资源
  - `screenshot/{yyyyMMdd}/{dirName}/viewport_0/screenshot.png`
  - `screenshot/{yyyyMMdd}/{dirName}/viewport_0/annotated_screenshot.png`
  - `screenshot/{yyyyMMdd}/{dirName}/viewport_0/clickable_elements.json`
- [ ] 结果 MQ 有消息（`screenshotResultProducer` 发送成功），消息字段验证：
  - `status = "SUCCESS"`
  - `httpCode` 在 200~399 之间
  - `s3DirKey` 与推算路径一致
  - `htmlS3Key = s3DirKey + "index.html"`
  - `batchId = "e2eTest"`
  - `ext` 字段原样透传
- [ ] Redis 中存在去重 key（TTL 约 604800s）

---

### T3-2 HTTP 4xx 抓取结果为 FAIL

**输入**：一个返回 404 的 URL（如 `https://www.example.com/this-page-does-not-exist-404`）

**验收**：
- 结果 MQ 消息 `status = "FAIL"`
- `httpCode = 404`
- S3 **不存在** 对应目录
- Redis **不写**去重 key

---

### T3-3 代理为 null 时终止抓取

**构造条件**：关闭代理服务或配置使代理获取失败。

**验收**：
- 日志出现 `proxy_is_null`
- 结果 MQ 消息 `status = "FAIL"`, `errorReason = "RuntimeException: proxy_is_null"`
- CAT 出现 `screenshot.proxy.null`

---

### T3-4 S3 目录路径验证工具

```bash
# 不传 dt → 自动使用当天日期
GET /screenshot/s3Path?url=https://www.meituan.com

# 指定历史日期
GET /screenshot/s3Path?url=https://www.meituan.com&dt=20250512
```

预期返回：
```json
{
  "bucket": "llm-web-sitemap",
  "s3DirKey": "screenshot/20250512/www_meituan_com_<hash8>/"
}
```

---

### T3-5 多视口采样参数验证

**验证 Lion 概率表配置是否生效**：

| 配置项 | 默认值（若 Lion 未配置） | 影响 |
|--------|------------------------|------|
| `screenshot_viewport_table` | 需 Lion 配置 | 控制视口宽高 |
| `screenshot_dpr_table` | 需 Lion 配置 | 控制设备像素比 |
| `screenshot_num_viewports_table` | 需 Lion 配置 | 控制截图视口数量 |

**检查方式**：
- 查看日志 `screenshot crawlScreenshot sampled params url=... viewport=...x... dpr=... numViewports=...`
- 确认 `numViewports` 与 `viewport_N` 目录数量一致（N 从 0 开始）

---

## 六、T4 — 异常降级测试

### T4-1 Phase 1 HTML 抓取失败（网络超时）

**构造**：修改 timeout 为极小值（如 100ms），或用无法访问的内网 IP。

**验收**：
- Phase 1 异常被 try-catch 吞掉，不影响 Phase 2
- 最终结果 `status = "SUCCESS"`（Phase 2 截图成功）
- `htmlBytes = null`，S3 不上传 `index.html`

---

### T4-2 Phase 2 截图失败

**构造**：Phase 2 中注入异常（或配置无效代理使 Phase 2 导航失败）。

**验收**：
- `viewportResults` 为空列表
- S3 不存在 `viewport_N` 目录
- 结果 MQ 的 `status` 取决于 `httpCode`（Phase 1 抓取到 httpCode 且在 200~399 → SUCCESS）

---

### T4-3 S3 上传失败

**构造**：关闭/错误配置 S3 endpoint。

**验收**：
- `uploadScreenshotBytes` 异常被 catch，CAT 记录 `screenshot.s3.upload.error`
- `uploadScreenshotResult` 方法返回 `s3DirKey`（即使部分文件上传失败，方法不抛异常）
- 结果 MQ 仍发送，`status = "SUCCESS"`（因为只要不抛出异常）

---

### T4-4 结果 MQ 发送失败

**构造**：关闭/错误配置结果 MQ Producer。

**验收**：
- 日志出现 `screenshot result mq send failed`
- CAT 出现 `screenshot.result.mq.send.error`
- 不影响 Redis 去重 key 的写入

---

### T4-5 download 方法整体异常兜底

**构造**：让 `screenshotPlaywrightService.crawlScreenshot` 抛出 RuntimeException。

**验收**：
- catch 块发送 FAIL 结果消息：`httpCode = -1`，`status = "FAIL"`，`errorReason` 包含异常类名
- `downloadTx.complete()` 在 finally 中被调用，不泄漏 CAT Transaction

---

### T4-6 Playwright 实例超时刷新

**Playwright 实例存活时间**：5 分钟（`PLAYWRIGHT_INSTANCE_SURVIVE_MAX_TIME = 300000ms`）

**验证方式**：
1. 启动服务后发送请求
2. 等待 5 分钟以上再次发送请求
3. 检查日志是否出现旧实例关闭、新实例创建的日志

---

## 七、T5 — 并发与线程池压测

### T5-1 线程池状态监控

发送请求前先轮询线程池：
```bash
GET /screenshot/threadpool/status
```

预期正常状态：
```json
{
  "corePoolSize": 5,
  "activeCount": 0,
  "queueSize": 0,
  "completedTaskCount": 0,
  "taskCount": 0
}
```

---

### T5-2 并发批量请求

```bash
POST /screenshot/testBatch
{
  "urls": [
    "https://www.meituan.com",
    "https://www.dianping.com",
    "https://www.zhihu.com",
    "https://www.baidu.com",
    "https://www.taobao.com"
  ],
  "batchId": "stressTest001"
}
```

**并发观察步骤**：
1. 发送批量请求
2. 立即循环调用 `GET /screenshot/threadpool/status`，观察 `activeCount`
3. 等待全部完成后验证 S3 和结果 MQ

**验收标准**：
- `activeCount` 最大不超过 `corePoolSize`（默认 5）
- 所有 URL 最终有结果 MQ 消息（SUCCESS 或 FAIL）
- 没有请求被 AbortPolicy 直接丢弃（因为 `addRequestToThreadPool` 等待线程池空闲）

---

### T5-3 动态调整线程数

1. 初始 `screenshot_thread_num = 2`
2. 发送 3 个请求（第 3 个等待）
3. 通过 MDP 控制台将 `screenshot_thread_num` 修改为 4
4. 观察第 3 个请求是否进入执行

**验收**：线程数变更生效，CAT 不出现异常。

---

## 八、测试环境准备 Checklist

| 依赖项 | 配置要点 |
|--------|---------|
| MDP Config | `screenshot_thread_num`、`screenshot_black_domain_list`、`screenshot_stale_threshold_hours` |
| MDP Config | `screenshot_playwright_thread_num`、`screenshot_viewport_table`、`screenshot_dpr_table`、`screenshot_num_viewports_table` |
| Redis | `redisClient0` 可访问，分类 `llm_screenshot_url` 可读写 |
| Rhino | `screenshot_limit` 入口配置合理 QPS |
| S3 | `s3PlusClient0` 可访问，`llm-web-sitemap` 桶有写权限 |
| Mafka | `screenshotResultProducer`、`screenshotRequestProducer` 配置正确 |
| 代理服务 | `ProxySetService.getJsNoCacheProxy()` 能正常返回代理 |
| Playwright | `PlaywrightDownLoader` 能正常初始化 Chromium 实例 |

---

## 九、快速验收 Checklist（每次发布前执行）

```
[ ] 1. GET /screenshot/threadpool/status  → 线程池已初始化，activeCount=0
[ ] 2. POST /screenshot/testConsumer (正常URL) → consumeStatus=CONSUME_SUCCESS，日志无异常
[ ] 3. 等待约 30s 后检查 S3 对应目录    → index.html + viewport_0/screenshot.png 存在
[ ] 4. 检查结果 MQ                       → 消息 status=SUCCESS，httpCode 正常
[ ] 5. POST /screenshot/testConsumer (相同URL) → 日志出现 redis去重命中
[ ] 6. POST /screenshot/testConsumer (无效URL) → 日志出现 errorUrl，不进入抓取
[ ] 7. GET /screenshot/s3Path            → 返回正确的 bucket 和 s3DirKey
```

---

## 十、CAT 监控埋点参考

| CAT 事件 key | 触发条件 |
|-------------|---------|
| `screenshot.msg.consume.statistics` / `all` | 每条消息进入 preFilterProcess |
| `screenshot.msg.consume.statistics` / `stale` | 过期消息丢弃 |
| `screenshot.msg.consume.statistics` / `errorUrl` | URL 格式非法 |
| `screenshot.msg.consume.statistics` / `blacklist` | 黑名单命中 |
| `screenshot.msg.consume.statistics` / `isLimited` | Rhino 限流 |
| `screenshot.msg.consume.statistics` / `hasBeenCrawled` | Redis 去重命中 |
| `screenshot.msg.consume.statistics` / `threadpool.enter` | 成功进入线程池 |
| `screenshot.proxy.null` | 代理获取为空，终止抓取 |
| `screenshot.s3.upload.error` | S3 上传失败 |
| `screenshot.result.mq.send.error` | 结果 MQ 发送失败 |
| `screenshot.threadpool.download` / `threadpool.isfull` | 线程池满，等待中 |
| `screenshot.threadpool.download` / `threadpool.isfull.retryDelivery` | 等待超时，重投 MQ |
| `screenshot.crawl.phase1` | Phase 1 HTML 抓取成功 |
| `screenshot.crawl.phase2` | Phase 2 截图成功 |