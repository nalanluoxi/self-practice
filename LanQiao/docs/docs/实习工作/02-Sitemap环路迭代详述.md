# Sitemap 环路迭代详述

> 本文档按时间顺序完整记录 Sitemap 站点发现环路的每一次迭代，包含场景、技术方案、代码关键点和量化收益。

---

## 一、系统背景

Sitemap 环路是大模型语料抓取系统中负责**站点新链接发现**的核心子系统。其闭环流程如下：

```
llm_host_robots_url（Hive 表）
      ↓ RobotsMsgSendingJob（每日 Spark 任务）
Topic: llm_sitemap_url（Mafka）
      ↓ SitemapRequestConsumer（在线消费）
robots.txt 下载 → 提取 sitemapUrl → 回环投递 MQ
      ↓
sitemap.xml 下载 → 提取 pageUrl → 发送 sitemapResultTopic
      ↓
下游链接库 → 调度 → 抓取 → 语料
```

---

## 二、迭代一：流量打散与三级精准限流体系（2025.11.10）

### 问题场景

原系统无流量控制，Sitemap 环路处于"无序竞争"状态：大站点凭借海量 sitemap 链接占用大部分 Mafka 带宽，严重挤兑小站点资源，小站任务长期积压、挖掘时效极差。调度集中在凌晨 3 点和下午 3 点，流量不均匀。

### 技术方案

在 `SitemapRequestConsumer` 消息处理链路中，从 0 到 1 构建三级限流防护网：

**① 集群层**：接入 Rhino OneLimiter
```
入口：sitemapService.isClusterQpsLimited()
配置：sitemap_cluster_qps_limit（MDP 动态配置）
作用：集群维度统一管控总请求速率
```

**② 单机层**：单机上限兜底
```
入口：sitemapService.isSingleNodeQpsLimited()
配置：sitemap_limit_single_node
作用：防止单节点过载
```

**③ Redis 精确层**：站点级精确限频
```
入口：sitemapService.isSiteQpsLimited(request)
实现：EffectiveTldFinder.getAssignedDomain() 提取顶级域 → Redis Lua 令牌桶
配置：sitemap_limit
作用：站点级别的精确调度，大站受限、小站保障
```

**关键优化**：将黑名单过滤（`blackDomainSet`）、URL 格式校验（`isValidUrl`）、后缀黑名单（`urlBlackListSuffixes`）、Redis 去重前置于限流入口之前——确保有效限流配额不被黑名单/重复请求消耗。

### 量化收益

- 调度分配从集中于凌晨 3 点和下午 3 点 → **全天均匀产出**
- 集群 QPS 6~8（吞吐量较低，为后续迭代打基础）

---

## 三、迭代二：多线程重构与 Mafka 集群扩容（2025.11.17）

### 问题场景

初始系统单线程消费模式，单次任务同步处理耗时约 15s，集群 QPS 低于 10。面对百万级站点发送量，系统吞吐量严重触顶。

### 技术方案

**并发架构重写**：
```java
// SitemapRequestConsumer.java 核心线程池
EXECUTOR = new ThreadPoolExecutor(
    threadNumValue, threadNumValue,       // core = max，通过 sitemap_thread_num 动态配置
    0L, TimeUnit.MILLISECONDS,
    new SynchronousQueue<>()              // 无缓冲队列，线程满即阻塞调用方
);
```

- `receive()` 方法只做反序列化与前置过滤，通过 `addRequestToThreadPool()` 将 sitemap 下载任务异步提交线程池，实现消息拉取与业务处理完全解耦
- 线程池满时 `synchronized + 自旋等待`（每次 sleep 1s，最长等待 36s），超时后通过 `sitemapPublishService.sendRequest()` 重投 MQ，消息不丢失
- 线程数通过 MDP 配置 `sitemap_thread_num` 热更新，支持不停机调整并发度

