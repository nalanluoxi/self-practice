# 截图流量统计 - domain 维度 Redis 监控设计方案

## 一、背景与目标

### 现状
- `ScreenshotRequestConsumer` 已有漏斗埋点 `addFunnelEvent()`，但使用 CAT `logEvent`，**上限 400 个 key**，无法覆盖所有 domain/host 维度
- 现有 `RedisService.recordTraffic()` 是针对 `SitemapRequest` 的，以 `batchId + type + dispatchTs` 为 field，**不支持 domain 维度**
- 两套逻辑分散，没有统一的 screenshot 流量监控查询入口

### 目标
1. 在 `ScreenshotRequestConsumer` 各过滤节点，按 **domain/host** 维度统计每个漏斗阶段的流量
2. 将写入逻辑抽离为独立 Service，不依赖 `SitemapRequest` 类型
3. 新增查询接口，在 `ScreenshotController` 中统一提供，方便排查问题

---

## 二、现有 Sitemap 流量统计机制分析

### Redis 数据结构

```
category: sitemap_monitor

Hash key:  monitor:hour:{yyyyMMddHH}:{monitorType}
  field:   {type}_:_{batchId}_:_{dispatchTsHour}
  value:   累计 count

Set key:   monitor:all:branchIds   → 所有出现过的 batchId
Set key:   monitor:all:types       → 所有出现过的 monitorType
```

### 问题
- `field` 中的 `type` 来自 `SitemapRequest.getType()`（sitemap/robots 等），**没有 domain 字段**
- `recordTraffic` 方法签名强依赖 `SitemapRequest`，screenshot 无法复用

---

## 三、截图流量统计设计

### 3.1 Redis 数据结构

新增独立的 category 和 key 体系，与 sitemap 完全隔离：

```
category: screenshot_monitor

# 漏斗阶段统计（按系统当前小时分桶）
Hash key:  screenshot:funnel:hour:{yyyyMMddHH}:{funnelStage}
  field:   {domain}                   ← 以 domain 为维度
  value:   累计 count

# domain 索引（用于查询时枚举所有出现过的 domain）
Set key:   screenshot:monitor:all:domains
Set key:   screenshot:monitor:all:stages

TTL: 7 天（与 sitemap 保持一致）
```

**漏斗阶段（funnelStage）枚举：**

| 阶段 key | 含义 |
|---------|------|
| `receive` | MQ 收到消息 |
| `jsonError` | 反序列化失败 |
| `urlBlank` | url 为空 |
| `stale` | 消息过期丢弃 |
| `invalidUrl` | URL 格式非法 |
| `blacklist` | 黑名单过滤 |
| `qpsLimited` | Rhino 限流 |
| `redisDuplicate` | Redis 去重命中 |
| `redisError` | Redis 查询异常 |
| `threadpoolEnter` | 进入线程池 |
| `threadpoolResend` | 线程池满重投 |

### 3.2 新增 Service：`ScreenshotTrafficService`

抽离为独立 Service，**不依赖** `SitemapRequest`，入参只需 `domain` 和 `stage`：

```java
// 写入
screenshotTrafficService.record(String stage, String domain);

// 查询（按天）
screenshotTrafficService.queryByDay(String day);           // 返回所有 stage 的全天聚合
screenshotTrafficService.queryByDayAndStage(String day, String stage);  // 指定 stage 按小时明细
screenshotTrafficService.queryByDayAndDomain(String day, String domain); // 指定 domain 跨 stage 汇总
```

### 3.3 改造 `ScreenshotRequestConsumer`

将现有 `addFunnelEvent()` 替换/增强：

```java
// 改造前：只打 CAT，无法按 domain 统计
addFunnelEvent("screenshot.funnel.blacklist", request);

// 改造后：同时写 Redis
addFunnelEvent("screenshot.funnel.blacklist", request);
screenshotTrafficService.record("blacklist", request.getDomain());
```

> **注意**：CAT logEvent 保留用于实时监控告警，Redis 用于精细化历史查询，两者互补，不互替。

### 3.4 查询接口（`ScreenshotController` 中新增）

```
GET /screenshot/traffic/stages
    → 返回所有漏斗阶段 key 列表

GET /screenshot/traffic/domains
    → 返回出现过的 domain 列表

GET /screenshot/traffic/day?day=20250514
    → 返回指定天各 stage 的 domain 汇总

GET /screenshot/traffic/day/stage?day=20250514&stage=blacklist
    → 返回指定天、指定 stage 的按小时明细 + domain 分布

GET /screenshot/traffic/day/domain?day=20250514&domain=example.com
    → 返回指定天、指定 domain 在各 stage 的流量
```

---

## 四、实现文件清单

| 文件 | 操作 | 说明 |
|------|------|------|
| `ScreenshotTrafficService.java` | **新增** | 封装 Redis 写入和查询逻辑 |
| `RedisService.java` | **微改** | 新增 screenshot_monitor category 常量；或将底层 Redis 操作下沉到 ScreenshotTrafficService 内部直接调用 `redisStoreClient` |
| `ScreenshotRequestConsumer.java` | **改造** | 在各过滤节点调用 `screenshotTrafficService.record()` |
| `ScreenshotController.java` | **扩展** | 新增 `/screenshot/traffic/**` 查询接口 |

---

## 五、待确认的细节问题

在执行前需要和你确认以下几点：

### Q1：domain 字段取哪个？
Consumer 里同时有 `request.getDomain()` 和 `request.getHost()`：
- `domain` 通常是二级域名（如 `example.com`）
- `host` 是完整主机名（如 `www.example.com`）

**按哪个维度统计？还是两个都统计？**

### Q2：`ScreenshotTrafficService` 是否复用 `redisStoreClient`？
- 方案 A：通过 `RedisService` 新增 `recordScreenshotTraffic()` 方法，category 和逻辑都放在 `RedisService` 里（与 sitemap 保持同一入口）
- 方案 B：`ScreenshotTrafficService` 直接 `@Autowired RedisStoreClient`，完全独立

**倾向哪种？**

### Q3：是否需要统计 `batchId` 维度？
现有 sitemap 统计的 field 包含 `batchId + dispatchTs`，截图这边：
- 只统计 **domain 维度**（更精细，适合排查哪些站点被过滤）
- 还是 **domain + batchId** 双维度（field = `{domain}_:_{batchId}`，兼顾批次追踪）

### Q4：查询 Controller 放在哪里？
- 放在现有 `ScreenshotController`（`/screenshot/traffic/**`）
- 还是新建独立的 `TrafficMonitorController`（统一管理 sitemap + screenshot 两路流量查询）

### Q5：`receive` 阶段是否统计？
反序列化失败时 domain 为 null，需要用 `"unknown"` 兜底。是否需要统计这个阶段？
