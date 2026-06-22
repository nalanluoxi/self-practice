# Antibot 域名亲和性路由设计与实现

> PR #127 — `dev/add-antibot-domain-affinity` 合并到 release（2026-06-02）
> 核心目标：让同一个域名的请求尽量打到同几台 Antibot 机器上，提升缓存命中、规避行为识别。

---

## 一、背景与问题

原有逻辑：每次分配 Antibot 机器时，从集群 IP 列表中**完全随机**选一台：

```java
// 改造前
public String getAntibotIp() {
    List<String> tempIps = new ArrayList<>(antibotClusterIp);
    return tempIps.get((int) (Math.random() * tempIps.size()));
}
```

问题：
- 同一个域名每次打到不同机器，Antibot 无法利用本地 Session/Cookie 缓存
- 流量大的域名和流量小的域名，分配到的机器数完全一样，没有按量分配

---

## 二、整体设计

改造分三层，每层独立实现，可单独测试：

```
请求进来
    │
    ▼
① Redis 流量统计（incrAntibotTraffic）                   RedisService.java
    │ 写入当前域名 + 全局计数
    ▼
② HRW 子集计算（computeAntibotSubsetSize + getAntibotIp） CrawlerRequestConsumer.java:758~820
    │ 按域名流量占比动态决定 K（子集大小），用 murmur3_128 稳定选 K 台
    ▼
③ 代理分类型绑定（AntibotProxyConfigProvider.pick）       AntibotDownloader.java:93
    │ 按 proxyType + antibotIp hash 选代理，同 antibotIp 同 type 永远选同一个代理
    ▼
AntibotDownloader 下载
```

**完整调用链（含行号）：**

```
CrawlerRequestConsumer.java:252   // isAntibotGray 分支入口（MQ消费）
    │
    ├─ :255  buildAntibotExt(domain)
    │           └─ :824  getAntibotIp(domain)
    │                       ├─ :788  redisService.incrAntibotTraffic()   ← ① 写流量计数
    │                       ├─ :796  computeAntibotSubsetSize()           ← ② 算 K
    │                       └─ :800-818  HRW top-K → 随机1台              ← ② 选机器
    │
    ├─ :259  crawlerService.getProxyItemModel()  // 提前算好 proxyType 写回 request
    │
    └─ :267  crawlerService.download(request)
                └── AntibotDownloader.java:71  download()
                        ├─ :82   parseAntibotIp(ext)                     // 读出 antibotIp
                        ├─ :93   AntibotProxyConfigProvider.pick()       ← ③ 代理绑定
                        └─ :99   buildAntibotRequest() → 发请求到 Antibot 机器

// HTTP 直调路径（CrawlerController.java）同步做了相同修改，行号：148、774
```

---

## 三、哪些请求会走 Antibot 流程

### 入口位置

`CrawlerRequestConsumer.java:252`，主流程按优先级依次判断走哪条下载链路：

```
收到 MQ 消息（CrawlerRequest）
    │
    ▼
isCamoufoxGray？  → CamoufoxDownloader
    │ 否
    ▼
isAntibotGray？   → AntibotDownloader   ← 这里
    │ 否
    ▼
isJsSpiderGray？  → JsSpiderDownloader
    │ 否
    ▼
普通 HTTP 下载
```

---

### isAntibotGray 判断逻辑（四级优先级）

```
① reqId 以 "antibot_test" 开头
   → 直接命中，走 Antibot（测试专用入口）

② antibotClusterIp 为空（Lion 没配集群 IP）
   → 不命中，跳过

③ Lion 配置 antibot_gray_ratio 里有 ALL_UNIT
   → 100% 全量灰度，全部走 Antibot

④ 按 domain / topDomain 查灰度比例
   → 优先查 Lion 配置，没有再查 Redis 配置
   → 查到比例后，随机数 < 比例 则命中
```

**举例：**

```
Lion 配置：
  antibot_gray_ratio = {
    "taobao.com": 1.0,   // 全量走 Antibot
    "jd.com":     0.5,   // 50% 走 Antibot
    "ALL_UNIT":   1.0    // 配了这个则所有请求全走 Antibot
  }

请求 domain=taobao.com → 随机数 < 1.0 → 必定命中
请求 domain=jd.com     → 随机数 < 0.5 → 50% 概率命中
请求 domain=weibo.com  → 没配 → 不命中，走普通下载
```

