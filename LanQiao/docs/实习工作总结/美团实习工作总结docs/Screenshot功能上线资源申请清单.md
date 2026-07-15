# Screenshot 功能上线资源申请清单

> 整理人：刘霞
> 适用模块：`llm-crawler-server` Screenshot 截图抓取功能
> 说明：以下资源均需在**正式上线前**完成申请和配置，部分资源缺失会导致服务**启动失败**或**静默降级**

---

## 一、Mafka MQ

### 1.1 截图请求生产者（screenshotRequestProducer）

| 项目 | 内容 |
|------|------|
| 用途 | 发送截图抓取请求（Rhino 限流时重投、线程池满时重投） |
| Bean 名称 | `screenshotRequestProducer` |
| 配置文件位置 | `profiles/test/mafka.properties` `profiles/prod/mafka.properties` → `producer[9]` |
| topicName | **待申请，当前为空** llm_screenshot_request |
| bgNameSpace | test: `common` / prod: `pingtai` |
| appkey | `com.sankuai.llm.spider.crawler` |
| 注意 | Topic 未填会导致发送方法报错，限流重投和线程池满重投均失效 |

### 1.2 截图结果生产者（screenshotResultProducer）

| 项目 | 内容 |
|------|------|
| 用途 | 发送截图抓取结果消息，下游 Spark 消费写入 Hive |
| Bean 名称 | `screenshotResultProducer` |
| 配置文件位置 | `profiles/test/mafka.properties` `profiles/prod/mafka.properties` → `producer[10]` |
| topicName | **待申请，当前为空** llm_screenshot_result|
| bgNameSpace | test: `common` / prod: `pingtai` |
| appkey | `com.sankuai.llm.spider.crawler` |
| 注意 | Topic 未填会导致结果消息无法发出，Hive 表数据全部缺失 |

### 1.3 截图请求消费者（screenshotRequestConsumer）

| 项目 | 内容 |
|------|------|
| 用途 | 消费上游调度系统发出的截图请求，触发抓取链路 |
| listenerId | `screenshotRequestConsumer`（对应 `ScreenshotRequestConsumer` 类的 `@MdpMafkaMsgReceive` 方法） |
| 配置文件位置 | `profiles/test/mafka.properties` `profiles/prod/mafka.properties` → `consumer[4]` |
| topicName | **待申请，当前为空** |
| subscribeGroup | **待申请，当前为空** |
| bgNameSpace | test: `common` / prod: `pingtai` |
| appkey | `com.sankuai.llm.spider.crawler` |
| 注意 | Topic 和 subscribeGroup 均需申请；subscribeGroup 建议命名为 `llm_screenshot_url_consumer` |

---

## 二、Redis

### 2.1 截图 URL 去重 Key

| 项目 | 内容 |
|------|------|
| 用途 | 记录已成功抓取的 URL，防止重复抓取 |
| Redis 集群 | `redisClient0`（与 sitemap 共用，集群名 `redis-llm-data_product`） |
| Category（分类 key） | `llm_screenshot_url`（常量 `REDIS_CATEGORY_SCREENSHOT_URL`） |
| TTL | 7 天（`7 * 24 * 3600` 秒） |
| 值格式 | `"1"`（字符串，仅标记存在） |
| 代码位置 | `ScreenshotService.java` 抓取成功后写入；`ScreenshotRequestConsumer.java` 步骤 6 读取去重 |
| 申请事项 | 在 **Squirrel 控制台**为 `llm_screenshot_url` 注册新 Category，关联到 `redis-llm-data_product` 集群，设置 TTL 策略 |
| 注意 | Category 未注册会导致 Squirrel 拒绝读写，去重逻辑报错后**整条消息被丢弃**（当前代码在 Redis 异常时 return CONSUME_SUCCESS） |

---

## 三、Lion 配置（MDP Config）

以下配置项均通过 `@MdpConfig` 注入，**未配置时值为 null**，其中标记 ⚠️ 的项目 null 时会导致启动失败或 NPE。

### 3.1 ScreenshotRequestConsumer 相关

