# html-page-js-download 分支测试文档

> 分支：`dev/html-page-js-download`
> 对比基线：`266a63765eac507e2d48e60bd6863c4e7ac90ee0`
> 编写时间：2026-05-25
> 编写人：哈吉夏

---

## 一、本次变更核心内容

### 1.1 架构变化总览

| 维度 | 旧版（266a6376） | 新版（当前分支） |
|---|---|---|
| **Phase1 + Phase2** | 两个独立 Context，各自 navigate 一次（2次网络请求） | 合并为一个 Context，只 navigate 一次，减少 50% 网络请求 |
| **资源上传时机** | 抓取完毕后统一批量上传 S3 | 边抓边上传：每个文件截完/抓完立即上传并置 null，内存峰值大幅降低 |
| **ScreenshotCrawlResult 字段** | 持有 `assetBytes`（Map）、`resourcesTsv`、`screenshotBytes`、`annotatedBytes` | 全部移除，只保留 `htmlBytes` + `viewportResults`（只含位置和 elementsJson） |
| **下载线程池** | 无内层线程池，直接在外层 EXECUTOR 里同步执行 crawlScreenshot | 新增内层 `DOWNLOAD_TASK_POOL`（SynchronousQueue + AbortPolicy），submitTask 返回 CompletableFuture |
| **proxy 为 null 的处理** | 直接返回失败 result，phase1/phase2 均为 FAIL | fallback 到默认代理 `10.217.129.223:9090`，继续执行抓取 |
| **Consumer 请求转发** | 直接调本地 `screenshotService.submit` | 新增 `forwardToCluster` 逻辑，按 Lion 配置灰度转发到集群 IP |
| **Controller 接口名** | `testConsumerAndSend` / `testConsumerNotSend` | 改为 `submitScreenShotAndSend` / `submitScreenShotNotSend`，加了 Rhino 限流 |
| **实例最大存活时间** | 5 分钟（300000ms） | **1 分钟（60000ms）**，实例复用窗口变短，重建更频繁 |

### 1.2 S3 文件结构（新版）

每次抓取在 S3 目录 `screenshot/{yyyyMMdd}/{dirName}/` 下生成如下文件：

```
index.html                      # 页面 HTML 源码
resources.tsv                   # 静态资源清单（url\tlocalName\ttype\tstatus）
assets/{localName}              # 各 JS/CSS/图片资源字节
viewport_0/screenshot.png       # 第0个视口原始截图
viewport_0/annotated_screenshot.png  # 第0个视口标注截图
viewport_0/clickable_elements.json   # 第0个视口可点击元素列表
viewport_1/...                  # （若采样到多个视口）
```

### 1.3 两层线程池关系

```
外层 EXECUTOR（ScreenshotRequestConsumer）
    │  SynchronousQueue + AbortPolicy
    │  线程数：screenshot_thread_num（默认5）
    ▼
  forwardToCluster()
    │  本地执行时调用 screenshotService.submit()
    ▼
  ScreenshotPlaywrightService.DOWNLOAD_TASK_POOL（内层）
    │  SynchronousQueue + AbortPolicy
    │  线程数：screenshot_download_thread_num（默认10）
    ▼
  crawlScreenshot()（实际 Playwright 执行）
```

**关键约束**：外层线程在 `future.get()` 处阻塞，等待内层线程执行完毕。如果外层线程数 >= 内层线程数，则可能出现所有外层线程都卡在 future.get()，而内层线程池已满的死锁风险。

---

## 二、已知问题（需修复才能运行测试）

### 问题1：ScreenshotPlaywrightServiceTest.java 与当前代码不兼容，编译报错

**受影响文件**：`llm-crawler-server/src/test/java/com/sankuai/llm/spider/crawler/service/ScreenshotPlaywrightServiceTest.java`