---

### 命中后做什么

```java
// CrawlerRequestConsumer.java:252~267
if (isAntibotGray(request)) {
    request.setReqId(reqId + "_ab");           // reqId 追加 _ab 后缀，便于日志区分
    request.setDownloader(ANTIBOT_DOWNLOADER); // 指定下载器类型
    request.setExt(buildAntibotExt(domain));   // HRW 选机器，写入 antibotIp
    request.setProxyType(proxyType);           // 提前算好代理类型写回
    // CAT 打点 antibot.gray.hit
}
```

---

### 两个入口（MQ + HTTP）

```
① MQ 消费入口（主链路）
   CrawlerRequestConsumer.java:252
   → 正常生产流量，按灰度比例路由

② HTTP 直调入口（测试/手动触发）
   POST /antibot/mock
   → 直接绑定 AntibotDownloader，不走灰度判断
   → 使用海外代理（proxyWay=5），不发 Mafka（isSendMafka=-1）
   → 供手动测试 Antibot 链路使用
```

---

### 完整判断流程图

```
CrawlerRequest 进来
    │
    ├─ reqId 以 "antibot_test" 开头？ → YES → 直接走 Antibot
    │
    ├─ antibotClusterIp 为空？ → YES → 跳过，走后续链路
    │
    ├─ Lion 配置有 ALL_UNIT？ → YES → 全量走 Antibot
    │
    ├─ Lion/Redis 有该 domain 的灰度比例？
    │       └─ random() < 比例？ → YES → 走 Antibot
    │
    └─ 以上都不满足 → 不走 Antibot
```

---

## 四、Redis 滑动窗口流量统计（isAntibotGray 命中后）

**文件：** `RedisService.java`

### 设计

用**时间分桶**实现滑动窗口，避免单 Key 写热点：

```
bucket = currentTimeMillis / 1000 / 60   （每 60 秒一个桶）
域名 Key：{topDomain}:d:{bucket}
全局 Key：{antibot_total}:{bucket}
TTL = 60 * (5 + 1) = 360 秒              （保留 5 个桶 + 1 个缓冲）
```

查询时把过去 5 个桶的计数加总，得到近 5 分钟的流量。

### 核心代码

```java
// 常量定义
public static final String ANTIBOT_TRAFFIC_CATEGORY = "antibot_traffic";
public static final String ANTIBOT_TRAFFIC_TOTAL_HASH_TAG = "antibot_total";
public static final int ANTIBOT_TRAFFIC_BUCKET_SECS = 60;
public static final int ANTIBOT_TRAFFIC_BUCKETS = 5;
public static final int ANTIBOT_TRAFFIC_TTL_SECS =
        ANTIBOT_TRAFFIC_BUCKET_SECS * (ANTIBOT_TRAFFIC_BUCKETS + 1);

// 每次分配 Antibot 时先打计数
public void incrAntibotTraffic(String topDomain) {
    if (StringUtils.isBlank(topDomain)) return;
    long bucket = antibotCurrentBucket();
    try {
        redisStoreClient.incrBy(new StoreKey(ANTIBOT_TRAFFIC_CATEGORY,
                antibotDomainField(bucket, topDomain)), 1, ANTIBOT_TRAFFIC_TTL_SECS, 0);
        redisStoreClient.incrBy(new StoreKey(ANTIBOT_TRAFFIC_CATEGORY,
                antibotTotalField(bucket)), 1, ANTIBOT_TRAFFIC_TTL_SECS, 0);
    } catch (Exception e) {
        log.warn("[antibot-traffic] incr error domain={}", topDomain, e);
    }
}

// 读取近 5 分钟某域名流量
public long getAntibotDomainTraffic(String topDomain) {
    if (StringUtils.isBlank(topDomain)) return 0;
    long now = antibotCurrentBucket();
    List<StoreKey> keys = new ArrayList<>(ANTIBOT_TRAFFIC_BUCKETS);
    for (int i = 0; i < ANTIBOT_TRAFFIC_BUCKETS; i++) {
        keys.add(new StoreKey(ANTIBOT_TRAFFIC_CATEGORY, antibotDomainField(now - i, topDomain)));
    }
    return sumAntibotBuckets(keys, "domain=" + topDomain);
}

// 读取近 5 分钟全局流量
public long getAntibotTotalTraffic() {
    long now = antibotCurrentBucket();
    List<StoreKey> keys = new ArrayList<>(ANTIBOT_TRAFFIC_BUCKETS);
    for (int i = 0; i < ANTIBOT_TRAFFIC_BUCKETS; i++) {
        keys.add(new StoreKey(ANTIBOT_TRAFFIC_CATEGORY, antibotTotalField(now - i)));
    }
    return sumAntibotBuckets(keys, "total");
}
```