**Mafka 集群扩容**：分区数从 200 → 400，消除传输链路物理瓶颈。

### 量化收益

| 指标 | 优化前 | 优化后 |
|:--|:--:|:--:|
| 小时抓取量 | 2.5 万 | 96 万 |
| 峰值 QPS | <10 | 400 |
| 每分钟集群抓取量 | 400~1500 | 24,000 |
| 日均新链发现量 | 2~3 亿 | 10~14 亿（峰值 75 亿） |
| 提升倍数 | — | **16x** |

---

## 四、迭代三：动态自适应本地限流优化（2025.11.27）

### 问题场景

三级限流建立后，前两层（集群+单机）为比例过滤，大站流量占比依然较高；固定阈值无法感知下游处理能力（线程池负载）的实时变化，线程池无法打满，并发能力浪费。

### 技术方案

在 `LimiteUtils` 中引入**基于时间窗口的站点级本地限流器**（`CounterRateLimiter`，`ConcurrentSkipListMap` 实现滑动窗口计数）。

**自适应反馈机制**：
```
setup() 方法启动后台探测守护线程，每秒采样：
  activeCount / corePoolSize → 与 80% 水位阈值对比
  结果写入长度为 3 的历史队列

联动逻辑（LimiteUtils.updateAdaptiveLimitQuota）：
  连续 3 次 activeCount < 80% corePoolSize → LIMITE_COUNT++（放宽配额）
  连续 3 次 activeCount > 80% corePoolSize → LIMITE_COUNT--（收紧配额）
  边界由 sitemap_limite_count_max 约束
```

建立"**线程池健康度–限流阈值**"动态联动模型，实现精细化自适应限频。

### 量化收益

- 英文留存量：2000 万 → **1.2 亿**
- 1000 万站点全量发现任务处理时长：**3~4 小时**
- 峰值集群 QPS：**1417+**

---

## 五、迭代四：GZIP 压缩包资源解析能力补齐（2025.12.17）

### 问题场景

原处理链路存在功能盲区：所有以 `.gz` 格式存放的 Sitemap 信息被直接视为无效并丢弃，大量高质量新链资源流失。

### 技术方案

在 `SitemapRequestConsumer.addRequestToThreadPool()` 的任务提交逻辑中：

```java
// URL 后缀嗅探（排除 tar.gz）
CompressedUtil.isValid(request)  // url.getPath().endsWith(".gz") && !endsWith(".tar.gz")

// GZIP 流式解压（SitemapDownloader 内部）
CompressedUtil.getCompressedData()
  → new GZIPInputStream(new ByteArrayInputStream(rawBytes))
  → 1024 byte buffer 分块读取 → ByteArrayOutputStream
  → 按 UTF-8 编码输出字符串 → 交给 SiteMapParser 正常解析
```

完整保留原有 Redis 去重、限流、CAT 监控等处理链路，最小化改动面。

> 设计考量：通过日志打点统计，环路中压缩包类型主要是 `.gz`，其他类型（`bz2/zip/7z`）每小时不超过 10 条，收益不明显；预留策略模式+工厂方法扩展口但未实现，保持代码整洁。

### 量化收益

| 指标 | 数据 |
|:--|:--:|
| 日均处理压缩包量 | 400 万+ |
| 压缩包占总流量比 | 4.08%（当日线程池 1 亿条，压缩包 400 万） |
| 压缩包贡献新链占比 | 4.08%~16.23% |
| 新链发现量变化 | 3.79 亿 → **52.6 亿**（**14x**） |

---

## 六、迭代五：频度调度 v1（2026.02.04）

### 问题场景

早期方案每天固定调度全量 robots.txt 进行挖掘，随着 robots 数量增加：
- 高频更新站点调度不足，错过最新链接
- 低频更新站点浪费大量资源
- 无差异化投递策略

### 技术方案