| 测试方法 | 问题描述 |
|---|---|
| `testProxyServerEmpty_ThrowsIllegalArgument` | 调用老签名 `crawlScreenshot(String, Proxy, String, int)`，新签名多了 `String s3DirKey` 参数，**编译报错** |
| `testProxyNull_ThrowsIllegalArgument` | 同上，**编译报错** |
| `testProxyServerEmpty_ThrowsIllegalArgument` | 新代码已注释 proxy 防御检查，不再抛 IllegalArgumentException，**用例失败** |
| `testProxyNull_ThrowsIllegalArgument` | 同上，**用例失败** |
| `testInstanceSurviveMaxTime_Is5Minutes` / `TC-23` | 断言 `PLAYWRIGHT_INSTANCE_SURVIVE_MAX_TIME == 300000L`，实际已改为 `60000L`（1分钟），**断言失败** |

### 问题2：addRequestToThreadPool 超时重投逻辑全部注释

`ScreenshotRequestConsumer.addRequestToThreadPool` 中超时等待 → 重投 MQ 的逻辑被注释，现在是**无限 while 循环**等待线程池空位，外层 Consumer 线程可能永久卡住。

### 问题3：forwardToCluster 中 sentGray 为 null 时 NPE 风险

`Math.random() < sentGray` 当 `sentGray`（`@MdpConfig("screenshot_Sent_Gray")`）未配置时值为 null，会抛 NullPointerException，导致请求丢失。

### 问题4：Phase1 失败时 phase2Tx 可能未被 complete（CAT 资源泄漏）

当 `browser.newContext()` 或 `mergedContext.newPage()` 抛异常时，外层 catch 调用了 `phase1Tx.setStatus(e)` 和 `phase1Tx.complete()`，但 `phase2Tx` 也在同一个 try 块里被创建，外层 catch 又调用了 `phase2Tx.setStatus(e)` 和 `phase2Tx.complete()`——逻辑上没有泄漏，但需确认代码路径是否覆盖所有异常分支。

---

## 三、测试目标

| 编号 | 测试目标 | 覆盖的变更 |
|---|---|---|
| T1 | 合并 Phase 后 S3 文件完整性 | Phase1+2 合并，边上传边释放 |
| T2 | 内层线程池拒绝时 Controller 返回 threadpool_full | DOWNLOAD_TASK_POOL + AbortPolicy |
| T3 | proxy 为 null 时 fallback 到默认代理 | proxy null 处理逻辑变更 |
| T4 | forwardToCluster 灰度路由正确 | Consumer 新增转发逻辑 |
| T5 | sanitizeReason 对特殊字符清理 | 新增 sanitizeReason 方法 |
| T6 | 实例存活时间 1 分钟（原 5 分钟） | 常量变更 |
| T7 | UA 与代理类型映射不变 | 未改动，回归确认 |
| T8 | updateDownloadThreadNum 动态扩缩容 | 新增 Lion 配置热更新 |
| T9 | buildS3DirKey 目录格式正确 | s3Service 新增方法 |
| T10 | 两层线程池大小配置不当时行为 | 并发风险验证 |

---

## 四、测试分层

| 层级 | 类型 | 工具 | 是否需要真实环境 |
|---|---|---|---|
| L1 | 纯单元测试 | JUnit5 + Mockito（无需 Spring 容器） | 否 |
| L2 | 集成测试（HTTP 接口） | 本地启动 + curl/Postman | 需要代理、S3 |
| L3 | 端到端链路测试 | MQ 投递 | 需要完整环境 |

---

## 五、L1 单元测试用例

### 5.1 S3 路径与文件名构建（T9）

#### TC-NEW-01：buildS3DirKey 格式验证
- **测试类**：`S3ServiceTest`
- **测试方法**：`S3Service.buildS3DirKey(url)`
- **输入**：`url = "https://www.example.com/path"`
- **预期结果**：
  - 返回值以 `"screenshot/"` 开头
  - 中间段为 `yyyyMMdd` 格式日期
  - 以 `"/"` 结尾
  - 格式：`screenshot/20260525/www_example_com_path_xxxxxxxx_1234567890123/`

#### TC-NEW-02：buildScreenshotDirName 长度限制
- **测试方法**：`S3Service.buildScreenshotDirName(url)`
- **输入**：构造 200 字符以上的超长 URL
- **预期结果**：
  - safe 名称前缀不超过 60 字符
  - 格式：`{safe前60位}_{md5前8位}_{时间戳}`
  - 不含非法 S3 路径字符（只含 `[a-zA-Z0-9_-]`）