---

## 五、HRW 子集选取（核心算法）

**文件：** `CrawlerRequestConsumer.java:758~820`

### 4.0 什么是 HRW，为什么用它

**HRW（Highest Random Weight，最高随机权重）** 是一种一致性哈希算法。

**目的：** 给定一个域名，从 N 台 Antibot 机器里，每次都稳定地选出**同样的 K 台**，不会因为调用时机不同而变化。

**为什么不直接用取模（hash(domain) % N）：**
- 取模只能选 1 台，但我们需要一个"子集"（K 台）
- 取模在机器列表增删时，大量域名的分配结果会跳变，造成大面积重路由

**HRW 的做法：**

```
对集群中每一台机器，计算  w = hash(domain, ip)
取 w 最大的 K 台，作为这个域名的"绑定子集"
子集内再随机选 1 台实际使用
```

**效果举例**（假设有 10 台机器，K=3）：

```
domain = "taobao.com"
  → 每次都选出同样的 3 台：[ip_3, ip_7, ip_9]
  → 在这 3 台里随机 1 台发请求

domain = "jd.com"
  → 每次都选出同样的 3 台：[ip_1, ip_4, ip_6]
```

即使机器列表新增/删除 1 台，只有少数域名的子集发生变化，其余稳定不变——这是 HRW 的**一致性特性**，比取模平滑得多。

**对比普通随机：**

| | 普通随机 | HRW 子集 |
|--|---------|---------|
| 同域名是否固定落同几台机器 | ✗ 完全随机 | ✓ 固定子集 |
| 节点增删影响范围 | 全部域名 | 仅边界域名 |
| 能否控制子集大小 K | ✗ | ✓ 动态调整 |

---

### 4.1 动态 K 计算

```java
private int computeAntibotSubsetSize(String topDomain, int n) {
    // 默认 K = sqrt(n)，最小 3，最大 n
    int defaultK = Math.min(n, Math.max(3, (int) Math.ceil(Math.sqrt(n))));
    if (StringUtils.isBlank(topDomain) || redisService == null) {
        return defaultK;
    }
    try {
        long total = redisService.getAntibotTotalTraffic();
        if (total <= 0) return defaultK;
        long domainReq = redisService.getAntibotDomainTraffic(topDomain);
        if (domainReq <= 0) return Math.max(3, Math.min(n, defaultK));

        double alpha = (antibotAlpha == null || antibotAlpha <= 0) ? 5.0 : antibotAlpha;
        double ratio = (double) domainReq / total;       // 域名流量占比
        int k = (int) Math.ceil(ratio * n * alpha);      // 流量越大，分到的 K 越大
        return Math.max(3, Math.min(n, k));
    } catch (Exception e) {
        log.warn("[antibot] computeAntibotSubsetSize fallback domain={}", topDomain, e);
        return defaultK;
    }
}
```

**K 公式：** `k = ceil(ratio × n × alpha)`
- `ratio`：该域名流量 / 全局流量
- `n`：集群机器总数
- `alpha`：放大系数，Lion 配置（默认 5.0，从 1.0 调优而来）
- 边界保护：`max(3, min(n, k))`，至少 3 台，最多全部

**含义：** 流量越大的域名，分到越多的 Antibot 机器（扩散负载）；流量极小的域名默认最少 3 台（保证可用）。

### 4.2 HRW（Highest Random Weight）选 K 台