**抓取侧差异化 Redis TTL**（`SitemapService.resolveExpireTimeByGrade()`）：
```java
grade=1（HIGH）  → Redis TTL 3h  → 每 3h 可重新抓取
grade=2（MIDDLE）→ Redis TTL 5h  → 每 5h 可重新抓取
低频/无等级      → 默认配置值
```

**上游透传 ext 字段**（`SitemapPublishService`）：
```json
{
  "batchId": "batch-freq-list",
  "dispatchTs": "1706400000000",
  "grade": "1"
}
```

**问题与修复**：上线数日后发现投递效果不佳，排查发现打分机制过于严苛，发送量少导致新链产出大幅下降。后续调整：放宽评分阈值 + 扩充原始站点集合，产出恢复正常。

---

## 七、迭代六：策略拦截（DSL 规则引擎）（2026.03.19）

### 问题场景

人工评估抓取结果发现大量垃圾/作弊站点混入链路，严重影响新链质量，需建立可实时更新的规则引擎进行系统性拦截。

### 技术方案

```java
// @PostConstruct 阶段预编译正则
regularPatternList = new HashSet<Pattern>();
sitemap_regular_filter_list.forEach(regex ->
    regularPatternList.add(Pattern.compile(regex)));

// @MdpConfigListener 配置热更新，无需重启
// preFilterProcess() 中前置拦截（在 Redis 去重和限流之前）
SitemapUtil.isRegularFilterDomain(domain, regularPatternList)
```

- 正则预编译缓存复用，避免重复编译 NFA/DFA 开销
- 拦截逻辑前置于 Redis 去重和限流判断，避免作弊站点消耗有效资源配额
- 热更新延时：**秒级**（无需发布部署）

### 量化收益

- 有效拦截垃圾站点：**90%**
- 日均拦截量：**4~5 亿**
- 规则生效延时：分钟级 → **秒级**
- 人工抽样高质量站点率（1+2 分）：**4.1% → 11.9%**

---

## 八、迭代七：无效站点治理与积压消息处理（2026.04）

### 问题场景

监控体系建设后发现：
- 每天历史积压 **1500 万** 条
- 一些批次中大量无效站点：默认页面占 27%~34%，抓取失败占 11%~17%，robots 无 sitemap 占 25%~27%

### 技术方案

**时效性过滤**（`SitemapRequestConsumer.preFilterProcess()`）：
```java
// 从 ext 字段提取 dispatchTs，与 staleThresholdHours 比对
// 超过时效窗口（默认 48h）的历史积压消息直接丢弃
// 标记为 sitemap.funnel.stale（CAT 打点）
// Lion 配置：sitemap_stale_threshold_hours
```

**失败原因细分记录**（`SitemapService.saveFailRobotsTxt()`）：
- `download_fail`、`abnormal_content`、`cf_blocked_1005/1020`、`captcha_blocked`
- 持久化到 `robots_txt` 表的 `ext` 字段，为无效站点识别提供数据基础

**过期时间策略**：设置合理过期时间而非永久封禁——避免一次失败导致站点被彻底屏蔽，在拦截效果与站点覆盖率之间取得平衡。

---

## 九、迭代八：Sitemap 环路监控体系建设（2026.03）

### 问题场景

缺乏对每批次抓取数据的精细化监控，无法及时发现消息积压、高失败率、大量无效站点问题。初期尝试接入内部 Raptor 日志监控平台，因监控指标粒度过细、触达 type 数量上限而不可行。

### 技术方案

采用 **Redis 统计方案**，以小时为粒度：

- 在 `SitemapService.downLoadRobotsTxt()` 和 `parseSitemapAndSend()` 等核心节点调用 `redisService.recordTraffic()`
- 按 `batchId + batchHour` 维度精确累计每批次抓取量与新链发现量

