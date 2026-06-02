# Sitemap 发现结果落库方案

## 背景

当前 sitemap 发现流程的结果仅通过 MQ 向下游传递，没有持久化记录。
无法追溯某个 host 发现了哪些 sitemap、发现时间、每层的链接数量，以及各阶段的失败情况。

---

## 当前流程梳理

```
robots.txt 爬取 (downLoadRobotsTxt)
    └─► 解析出 sitemap URL 列表
            └─► 发送到 llm_sitemap_url MQ → downLoadSitemap (type=sitemap)
                    └─► 解析 XML
                            │
                            ├─► SiteMapIndex（index 型，内容是子 sitemap）
                            │       └─► 再次发送到 llm_sitemap_url MQ（递归）
                            │
                            └─► SiteMap（叶子型，内容是页面 URL）
                                    └─► sendResultList → 发送到【结果 MQ】
                                                              └─► Spark 同步到 Hive
```

### 两种 sitemap 类型

| 类型 | 内容 | 下一步 |
|------|------|--------|
| `SiteMapIndex` | 子 sitemap 的 URL 列表 | 重新发 `llm_sitemap_url` 递归处理 |
| `SiteMap`（叶子） | 页面 URL 列表 | 发结果 MQ → Spark → Hive |

---

## 失败场景分类

根据流程，失败点有 4 处：

| 失败点 | 位置 | 影响 |
|--------|------|------|
| ① sitemap URL 下载失败 | `downLoadSitemap` | 该 sitemap 整条子树全部丢失 |
| ② sitemap 解析失败（XML 格式异常） | `parseSitemapAndSend` catch | 已下载内容无法使用 |
| ③ 解析结果为空（`counted=false`） | `parseSitemapAndSend` 末尾 | 有内容但未能提取链接 |
| ④ 结果 MQ 发送失败 | `sendResultList` | 页面 URL 未送达 Spark，Hive 数据缺失 |

**其中最需要记录的是 ① 和 ④**：① 导致整棵子树静默丢失；④ 导致已发现的页面 URL 无法进入 Hive。

---

## 方案设计

### 核心思路

**不单独建"失败表"，而是在 `site_sitemap_discovery` 中记录每个 sitemap URL 的全生命周期状态。**

每个 sitemap URL 在被发现时即写入（`status=pending`），下载结束后更新状态（`success/fail`）。
这样既有历史记录，又有失败追踪，一张表解决所有问题。

---

### 新增表：`site_sitemap_discovery`

```sql
CREATE TABLE site_sitemap_discovery (
    id              BIGINT        PRIMARY KEY AUTO_INCREMENT,
    host            VARCHAR(500)  NOT NULL                   COMMENT '所属 host（子域名粒度）',
    domain          VARCHAR(200)                             COMMENT '顶级域名',
    url             VARCHAR(2000) NOT NULL                   COMMENT '本条 sitemap 的 URL',
    parent_url      VARCHAR(2000)                            COMMENT '发现此 URL 的父节点（robots.txt URL 或父 sitemap URL）',
    source_type     VARCHAR(20)                              COMMENT '发现来源: robots / sitemap_index',
    sitemap_type    VARCHAR(20)                              COMMENT '本节点类型: sitemap_index / sitemap_leaf / unknown',
    child_count     INT           DEFAULT 0                  COMMENT '子链接数量（index型=子sitemap数，leaf型=页面URL数）',
    status          VARCHAR(20)   DEFAULT 'pending'          COMMENT 'pending / success / fail',
    fail_reason     VARCHAR(100)                             COMMENT '失败原因',
    http_code       INT                                      COMMENT 'HTTP 状态码',
    result_mq_sent  TINYINT       DEFAULT 0                  COMMENT '页面URL是否已发送结果MQ: 0=否 1=是（仅leaf型有效）',
    batch_id        VARCHAR(100)                             COMMENT '调度批次 ID（来自 ext）',
    create_time     DATETIME      DEFAULT CURRENT_TIMESTAMP  COMMENT '发现时间',
    update_time     DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX uk_url (url(500)),
    INDEX idx_host        (host),
    INDEX idx_domain      (domain),
    INDEX idx_status      (status),
    INDEX idx_batch_id    (batch_id),
    INDEX idx_create_time (create_time)
) COMMENT='sitemap 发现与执行记录表';
```

---

## 各阶段写入逻辑

### 阶段 1：robots.txt 解析成功，发现子 sitemap URL

**位置**：`downLoadRobotsTxt` 第 362-373 行，`rules.getSitemaps()` 非空时

```java
// 发 MQ 之前，批量 INSERT 发现记录
for (String sitemapUrl : sitemaps) {
    SitemapDiscoveryEntity entity = new SitemapDiscoveryEntity();
    entity.setHost(host);
    entity.setDomain(domain);
    entity.setUrl(sitemapUrl);
    entity.setParentUrl(url);           // robots.txt URL
    entity.setSourceType("robots");
    entity.setSitemapType("unknown");   // 尚未下载，类型未知
    entity.setStatus("pending");
    entity.setBatchId(batchId);         // 从 request.getExt() 取
    sitemapDiscoveryMapper.insertIgnore(entity); // 幂等，重复不报错
}
```

---

### 阶段 2：SiteMapIndex 解析完成，发现子 sitemap URL

