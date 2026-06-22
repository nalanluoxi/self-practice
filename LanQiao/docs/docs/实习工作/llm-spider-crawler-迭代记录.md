# llm-spider-crawler release 分支迭代记录

> 基准提交：`c826ff6`（线程池阻塞不重投递）
> 统计范围：`c826ff6..HEAD`（release 分支）
> 整理时间：2026-06-17

---

## 一、总览

| # | PR编号 | 分支名 | 合并时间 | 核心方向 | 文件变更 |
|---|--------|--------|----------|---------|---------|
| 1 | **#125** | `dev/html-page-js-download` | 2026-05-20 | 截图+JS+HTML 抓取能力建设 | 29 个文件，+2911/-143 行 |
| 2 | **#127** | `dev/add-antibot-domain-affinity` | 2026-06-02 | Antibot 代理绑定 + 域名亲和性路由 | 6 个文件，+451/-24 行 |

---

## 二、PR #125 — 截图+JS+HTML 抓取能力建设

**合并提交**：`bccd222`
**PR 标题**：dev html page js download
**合并人**：jiziyan

### 2.1 背景与目标

在原有 Sitemap 纯文本链接抓取之外，新增基于 Playwright 的**截图 + JS渲染 + HTML** 抓取能力，满足大模型对网页视觉内容（页面截图、可交互元素、资源文件）的采集需求。

### 2.2 新增核心模块

#### ① ScreenshotPlaywrightService（核心服务，1041行）

截图抓取服务的主体，负责 Playwright 实例管理、两阶段抓取与结果封装。

**实例管理设计：**
- `ThreadLocal<PlaywrightDownLoader>` 按**代理线路（proxyType）**分三组隔离
  - `INSTANCES_137`：海外普通线路（Chrome 137，新加坡时区）
  - `INSTANCES_136`：海外高级线路（Chrome 136，英国时区）
  - `INSTANCES_130`：国内普通线路（Chrome 130）
- 实例存活时间 5 分钟（`PLAYWRIGHT_INSTANCE_SURVIVE_MAX_TIME = 300s`），到期强制重建
- 独立后台清理线程（`CLEAN_UP_THREAD`，每5秒扫描）负责释放过期实例，与 Sitemap 实例池完全隔离
- 实例存活时间通过 `instanceMap`（`ConcurrentHashMap<PlaywrightDownLoader, Long>`）统一管理，每次使用时续期

**两阶段抓取流程（`crawlScreenshot`）：**

```
Phase 1 — HTML 抓取
  └── 新建 BrowserContext（注入代理、locale/timezone、bypassCSP）
  └── page.navigate(url, timeout)
  └── 抓取 HTML + 静态资源（图片/CSS/JS bytes）→ resourcesTsv
  └── 上传到 S3

Phase 2 — 多视口截图 + DOM 元素提取
  └── 按概率表采样参数：
      ├── viewport（宽高，Lion 配置 screenshot_viewport_table）
      ├── DPR（设备像素比，screenshot_dpr_table）
      └── numViewports（采样视口数，screenshot_num_viewports_table）
  └── 随机 + 位置权重采样截图坐标（screenshot_position_table）
  └── JS 注入提取可交互 DOM 元素（tag/text/bbox/href/aria_label/styles）
  └── 截图上传 S3
```

**概率表配置化（Lion 动态推送）：**

| Lion配置Key | 含义 | 格式 |
|------------|------|------|
| `screenshot_viewport_table` | 视口宽高概率表 | `[[width,height,累积概率],...]` |
| `screenshot_dpr_table` | 设备像素比概率表 | `[[dpr,累积概率],...]` |
| `screenshot_num_viewports_table` | 采样视口数概率表 | `[[num,累积概率],...]` |
| `screenshot_position_table` | 视口位置偏重概率表 | `[[row,col,累积概率],...]` |

- 每次采样使用 `url.hashCode()` 作为随机 seed，同一 URL 重试时参数一致，便于排查
- Lion 配置变更时由 `@MdpConfigListener` 异步更新缓存 `double[][]`，避免每次重复转换