```java
public String getAntibotIp(String domain) {
    if (antibotClusterIp == null || antibotClusterIp.isEmpty()) return null;

    String topDomain = (domain == null) ? null : UrlUtils.getTopDomain(domain);

    // 打流量计数
    if (redisService != null && StringUtils.isNotBlank(topDomain)) {
        try {
            redisService.incrAntibotTraffic(topDomain);
        } catch (Exception e) {
            log.warn("[antibot] incr traffic fail domain={}", topDomain, e);
        }
    }

    List<String> ips = new ArrayList<>(antibotClusterIp);
    int n = ips.size();
    int K = computeAntibotSubsetSize(topDomain, n);

    // hashKey 用 topDomain（相同根域名的子域名分到同一子集）
    String hashKey = (topDomain != null && !topDomain.isEmpty()) ? topDomain
            : (domain != null ? domain : "");

    // HRW：对每台机器计算 hash(domain, ip)，取 top-K 作为子集
    PriorityQueue<long[]> heap = new PriorityQueue<>(K, Comparator.comparingLong(a -> a[0]));
    for (int i = 0; i < n; i++) {
        long w = Hashing.murmur3_128()
                .newHasher()
                .putString(hashKey, StandardCharsets.UTF_8)
                .putByte((byte) 0)
                .putString(ips.get(i), StandardCharsets.UTF_8)
                .hash().asLong();
        if (heap.size() < K) {
            heap.offer(new long[]{w, i});
        } else if (w > heap.peek()[0]) {
            heap.poll();
            heap.offer(new long[]{w, i});
        }
    }

    // 子集内随机选 1 台
    int[] subset = new int[heap.size()];
    int p = 0;
    for (long[] entry : heap) subset[p++] = (int) entry[1];
    int pick = subset[ThreadLocalRandom.current().nextInt(subset.length)];
    return ips.get(pick);
}
```

**HRW 算法要点：**
- 对集群中每台机器计算 `hash(topDomain + ip)`
- 取哈希值最大的 K 台作为子集
- 子集内随机选 1 台
- 相同域名每次计算出的子集**完全相同**（确定性），保证亲和性
- 节点增删时只有边界附近的域名子集变化，其他稳定（一致性哈希特性）

---

## 六、代理分类型绑定

**文件：** `AntibotProxyConfigProvider.java`（新增）

### 设计

同一台 Antibot 机器 + 同一种代理类型，总是使用同一个代理 IP。防止 Antibot 机器频繁切换出口 IP 被识别。

### 代理类型分类

通过 `getProxyItemModel` 提前判断域名应使用的代理类型：
- `mainland_normal`：国内普通代理
- `oversea_normal`：海外普通代理
- `oversea_advanced_line`：海外专线代理

### 核心代码

```java
@Service
public class AntibotProxyConfigProvider {

    // Lion 配置格式：{"mainland_normal": [{"host":"x.x.x.x","port":1234,...},...], ...}
    @MdpConfig(key = "antibot_proxy_config")
    private HashMap<String, ArrayList<ProxyItem>> antibotProxyConfig;

    /**
     * 按 proxyType 取 list，按 antibotIp hash 选一个 ProxyItem。
     * 任一参数空 / Lion 没配 / type 不匹配 → 返 null，调用方走 fallback 用入参 proxyItem。
     */
    public static ProxyItem pick(String proxyType, String antibotIp) {
        AntibotProxyConfigProvider self = INSTANCE;
        if (self == null
                || StringUtils.isBlank(proxyType)
                || StringUtils.isBlank(antibotIp)
                || self.antibotProxyConfig == null) {
            return null;
        }
        ArrayList<ProxyItem> list = self.antibotProxyConfig.get(proxyType);
        if (list == null || list.isEmpty()) return null;

        // 同 antibotIp 永远 hash 到同一个代理
        long h = Hashing.murmur3_128()
                .hashString(antibotIp, StandardCharsets.UTF_8).asLong();
        int idx = (int) Math.floorMod(h, (long) list.size());
        return list.get(idx);
    }
}
```

---

## 七、调用链路整合

**文件：** `CrawlerRequestConsumer.java` + `CrawlerController.java`

改造前后对比：