#### TC-NEW-03：buildScreenshotDirName 空URL兜底
- **输入**：`url = null` 或 `url = ""`
- **预期结果**：`dirName` 含 `"unknown"`，不抛异常

### 5.2 sanitizeReason 清理逻辑（T5）

#### TC-NEW-04：换行符被替换为空格
- **测试类**：`ScreenshotServiceTest`（通过反射调用私有方法）
- **输入**：`"error\nline2\rline3"`
- **预期结果**：`"error line2 line3"`

#### TC-NEW-05：制表符被替换为空格
- **输入**：`"error\tcause"`
- **预期结果**：`"error cause"`

#### TC-NEW-06：控制字符被删除
- **输入**：包含 `\x00`、`\x1F`、`\x7F` 的字符串
- **预期结果**：控制字符全部删除，正常字符保留

#### TC-NEW-07：连续空白被压缩为单个空格
- **输入**：`"error   cause"`（多个空格）
- **预期结果**：`"error cause"`

#### TC-NEW-08：超过 500 字符时截断
- **输入**：构造 600 字符字符串
- **预期结果**：返回长度恰好为 500

#### TC-NEW-09：null 输入返回 null
- **输入**：`null`
- **预期结果**：返回 `null`，不抛异常

### 5.3 内层线程池拒绝处理（T2）

#### TC-NEW-10：DOWNLOAD_TASK_POOL 满时 submitTask 抛 RejectedExecutionException
- **测试类**：`ScreenshotPlaywrightServiceTest`
- **测试方式**：通过反射将 `DOWNLOAD_TASK_POOL` 替换为全满的假线程池（所有线程卡住），然后调用 `submitTask`
- **预期结果**：抛出 `RejectedExecutionException`

#### TC-NEW-11：future.get() 包装后异常类型验证
- **测试内容**：CompletableFuture.supplyAsync 里抛出的 RejectedExecutionException，经 future.get() 后是否被包装为 ExecutionException
- **预期结果**：`future.get()` 抛出 `ExecutionException`，其 `getCause()` 为 `RejectedExecutionException`
- **影响**：`ScreenshotService.download()` 中只 catch `RejectedExecutionException`，若被包装则无法捕获，需确认

#### TC-NEW-12：updateDownloadThreadNum 扩容（新 > 旧）
- **测试内容**：模拟 Lion 配置变更，新值 > 当前 MaximumPoolSize
- **步骤**：
  1. 反射设置 `DOWNLOAD_TASK_POOL` 初始大小为 5
  2. 构造 ConfigEvent，newValue = "10"
  3. 调用 `updateDownloadThreadNum`
- **预期结果**：`DOWNLOAD_TASK_POOL.getCorePoolSize() == 10`，`getMaximumPoolSize() == 10`

#### TC-NEW-13：updateDownloadThreadNum 缩容（新 < 旧）
- **测试内容**：新值 < 当前大小
- **预期结果**：先设 corePoolSize 再设 maxPoolSize，不抛 IllegalArgumentException

### 5.4 proxy null 时 fallback 到默认代理（T3）

#### TC-NEW-14：proxy 为 null 时使用默认代理
- **测试类**：`ScreenshotServiceTest`
- **测试方式**：Mock `crawlerService.getProxyItemModel` 使最终 proxy 为 null，Mock `screenshotPlaywrightService.submitTask` 捕获传入的 proxy 参数
- **预期结果**：
  - 传入 `submitTask` 的 proxy.server 为 `"10.217.129.223:9090"`
  - CAT 打点 `screenshot.proxy` = `"default"`
  - 不返回 phase1/phase2 FAIL 的 result

#### TC-NEW-15：proxy host 无效时置 null 并走 fallback
- **输入**：finalProxy 的 host 为空字符串或 port = 0
- **预期结果**：proxy 被置 null，走 fallback 到默认代理

### 5.5 实例存活时间常量（T6）

#### TC-NEW-16：PLAYWRIGHT_INSTANCE_SURVIVE_MAX_TIME 为 1 分钟
- **测试方式**：反射读取常量
- **预期结果**：`PLAYWRIGHT_INSTANCE_SURVIVE_MAX_TIME == 60000L`（1分钟）
- **说明**：旧值为 300000L（5分钟），此用例验证常量已按预期修改