**JS DOM 提取逻辑：**
- 内联 JS 脚本（`JS_DOM_EXTRACT`）：遍历所有 DOM 节点，筛选可见+可交互元素（INPUT/BUTTON/A/IFRAME/VIDEO 等）
- 返回字段：`tag`、`text`、`aria_label`、`bbox`（[left,top,right,bottom]）、`href`、`input_type`、`placeholder`、`styles`（color/fontSize/hasIcon）
- 过滤条件：area ≥ 20px²、在视口内可见、叶子节点优先（子节点更精确时过滤父节点）

#### ② ScreenshotRequestConsumer（线程池消费者，310行）

与 Sitemap 消费者完全隔离的独立 MQ 消费者。

**线程池设计：**
- `SynchronousQueue` + `AbortPolicy`（与 Sitemap 线程池策略一致）
- 线程数由 Lion `screenshot_thread_num` 动态配置，支持热更新（`@MdpConfigListener`）
- 每秒上报线程池活跃数/核心数到 CAT（`screenshot.threadpool.activeCount`、`screenshot.threadpool.coreCount`）

**消费流程：**
1. 反序列化 → 解析 domain/host
2. URL 合法性校验
3. 黑名单过滤（`screenshot_black_domain_list`）
4. 陈旧消息过滤（`screenshot_stale_threshold_hours`）
5. While 循环检查线程池空闲 → 投递，超时告警，最多重试 `MAX_RETRY_COUNT` 次
6. 异常兜底：`RejectedExecutionException` → `CONSUME_FAILURE`（触发 MQ 重投）

#### ③ ScreenshotService / ScreenshotPublishService

- `ScreenshotService`：封装代理选取 + 调用 `ScreenshotPlaywrightService.crawlScreenshot()`，处理重试逻辑
- `ScreenshotPublishService`：将 `ScreenshotCrawlResult` 序列化后发送到下游 Mafka Topic，支持 GZip 压缩发送（`mafka.properties` 新增 screenshot 消费者组配置）

#### ④ 辅助工具新增

| 文件 | 新增内容 |
|------|---------|
| `FilterUtils`（100行新增） | URL 合法性校验、黑名单过滤逻辑（域名/host 两维度） |
| `MonitorUtils`（189行新增） | CAT 监控上报工具封装，统一 `logMetricForValue`/`logEvent` 调用 |
| `StatMonitorController`（85行新增） | HTTP 接口：实时查询线程池状态、Redis 统计、健康检查 |
| `ScreenshotController`（122行新增） | 截图任务的 HTTP 测试接口（手动触发单次截图） |
| `S3Service`（+169行） | 新增截图专用上传方法：支持字节数组直传、自动生成S3路径 |
| `RedisService`（+31行） | 新增截图消息去重检查方法 |

#### ⑤ 数据模型

| 模型类 | 字段说明 |
|--------|---------|
| `ScreenshotCrawlResult` | `httpCode`、`phase1Status/ErrorReason`、`phase2Status/ErrorReason`、`htmlBytes`、`resourcesTsv`、`assetBytes`、`viewportResults`（多视口截图列表）、`startTime` |
| `ScreenshotRequest` | `url`、`domain`、`host`、`proxyType`、`timeout` |
| `ClickableElement` | `tag`、`text`、`bbox`、`href`、`aria_label`、`styles` |
| `ScreenshotResultMessage` | MQ 下游消息封装（GZip 压缩后 base64） |

### 2.3 配套修改

- `BaseRequest.java`：+18行，新增通用字段（`host`、`proxyType`、`timeout`）供 Screenshot/Sitemap 共用
- `SitemapUtil.java`：-79行，工具方法迁移到 `FilterUtils`，解耦 Sitemap 与通用过滤逻辑
- `strategy.html`：+25行，站点策略管理页面新增截图相关策略入口
- `mafka.properties`（prod+test）：新增 screenshot 的 Producer/Consumer 配置项

### 2.4 观测指标