**Redis Key 体系**：
```
sitemap_funnel_robots_suc       ← robots 下载成功数
sitemap_funnel_url_found        ← 发现 URL 数
sitemap_funnel_sitemapIndex_found ← SiteMapIndex 数量
sitemap.funnel.stale            ← 过期消息数（CAT）
sitemap.funnel.*                ← 系列 CAT 漏斗事件
```

---

## 十、迭代九：频度调度 v2（三维打分模型）（2026.04）

### 问题场景

v1 维度单一，仅依赖新链接产出分；未综合考量站点规模与留存质量，调度决策不够精准。

### 技术方案（UpdateFrequencyTableJob）

**三维打分模型**：
```
compositeScore = 0.5 × newUrlScore + 0.5 × fuzzyScore
fuzzyScore = 0.5 × rateScore + 0.5 × scaleScore
```

**打分来源**：
| 维度 | 来源 | 含义 |
|:--|:--|:--|
| `newUrlScore` | `sitemap_link_bloomfilter_daily_new_url`（Bloom 去重新链接表） | 昨日新链接产出效果 |
| `rateScore` | `llm_crawler_domain_save_info`（域名留存统计） | 留存率（留存量/抓取数） |
| `scaleScore` | `llm_crawler_domain_save_info` | 站点规模（log 压缩） |

**等级映射**：
```
compositeScore ≥ 0.75 → grade 1 (HIGH,   interval=6，每天 6 次)
compositeScore ≥ 0.50 → grade 2 (MIDDLE, interval=4，每天 4 次)
compositeScore ≥ 0.25 → grade 3 (LOW,    interval=2，每天 2 次)
rate ≥ 0.01           → grade 4 (DISUSE, interval=1，每天 1 次)
else                  → grade 5 (PENDING，不投递)
```

**降级保护**：每次最多降 1 级，防止因数据波动导致等级快速跌落。

**多轮投递**：`FrequencyDeliverySitemapJob` 每天运行多次（传递不同 `grades` 参数），实现差异化的一天多轮投递。

**闭环反馈**：
```
抓取结果 → UpdateFrequencyTableJob（T+1 打分）→ DF/DI 表
                          ↓
              FrequencyDeliverySitemapJob 读 DF 表投递
                          ↓
              SitemapService 根据 grade 设置 Redis TTL
                          ↓
              下一次抓取结果反馈回评分系统
```

### 量化收益（2026-05-10 ~ 2026-05-15 实测）

| 指标 | 数据 |
|:--|:--:|
| 日均多批次命中 sitemap | **~413 万** |
| 峰值（2026-05-14） | **493 万** |
| 日均额外贡献 URL | **~908 万** |
| 单日峰值 URL 贡献 | **1,139 万** |
| 整体日抓取量提升 | 10~70 亿 → 100~200 亿（**~10x**） |
| 稳定运行状态 | 连续 6 天在 350~500 万区间 |

---

## 十一、数据质量验证（质检报告）

### 质检时间线

| 质检日期 | 核心发现 | 后续动作 |
|:--|:--|:--|
| 2026-03-03 | 首次质检，发现大量作弊站点 | 制定拦截措施 |
| 2026-03-06 | 二次质检，验证作弊站点打压效果 | 调整抽样策略 |
| 2026-03-20 | 评估策略拦截上线后链接质量 | 进一步优化拦截规则 |
| 2026-04-07 | 扩充投递站点集合后质量评估 | 建立定期质检观测体系 |

### 漏斗数据摸底（2026-04-27，767w 集合）

```
投递 767w
  → 进入线程池 robots 请求 646w (84%)
    → robots 抓取成功 530w (82%)
      → 有 sitemap 的 robots ~320w
        → 进入线程池 sitemap 请求 3545w
          → sitemap 下载成功 ~2618w
            → 有 url 的 sitemap 2700w
              → 发现 url 2.73 亿
```

**关键漏损点**：
- robots 无 sitemap：25%~27%（约 300w+ robots 白抓）
- 数据挤压：当天未处理 1482 万（sitemap+robots 合计）