#### TC-NEW-17：实例 1 分钟后触发重建（替换旧 TC-19）
- **测试方式**：反射修改 `THREAD_TIME_130` 为 `now - 61000`（超过1分钟）
- **预期结果**：下次调用时触发 ThreadLocal remove + initScreenshotInstance

### 5.6 UA 与代理类型映射回归（T7）

> 以下用例沿用旧版，验证未改动逻辑没有退化

#### TC-09（回归）：OVERSEA_NORMAL → INSTANCES_137
- **预期结果**：`getInstanceHolder(OVERSEA_NORMAL.getWord())` 返回 `INSTANCES_137`

#### TC-10（回归）：OVERSEA_ADVANCED_LINE → INSTANCES_136
- **预期结果**：`getInstanceHolder(OVERSEA_ADVANCED_LINE.getWord())` 返回 `INSTANCES_136`

#### TC-11（回归）：MAINLAND_NORMAL → INSTANCES_130
- **预期结果**：`getInstanceHolder(MAINLAND_NORMAL.getWord())` 返回 `INSTANCES_130`

#### TC-12（回归）：未知 proxyType 默认返回 INSTANCES_130（国内线路）
- **预期结果**：`getInstanceHolder("unknown_type")` 返回 `INSTANCES_130`

#### TC-13（回归）：getUaByProxyType 与 getInstanceHolder 映射一致
- **预期结果**：三种线路 UA 字符串完全对应，且版本号正确（137/136/130）

### 5.7 forwardToCluster 路由逻辑（T4）

#### TC-NEW-18：screenshotClusterIp 为空时走本地执行
- **测试方式**：反射设置 `screenshotClusterIp = null`，Mock `screenshotService.submit`
- **预期结果**：直接调用 `screenshotService.submit`，不发 HTTP 请求

#### TC-NEW-19：isGray=false 时走本地执行
- **测试方式**：反射设置 `screenshotClusterIp = ["1.2.3.4"]`，`screenShotSentisGray = false`
- **预期结果**：走本地路径

#### TC-NEW-20：sentGray 为 null 时不抛 NPE（Bug 验证）
- **测试内容**：验证已知 NPE 风险
- **预期结果**：当前代码抛 NullPointerException（记录为 Bug）

---

## 六、L2 集成测试用例（HTTP 接口）

> 前置：服务本地启动，Lion 概率表已注入，S3 bucket 有写权限

### 6.1 submitScreenShotNotSend 接口（不发 MQ）

#### TC-INT-01：正常 URL 抓取，验证 S3 文件完整性
```
POST /screenshot/submitScreenShotNotSend
{
  "url": "https://www.example.com",
  "domain": "example.com"
}
```
**验证内容**：
1. 接口返回 `phase1Status="SUCCESS"`，`phase2Status="SUCCESS"`
2. S3 目录 `screenshot/{today}/{dirName}/` 下存在：
   - `index.html`（非空）
   - `resources.tsv`（如有静态资源）
   - `viewport_0/screenshot.png`
   - `viewport_0/clickable_elements.json`
3. `s3DirKey` 与 S3 实际路径一致

#### TC-INT-02：线程池满时返回 threadpool_full
- **方法**：同时发送超过 `screenshot_download_thread_num` 数量的长耗时请求
- **预期结果**：后续请求返回 `{"code": -1, "message": "threadpool_full"}`

#### TC-INT-03：Phase1 失败时 S3 目录内 index.html 缺失
- **方法**：传入无效 URL 或断网场景
- **预期结果**：`phase1Status="FAIL"`，`s3DirKey` 不为 null，但 S3 目录下 index.html 不存在（或为空）

#### TC-INT-04：旧接口名已不可用
- **方法**：`POST /screenshot/testConsumerAndSend`
- **预期结果**：返回 404

### 6.2 submitScreenShotAndSend 接口（发 MQ）

#### TC-INT-05：正常抓取后 MQ 消息可收到
- **验证内容**：MQ 消息中 `phase1Status`、`phase2Status`、`s3DirKey`、`html` 字段正确

#### TC-INT-06：Rhino 限流触发时返回 screenshot_qps_limited
- **方法**：短时间内超过 QPS 限制
- **预期结果**：返回 `{"code": -1, "message": "screenshot_qps_limited"}`