| 指标 | CAT Key | 说明 |
|------|---------|------|
| 截图任务抓取耗时 | `screenshot.crawlScreenshot` | 整体耗时事务 |
| Phase1 HTML抓取成功率 | `screenshot.phase1` | Phase1 成功/失败 |
| Phase2 截图成功率 | `screenshot.phase2` | Phase2 成功/失败 |
| Playwright实例获取 | `screenshot.getInstance` | 实例重建频率 |
| 线程池活跃线程数 | `screenshot.threadpool.activeCount` | 每秒上报 |
| 线程池核心线程数 | `screenshot.threadpool.coreCount` | 每秒上报 |
| 黑名单过滤漏斗 | `screenshot.funnel.blacklist` | 被过滤比例 |
| URL格式错误 | `screenshot.funnel.errorUrl` | 非法URL丢弃量 |

---

## 三、PR #127 — Antibot 代理绑定 + 域名亲和性路由

**合并提交**：`9fe1811`
**PR 标题**：代理绑定，Redis流量统计及antibot域名亲和性路由
**合并人**：yangjiajun09
**包含子分支合并**：`dev/20260526-init-browser` → `dev/add-antibot-domain-affinity`（`8b88e66`）

### 3.1 背景与目标

Antibot 是使用 Headed Browser（真实有头浏览器）绕过反爬保护的下载器。原有调度方式存在两个问题：
1. **代理不区分类型**：Antibot 机器调用时使用通用代理池，无法针对性配置专用线路
2. **域名分发不均**：同一域名的请求可能分散到所有 Antibot 机器，无法在单机上建立 session/cookie 状态，影响反爬效果

本次迭代通过**代理类型绑定** + **域名亲和性路由（HRW算法）**解决以上问题，并引入 **Redis 实时流量统计**动态调整亲和子集大小。

### 3.2 核心改动详解

#### ① 域名亲和性路由（HRW 算法）

**文件**：`CrawlerRequestConsumer.java` - `getAntibotIp(domain)`

**算法流程：**
```
输入：domain（如 baidu.com）

1. 提取 topDomain（crawlercommons EffectiveTldFinder）
2. Redis 统计：incrAntibotTraffic(topDomain)（时间桶计数）
3. 计算子集大小 K = computeAntibotSubsetSize(topDomain, n)
4. HRW（Highest Random Weight）选 Top-K 子集：
   for each antibotIp in cluster:
       weight = murmur3_128(topDomain + "\0" + antibotIp)
   取 weight 最大的 K 台机器作为候选子集
5. 子集内随机选 1 台返回
```

**子集大小动态计算（`computeAntibotSubsetSize`）：**

```
K = ceil(domainTraffic / totalTraffic × n × alpha)

参数说明：
  domainTraffic = Redis 近 N 个时间桶内该域名请求数
  totalTraffic  = Redis 近 N 个时间桶内所有域名总请求数
  n             = Antibot 集群总机器数
  alpha         = antibotAlpha（Lion 配置，默认 5.0，PR commit daf943c 从 1.0 调整为 5.0）

边界处理：
  K = max(3, min(n, K))   // 至少 3 台，至多全集群
  total <= 0 时：退化为 ceil(sqrt(n))
```

**设计意图：**
- **高流量域名**（如 baidu.com）：domainTraffic/totalTraffic 占比大 → K 较大 → 候选子集多，分散负载
- **低流量域名**：占比小 → K 较小 → 固定落在少数几台机器 → 建立持久 session，提升反爬通过率
- **alpha 参数**：控制子集大小的放大系数，从 1.0 调整为 5.0（`daf943c`），使低流量域名也能分配到足够机器数

#### ② Redis 流量统计（时间桶设计）

**文件**：`RedisService.java` - 新增 antibot 流量统计方法（+76行）

**存储设计：**
```
Category: antibot_traffic

Key 格式（域名维度）：{topDomain}:d:{bucketId}
Key 格式（全局总量）：{antibotTrafficTotalHashTag}:{bucketId}

bucketId = System.currentTimeMillis() / 1000 / ANTIBOT_TRAFFIC_BUCKET_SECS
TTL = ANTIBOT_TRAFFIC_TTL_SECS

查询时：multiGet 近 ANTIBOT_TRAFFIC_BUCKETS 个桶，求和
```