| Lion Key | 类型 | 用途 | 默认行为（未配置时） | 优先级 |
|----------|------|------|---------------------|--------|
| `screenshot_black_domain_list` | `HashSet<String>` | 黑名单域名集合，命中则丢弃请求 | null → 黑名单检查直接跳过（无过滤） | 上线前配置 |
| `screenshot_stale_threshold_hours` | `Integer` | 消息过期阈值（小时） | null → 代码兜底使用 48 小时 | 可后续配置 |
| `screenshot_thread_num` | `Integer` | 消费者线程池大小 | null → 代码兜底使用 5 | 上线前配置 |
| `screenshot-threadpool-max-wait-time` | `long` | 线程池满时最大等待时间（ms） | 字段初始值 36000ms | 可后续配置 |
| `screenshot-max-retry-count` | `int` | 等待超时后最大重试次数 | 字段初始值 10 | 可后续配置 |

### 3.2 ScreenshotPlaywrightService 相关

| Lion Key | 类型 | 用途 | 默认行为（未配置时） | 优先级 |
|----------|------|------|---------------------|--------|
| `screenshot_playwright_thread_num` | `Integer` | Playwright 抓取线程池大小 | null → 代码兜底使用 4 | 上线前配置 |
| `screenshot_viewport_table` ⚠️ | `List<List<Double>>` | 视口分辨率概率表 `[[width,height,累积概率],...]` | **null → `@PostConstruct` 调用 `toDoubleArray(null)` 抛 NPE，服务启动失败** | **上线前必须配置** |
| `screenshot_dpr_table` ⚠️ | `List<List<Double>>` | 设备像素比概率表 `[[dpr,累积概率],...]` | **null → 启动失败** | **上线前必须配置** |
| `screenshot_num_viewports_table` ⚠️ | `List<List<Double>>` | 采样视口数概率表 `[[视口数,累积概率],...]` | **null → 启动失败** | **上线前必须配置** |
| `screenshot_position_table` | `List<List<Double>>` | 视口位置偏重概率表 `[[row,col,累积概率],...]`，用于首屏优先采样 | null 或空 → 退化为均匀随机采样，功能正常 | 可后续配置 |

**概率表配置（基于实际屏幕分布，可直接使用）：**

```json
// screenshot_viewport_table
// 覆盖主流桌面分辨率，按 StatCounter 2024 分布加权
[[1024,768,0.18],[1280,720,0.30],[1280,800,0.35],[1280,1024,0.40],[1366,768,0.52],[1440,900,0.59],[1536,864,0.67],[1600,900,0.74],[1920,1080,0.94],[2560,1440,1.00]]

// screenshot_dpr_table
// 覆盖 1x~3x DPR，1.5x 为笔记本主流
[[1.0,0.25],[1.25,0.45],[1.5,0.70],[2.0,0.90],[3.0,1.00]]

// screenshot_num_viewports_table
// 70% 单视口，20% 双视口，10% 三视口
[[1,0.70],[2,0.90],[3,1.00]]

// screenshot_position_table
// (0,0) 首屏位置 50% 概率优先命中，其余从剩余格子随机补足
[[0,0,0.50],[0,1,1.00]]
```

> 格式说明：每行最后一列为**累积概率**，末行必须为 1.0。`screenshot_position_table` 中 `[0,1,1.00]` 为 others 占位行，代码会自动从剩余格子随机补足，row/col 值不影响逻辑。

---

## 四、Rhino 限流

| 项目 | 内容 |
|------|------|
| 限流入口名 | `screenshot_limit` |
| 限流器实例 | `ScreenshotRequestConsumer.oneLimiter`（独立实例，与 sitemap/crawler 完全隔离） |
| 用途 | 控制整体截图抓取 QPS，限流时将请求重投 MQ 而非丢弃 |
| 申请事项 | 在 **Rhino 平台**注册入口 `screenshot_limit`，配置单机或集群级别 QPS 阈值 |
| 注意 | 入口未注册时 `oneLimiter.run("screenshot_limit")` 返回 null，代码判断 `limitResult == null` 时**触发限流重投**，等效于所有请求都被限流，截图功能完全不工作 |