```java
// ===== 改造前 =====
String antibotIp = getAntibotIp();   // 纯随机
request.setExt(JackSonUtils.toJson(Collections.singletonMap("antibot_ip", antibotIp)));

// ===== 改造后 =====
// 步骤 1：域名亲和选机器（含流量统计）
request.setExt(buildAntibotExt(request.getDomain()));

// 步骤 2：提前计算 proxyType，写回 request 供 AntibotDownloader 内部读
try {
    ProxyItemModel m = crawlerService.getProxyItemModel(request, 0);
    if (m != null && m.getProxyType() != null) {
        request.setProxyType(m.getProxyType().getWord());
    }
} catch (Exception e) {
    log.warn("antibot precompute proxyType err", e);
}
```

```java
// buildAntibotExt 封装
public String buildAntibotExt(String domain) {
    String antibotIp = getAntibotIp(domain);   // HRW 亲和选机器
    return JackSonUtils.toJson(Collections.singletonMap("antibot_ip", antibotIp));
}
```

**AntibotDownloader 代理覆盖逻辑：**

```java
// AntibotDownloader.java
// 先用通用逻辑选出 proxyItem，再尝试用 antibotProxyConfig 覆盖
ProxyItem antibotProxy = AntibotProxyConfigProvider.pick(crawlerRequest.getProxyType(), antibotIp);
if (antibotProxy != null) {
    proxyItem = antibotProxy;  // 命中则覆盖，未命中 fallback 用通用代理
}
```

---

## 八、数据流全景图

```
MQ/HTTP 请求
    │
    ▼
CrawlerRequestConsumer / CrawlerController
    │
    ├── 1. buildAntibotExt(domain)
    │       ├── incrAntibotTraffic(topDomain)     → Redis 写计数
    │       ├── getAntibotTotalTraffic()           → Redis 读全局
    │       ├── getAntibotDomainTraffic(domain)   → Redis 读域名
    │       ├── computeAntibotSubsetSize → K = ceil(ratio × n × alpha)
    │       └── HRW top-K → 子集内随机 → antibotIp 写入 ext
    │
    ├── 2. getProxyItemModel(request) → proxyType 写回 request
    │
    ▼
AntibotDownloader
    ├── parseAntibotIp(ext)
    ├── AntibotProxyConfigProvider.pick(proxyType, antibotIp)
    │       └── hash(antibotIp) → 固定选同一个代理 IP
    └── 发请求到 Antibot 机器
```

---

## 九、配置项（Lion）

| 配置 Key | 类型 | 默认值 | 说明 |
|---------|------|--------|------|
| `antibot_alpha` | Double | 5.0 | K 放大系数（从 1.0 调优到 5.0） |
| `antibot_proxy_config` | JSON Map | — | 各 proxyType 对应的代理列表 |
| `antibot_cluster_ip` | List | — | Antibot 集群机器 IP 列表 |

---

## 十、单元测试覆盖

**文件：** `AntibotDispatchTest.java`

| 测试方法 | 验证点 |
|---------|--------|
| `testGetAntibotIpHrwStability` | 同一域名多次调用，命中子集稳定（HRW 确定性） |
| `testAntibotProxyConfigPick` | 同 antibotIp + type，每次选相同代理 |
| `testEndToEndPickByType` | 端到端：domain → proxyType → antibotProxy 完整链路 |
| `testKGrowsWithTraffic` | 热域名 K 值 ≥ 冷域名 K 值（流量驱动子集扩散） |
| `testRedisRollingWindowSmooth` | 同桶内多次读数一致，滑动窗口时序正确 |

---

## 十一、改动文件汇总

| 文件 | 类型 | 改动说明 |
|------|------|---------|
| `CrawlerRequestConsumer.java` | 修改 | 替换随机选机器为 HRW 亲和路由，新增流量统计调用 |
| `RedisService.java` | 修改 | 新增 antibot 分桶流量统计（incr/get） |
| `AntibotProxyConfigProvider.java` | **新增** | 代理分类型绑定，hash 稳定选代理 |
| `AntibotDownloader.java` | 修改 | 新增 antibotProxy 覆盖逻辑（fallback 通用代理） |
| `CrawlerController.java` | 修改 | 同步替换 HTTP 直调路径的 getAntibotIp 调用 |
| `AntibotDispatchTest.java` | **新增** | 覆盖 HRW 稳定性、代理绑定、K 动态调整等场景 |