**位置**：`parseSitemapAndSend` 第 651-674 行，`asm instanceof SiteMapIndex` 分支

```java
// 更新当前 sitemap 节点为 index 类型，同时 INSERT 子节点
// 更新当前节点
sitemapDiscoveryMapper.updateSuccess(url, "sitemap_index", siteMaps.size());

// INSERT 子节点（发 MQ 之前写入）
for (AbstractSiteMap siteMap : siteMaps) {
    SitemapDiscoveryEntity child = new SitemapDiscoveryEntity();
    child.setUrl(siteMap.getUrl().toString());
    child.setParentUrl(url);
    child.setSourceType("sitemap_index");
    child.setSitemapType("unknown");
    child.setStatus("pending");
    child.setBatchId(batchId);
    sitemapDiscoveryMapper.insertIgnore(child);
}
```

---

### 阶段 3：SiteMap（叶子）解析完成，页面 URL 发送结果 MQ

**位置**：`parseSitemapAndSend` 第 676-712 行，`asm instanceof SiteMap` 分支

```java
// 发 MQ 成功后更新
sitemapDiscoveryMapper.updateLeafSuccess(url, urlList.size(), /* result_mq_sent= */ true);
```

```java
// 若 sendResultList 抛异常，捕获后更新状态
sitemapDiscoveryMapper.updateFail(url, "result_mq_send_fail", null);
```

---

### 阶段 4：sitemap 下载失败

**位置**：`downLoadSitemap` 第 523-531 行，`!judgeDownloadSuccess` 分支

```java
sitemapDiscoveryMapper.updateFail(url, "download_fail", httpCode);
```

---

### 阶段 5：sitemap 解析失败

**位置**：`parseSitemapAndSend` catch 块

```java
// UnknownFormatException
sitemapDiscoveryMapper.updateFail(url, "parse_unknown_format", null);

// 其他 Exception
sitemapDiscoveryMapper.updateFail(url, "parse_error", null);
```

---

## Mapper 设计

```java
public interface SitemapDiscoveryMapper {

    // 发现时写入，url 重复则忽略（幂等）
    @Insert("INSERT IGNORE INTO site_sitemap_discovery " +
            "(host, domain, url, parent_url, source_type, sitemap_type, status, batch_id) " +
            "VALUES (#{host}, #{domain}, #{url}, #{parentUrl}, #{sourceType}, #{sitemapType}, #{status}, #{batchId})")
    void insertIgnore(SitemapDiscoveryEntity entity);

    // index 节点下载成功
    @Update("UPDATE site_sitemap_discovery SET sitemap_type='sitemap_index', status='success', child_count=#{childCount} WHERE url=#{url}")
    void updateIndexSuccess(@Param("url") String url, @Param("childCount") int childCount);

    // leaf 节点下载成功，页面 URL 已发结果 MQ
    @Update("UPDATE site_sitemap_discovery SET sitemap_type='sitemap_leaf', status='success', child_count=#{childCount}, result_mq_sent=#{mqSent} WHERE url=#{url}")
    void updateLeafSuccess(@Param("url") String url, @Param("childCount") int childCount, @Param("mqSent") boolean mqSent);

    // 任意失败
    @Update("UPDATE site_sitemap_discovery SET status='fail', fail_reason=#{failReason}, http_code=#{httpCode} WHERE url=#{url}")
    void updateFail(@Param("url") String url, @Param("failReason") String failReason, @Param("httpCode") Integer httpCode);
}
```

---

## 与 Spark/Hive 的关系

Spark 消费结果 MQ → 写 Hive，这条链路**不需要改造**。

`site_sitemap_discovery` 解决的是**落库侧的可观测性**问题：

```
site_sitemap_discovery
    ├─ result_mq_sent = 0 + status = success  → 说明页面URL已解析但MQ发送失败，Hive可能缺数据
    ├─ status = fail + fail_reason             → 某条 sitemap 下载/解析失败，子树全部丢失
    └─ status = pending（长时间未更新）         → 消费积压或丢消息
```

通过查询此表，可以直接定位 Hive 数据缺失的根因，而不需要反查 MQ 日志。

---

## 数据量评估与清理

- 每个 domain 的 sitemap 树通常 10-500 条记录（极少超过 1000）
- 按 `create_time` 保留最近 90 天，或按 `batch_id` 归档历史批次数据

```sql
DELETE FROM site_sitemap_discovery WHERE create_time < DATE_SUB(NOW(), INTERVAL 90 DAY);
```

---

## 与其他表的关系

| 表 | 职责 |
|----|------|
| `site_robots_txt` | robots.txt 协议内容快照（allow/disallow 规则、sitemap URL 列表） |
| `site_robots_fail_log` | robots.txt 爬取失败历史日志 |
| `site_sitemap_discovery`（新增） | 每个 sitemap URL 的发现来源、执行状态、子链接数 |

---

## 后续扩展

- **重试驱动**：查询 `status=fail` 或长时间 `pending` 的记录，自动补推 MQ 重试
- **覆盖率统计**：`SUM(child_count) WHERE sitemap_type=sitemap_leaf AND domain=?` 得到该 domain 通过 sitemap 发现的页面 URL 总量
- **Hive 对账**：对比 `child_count` 与 Hive 实际入库数，发现数据丢失
- **sitemap 树可视化**：基于 `parent_url → url` 递归关系，展示某 domain 的完整 sitemap 结构