---

## 五、S3 存储

| 项目 | 内容 |
|------|------|
| Bucket 名称 | `llm-web-sitemap` |
| 路径前缀 | `screenshot/{yyyyMMdd}/{urlDirName}/` |
| 存储内容 | 各视口截图（`viewport_N/screenshot.png`）、标注图（`annotated_screenshot.png`）、可点击元素 JSON（`clickable_elements.json`）、原始 HTML（`index.html`）、资源清单（`resources.tsv`）、各类静态资源 |
| Bucket 状态 | **已存在**（Sitemap 功能已在使用） |
| 申请事项 | 确认 `com.sankuai.llm.spider.crawler` 的 AppKey 对 `llm-web-sitemap` 的 `screenshot/` 路径有**写入权限** |
| 注意 | 权限不足会导致 S3 上传失败，结果消息中 `s3DirKey` 为 null，状态变为 FAIL |

---

## 六、其他依赖

### 6.1 代理池服务（ProxySetService）

| 项目 | 内容 |
|------|------|
| 用途 | 为每次截图抓取提供 HTTP 代理，无代理时**直接终止不裸跑** |
| 依赖方法 | `proxySetService.getJsNoCacheProxy(proxyReq)` |
| 代理类型 | JS 渲染无缓存代理（与普通抓取代理相同接口） |
| 注意 | 代理池为空或接口异常时，`ScreenshotService.download` 抛 `proxy_is_null` 异常，该 URL 标记 FAIL 并发送失败结果消息；需确认代理池容量能支撑截图场景的并发量（Playwright 抓取耗时远高于普通 HTTP 请求） |

### 6.2 Playwright 运行时

| 项目 | 内容 |
|------|------|
| 用途 | 执行 Chrome 无头浏览器截图抓取 |
| 依赖 | `playwright` 及对应浏览器二进制（`PlaywrightDownLoader` 类负责管理实例） |
| 注意 | 部署机器需预装 Playwright 所需的浏览器和系统依赖（`libglib2.0`、`libnss3` 等）；容器部署时需确认 Docker 镜像中已包含 |

### 6.3 CAT 监控告警

| 建议配置的告警规则 | 说明 |
|-------------------|------|
| `screenshot.crawl.phase1.error` 事件量突增 | Phase 1 HTML 抓取大量失败 |
| `screenshot.crawl.phase2.error` 事件量突增 | Phase 2 截图大量失败 |
| `screenshot.proxy.null` 事件出现 | 代理池耗尽，截图功能停摆 |
| `screenshot.result.mq.send.error` 事件出现 | 结果消息发送失败，Hive 数据缺失 |
| `screenshot.threadpool.activeCount` 持续等于 `coreCount` | 线程池长期打满，需扩容 |

---

## 七、申请顺序建议

```
第一步（必须，影响启动）：
  Lion 配置 → screenshot_viewport_table / screenshot_dpr_table / screenshot_num_viewports_table
  （三项缺一不可，缺少会导致服务启动失败）
  Lion 配置 → screenshot_position_table（可选，未配置退化为均匀随机，不影响启动）

第二步（必须，影响核心功能）：
  Mafka Topic 申请 → screenshotRequestProducer / screenshotResultProducer / screenshotRequestConsumer
  Redis Category 注册 → llm_screenshot_url
  Rhino 入口注册 → screenshot_limit

第三步（必须，影响上线前验证）：
  S3 写权限确认 → llm-web-sitemap 的 screenshot/ 路径
  代理池容量评估 → 确认截图场景并发需求

第四步（上线后补充）：
  CAT 告警规则配置
  Lion 其他配置项调优（screenshot_thread_num / screenshot_playwright_thread_num 等）
```

> **最高风险项**：`screenshot_viewport_table` / `screenshot_dpr_table` / `screenshot_num_viewports_table` 三个 Lion 概率表配置——这三项没有任何代码兜底，**未配置直接导致服务启动失败**，必须在部署前完成配置。`screenshot_position_table` 为可选项，未配置时自动退化为均匀随机采样。