---

## 十二、上线后如何观测收益

### 现有打点（改造前就有，改造后继续生效）

| CAT 打点 | 位置 | 含义 |
|---------|------|------|
| `Cat.newTransaction("antibot.download", domain)` | `AntibotDownloader.java:72` | 每次 Antibot 下载的事务，记录耗时和成败 |
| `Cat.newTransaction("antibot.api.call", domain)` | `AntibotDownloader.java:106` | 调 Antibot HTTP API 的耗时 |
| `Cat.newCompletedTransactionWithDuration("antibot.download.success", domain, cost)` | `AntibotDownloader.java:171` | 下载成功时记录耗时，**可按 domain 维度看成功率** |
| `Cat.logEvent("antibot.rate.limited", domain)` | `AntibotDownloader.java:143` | Antibot 返回 429 限流，**可观测被限流频率** |
| `Cat.logEvent("antibot.gray.hit", domain)` | `CrawlerRequestConsumer.java:266` | 命中灰度走 Antibot 链路 |
| `Cat.logMetricForCount("download.httpCode.xxx")` | `Downloader.java:380` | HTTP 状态码分布（2xx/4xx/5xx） |
| `Cat.logEvent("download.httpCode_detail", statusCode)` | `Downloader.java:386` | 关键状态码明细（403/429等） |

### 改造后新增的日志（异常/降级路径）

| 日志 | 位置 | 含义 |
|-----|------|------|
| `log.warn("[antibot] incr traffic fail domain={}")` | `CrawlerRequestConsumer.java:790` | Redis 写流量计数失败（降级走 sqrt 默认 K） |
| `log.warn("[antibot] computeAntibotSubsetSize fallback domain={}")` | `CrawlerRequestConsumer.java:773` | K 计算异常降级 |
| `log.warn("antibot precompute proxyType err")` | `CrawlerRequestConsumer.java:264` | proxyType 提前计算失败（降级用通用代理） |

> **这几条 warn 出现时说明流量统计或代理绑定未生效，亲和性降级为随机。**

---

### 如何判断改造有没有收益

**改造前后没有新增专门的收益指标打点**，需通过**现有打点的横向对比**来判断。

#### 方法一：看 403/429 率变化（最直接）

Antibot 的核心收益是降低被识别为爬虫的概率，被识别后通常返回 403 或 429。

```
CAT 查询：download.httpCode_detail 中 403、429 的 count
对比维度：同一批域名，改造前 vs 改造后
预期：改造后 403/429 率下降
```

#### 方法二：看 antibot.download.success 按 domain 的成功耗时

```
CAT 查询：antibot.download.success 的 TP99/平均耗时，按 domain 分组
预期：亲和后 Antibot 机器有上下文缓存，响应时间应有一定下降
```

#### 方法三：看 antibot.rate.limited 事件频率

```
CAT 查询：antibot.rate.limited 的 count 趋势
预期：代理绑定后出口 IP 行为稳定，限流次数下降
```

#### 方法四：通过 Redis 确认流量统计是否正常工作

直接查 Redis 里的 key（线下/测试环境验证）：

```
antibot_traffic 分类下：
  {taobao.com}:d:{bucket}    → taobao.com 域名当前桶计数
  {antibot_total}:{bucket}   → 全局当前桶计数
```

如果两个 key 的值在正常增长，说明流量统计模块工作正常，K 动态调整有数据支撑。

---

### 当前观测能力的局限

**没有直接打点 K 值和子集命中情况**，无法从 CAT 直接看到：
- 某个域名当前被分配了几台机器（K 值）
- 每次请求实际落到了哪台 Antibot 机器
- 代理绑定是走了 antibotProxyConfig 命中还是 fallback 通用代理

如果需要更精细地观测，可以在 `getAntibotIp` 里补充打点，例如：
```java
Cat.logEvent("antibot.subset.size", topDomain + ":" + K);
Cat.logEvent("antibot.ip.selected", ips.get(pick));
```
但目前代码里没有这些打点。