**方法清单：**

| 方法 | 作用 |
|------|------|
| `incrAntibotTraffic(topDomain)` | 当前时间桶 +1（域名计数 + 总量计数） |
| `getAntibotDomainTraffic(topDomain)` | 近 N 桶域名流量求和 |
| `getAntibotTotalTraffic()` | 近 N 桶全局流量求和 |

**关键点**：使用相同 hashTag（`{topDomain}` 和 `{antibotTrafficTotalHashTag}`）保证 Redis Cluster 下域名计数与总量计数落在同一 slot，多个 key 的 `multiGet` 不会跨 slot。

#### ③ Antibot 专用代理绑定（`AntibotProxyConfigProvider`）

**文件**：`AntibotProxyConfigProvider.java`（新增，53行）

**设计思路：**
- Lion 配置 `antibot_proxy_config`：`Map<proxyType, List<ProxyItem>>`，每种线路对应一批专用代理
- 选取规则：`murmur3_128(antibotIp)` hash 后取模，同一 Antibot 机器稳定映射到同一代理
- 调用时机：`AntibotDownloader.download()` 中，`AntibotProxyConfigProvider.pick(proxyType, antibotIp)` 优先使用专用代理，未命中则 fallback 到通用代理池

**代理类型绑定流程：**
```
消费者选路：
  CrawlerRequestConsumer.handleSpiderMsg()
    → isAntibotGray()   // 判断是否命中 Antibot 灰度
    → getAntibotIp(domain)  // HRW 选定 antibotIp
    → crawlerService.getProxyItemModel(request, 0)  // 预计算 proxyType 写回 request
    → crawlerService.download(request, retryNum)

执行下载：
  AntibotDownloader.download()
    → AntibotProxyConfigProvider.pick(request.proxyType, antibotIp)
    → 命中专用代理 → 使用专用代理
    → 未命中 → fallback 通用代理
    → HTTP POST http://{antibotIp}:8000/api/download
```

#### ④ AntibotDownloader 优化

**文件**：`AntibotDownloader.java`（-27/+27行调整）

- 新增 `AntibotProxyConfigProvider.pick()` 调用，实现代理覆盖逻辑
- 专用 `ANTIBOT_HTTP_CLIENT`（静态单例，HTTP Timeout 120s，connectTimeout 10s）
- 处理 429 限流响应（`antibotResponse.code == 429`）→ 返回特定错误码，CAT 上报 `antibot.rate.limited`

#### ⑤ 新增测试类

**文件**：`AntibotDispatchTest.java`（163行）

单元测试覆盖：
- `computeAntibotSubsetSize` 在不同流量比例下的 K 值计算
- HRW 选点的确定性（同 domain 多次调用结果一致）
- alpha 参数边界（null/0/负数退化到默认值）

### 3.3 配置变更

| Lion 配置 Key | 类型 | 说明 |
|-------------|------|------|
| `antibot_alpha` | Double | HRW 子集放大系数，默认 5.0（原 1.0，commit daf943c 调整） |
| `antibot_proxy_config` | `Map<String, List<ProxyItem>>` | Antibot 专用代理配置，按 proxyType 分组 |
| `antibot_gray_ratio` | `Map<String, Double>` | 各 proxyType 的 Antibot 灰度比例 |
| `antibot_cluster_ip` | `List<String>` | Antibot 机器 IP 列表 |

### 3.4 观测指标

| 指标 | CAT Key | 说明 |
|------|---------|------|
| Antibot 下载总耗时 | `antibot.download` | 整体事务 |
| Antibot API 调用 | `antibot.api.call` | 对 Antibot HTTP API 的请求 |
| Antibot 下载成功 | `antibot.download.success` | 成功路径，带耗时 |
| Antibot 灰度命中 | `antibot.gray.hit` | 进入 Antibot 路径的请求量 |
| Antibot 被限流 | `antibot.rate.limited` | 429 响应计数 |
| Redis 流量统计写入 | 日志 `redis中自增统计` | Antibot 流量 incrBy 操作 |