---

## 七、端到端链路测试（L3）

> 需要完整环境：Mafka Topic、代理池、S3、Lion 配置均可用

#### TC-E2E-01：完整 MQ → 抓取 → S3 → 结果 MQ 链路
1. 向 screenshot 请求 MQ 发送一条消息
2. 等待抓取完成（约 30~60s）
3. **验证**：
   - S3 目录下文件完整（index.html + assets + viewport_0）
   - 结果 MQ 消息到达，`phase1Status="SUCCESS"`
   - `s3DirKey` 格式正确，日期为当天

#### TC-E2E-02：forwardToCluster 灰度转发验证
1. Lion 配置 `screenshot_cluster_ip = ["目标机器IP"]`，`screenshot_Sent_isGray=true`，`screenshot_Sent_Gray=1.0`
2. 发送请求到消费者机器
3. **验证**：消费者机器日志出现 `forwardToCluster resend`，目标机器收到 HTTP 请求并执行抓取

---

## 八、回归检查清单

上线前必须通过以下核心用例：

| 用例 | 测试内容 | 通过标准 |
|---|---|---|
| TC-NEW-01 | buildS3DirKey 格式 | 100% |
| TC-NEW-04~09 | sanitizeReason 边界清理 | 100% |
| TC-NEW-10 | 线程池满时抛 RejectedExecutionException | 100% |
| TC-NEW-11 | future.get() 异常包装类型确认 | 100%（若有包装需同步修改 catch）|
| TC-NEW-14 | proxy null 走默认代理 | 100% |
| TC-NEW-16 | 实例存活时间常量为 1 分钟 | 100% |
| TC-09~13（回归）| UA 与代理类型映射 | 100% |
| TC-INT-01 | S3 文件完整性 | 100% |
| TC-INT-04 | 旧接口 404 | 100% |

---

## 九、需修复的已知问题（上线前必须处理）

| 编号 | 文件 | 问题描述 | 优先级 |
|---|---|---|---|
| BUG-01 | `ScreenshotPlaywrightServiceTest.java` | 测试方法调用老签名 `crawlScreenshot(String,Proxy,String,int)`，新签名多了 `s3DirKey` 参数，编译报错 | P0（编译阻断）|
| BUG-02 | `ScreenshotPlaywrightServiceTest.java` | `testProxyServerEmpty` / `testProxyNull` 断言抛 IllegalArgumentException，但新代码已注释该防御逻辑 | P1 |
| BUG-03 | `ScreenshotPlaywrightServiceTest.java` | `testInstanceSurviveMaxTime_Is5Minutes` / `TC-23` 断言 `300000L`，实际已改为 `60000L` | P1 |
| BUG-04 | `ScreenshotRequestConsumer.java` | `addRequestToThreadPool` 超时重投逻辑注释后无限等待，Consumer 线程可能永久阻塞 | P1 |
| BUG-05 | `ScreenshotRequestConsumer.java` | `forwardToCluster` 中 `sentGray` 为 null 时 `Math.random() < sentGray` 抛 NPE | P1 |
| BUG-06 | 架构 | 外层 EXECUTOR 线程数 >= 内层 DOWNLOAD_TASK_POOL 时，所有外层线程卡在 future.get()，内层又被 AbortPolicy 拒绝，死锁风险 | P1（配置依赖）|

---

## 十、环境配置说明

### L1 最小环境
```
JUnit5 + Mockito，无需 Spring 容器
```

### L2 本地测试环境 Lion 配置
```
screenshot_viewport_table = [[1280,800,0.5],[1920,1080,1.0]]
screenshot_dpr_table = [[1,0.7],[2,1.0]]
screenshot_num_viewports_table = [[1,0.6],[2,1.0]]
screenshot_download_thread_num = 10
screenshot_thread_num = 5
screenshot_timeout_ms = 30000
screenshot_Sent_Gray = 0.0
screenshot_Sent_isGray = false
```

### L3 完整环境
```
以上配置 +
Mafka Topic 已申请
S3 bucket llm-web-sitemap 写权限已授权
代理池 OVERSEA_NORMAL / MAINLAND_NORMAL 均可用
Playwright Chromium 已安装
```