---

## 四、优化效果总结

### PR #125 — 截图能力建设

| 维度 | 效果 |
|------|------|
| 新增能力 | 从"纯链接抓取"扩展到"视觉内容采集"，支持截图+JS渲染+DOM元素提取 |
| 实例隔离 | 截图实例池与 Sitemap 实例池完全独立，互不干扰，避免资源竞争 |
| UA/时区绑定 | 代理线路与 UA/时区一一绑定，降低反爬检测风险 |
| 参数可配 | 视口/DPR/截图数量/位置偏重全部概率配置化，无需重启热更新 |
| 线程池动态化 | 截图线程数支持 Lion 热更新，线程池状态实时上报 CAT |

### PR #127 — Antibot 域名亲和性路由

| 维度 | 优化前 | 优化后 |
|------|--------|--------|
| 域名分发 | 随机分配，同域名散落各机器 | HRW 固定映射，低流量域名集中在少数机器 |
| 代理匹配 | 使用通用代理池 | 按 proxyType + antibotIp hash 使用专用代理 |
| 子集大小 | 固定 ceil(sqrt(n)) | 根据实时流量比例动态计算（alpha=5.0 放大系数） |
| session 保持 | 无法保持（机器随机变化） | 同域名稳定落在 K 台候选机器，有利于 session/cookie 积累 |
| 代理稳定性 | 同一 antibotIp 每次随机代理 | murmur3 hash 稳定绑定，减少代理切换 |

---

## 五、涉及文件清单

### PR #125 新增/修改文件

| 文件路径 | 变更类型 | 行数变化 |
|---------|---------|---------|
| `service/ScreenshotPlaywrightService.java` | **新增** | +1041 |
| `consumer/ScreenshotRequestConsumer.java` | **新增** | +310 |
| `screenshot/ScreenshotService.java` | **新增** | +256 |
| `screenshot/ScreenshotPublishService.java` | **新增** | +137 |
| `controller/ScreenshotController.java` | **新增** | +122 |
| `controller/StatMonitorController.java` | **新增** | +85 |
| `screenshot/model/ScreenshotCrawlResult.java` | **新增** | +95 |
| `screenshot/model/ClickableElement.java` | **新增** | +65 |
| `screenshot/model/ScreenshotResultMessage.java` | **新增** | +72 |
| `screenshot/model/ScreenshotRequest.java` | **新增** | +30 |
| `util/FilterUtils.java` | **新增** | +100 |
| `util/MonitorUtils.java` | **新增** | +189 |
| `service/S3Service.java` | **修改** | +169 |
| `service/RedisService.java` | **修改** | +31 |
| `request/BaseRequest.java` | **修改** | +18 |
| `service/SiteStrategyService.java` | **修改** | +18 |
| `service/CrawlerService.java` | **修改** | +42 |
| `service/PlaywrightService.java` | **修改** | +11 |
| `util/SitemapUtil.java` | **修改（删减）** | -79 |
| `sitemap/SitemapService.java` | **修改** | +2/-1 |
| `profiles/prod/mafka.properties` | **修改** | +22/-1 |
| `profiles/test/mafka.properties` | **修改** | +22/-1 |
| `static/strategy.html` | **修改** | +25 |

### PR #127 新增/修改文件

| 文件路径 | 变更类型 | 行数变化 |
|---------|---------|---------|
| `service/proxy/AntibotProxyConfigProvider.java` | **新增** | +53 |
| `service/AntibotDispatchTest.java`（测试） | **新增** | +163 |
| `consumer/CrawlerRequestConsumer.java` | **修改** | +96 |
| `downloader/AntibotDownloader.java` | **修改** | +27/-27 |
| `service/RedisService.java` | **修改** | +76 |
| `controller/CrawlerController.java` | **修改** | +60 |
