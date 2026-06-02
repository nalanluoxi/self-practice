# Robots.txt 失败记录优化设计方案

## 背景

`saveFailRobotsTxt` 方法在爬取 robots.txt 失败时，以 `host` 为唯一标识对 `site_robots_txt` 表做 upsert 操作。
每次失败都覆盖上一条记录，只保留最近一次状态，存在以下问题。

---

## 现有问题

| 问题 | 说明 |
|------|------|
| 无历史记录 | 只保留最后一次失败状态，无法判断是偶发还是持续失败 |
| 成功被失败覆盖 | host 先成功爬取后失败，成功记录被覆盖，导致数据污染 |
| 无法统计频率 | 不知道一个 host 被 CF 拦截了几次，无法做趋势分析 |
| 调试困难 | 排查问题时缺乏上下文和历史快照 |

---

## 优化方案：主表 + 异常历史表

### 核心思路

双表分离，职责明确：

```
site_robots_txt（主表）              site_robots_fail_log（异常历史表）
├── 每个 host 只有一条记录            ├── 每次失败都 INSERT 一条
├── 记录该 host 的最新状态            ├── 保留完整历史，用于趋势分析
└── status 字段标记当前成功/失败       └── 可按时间定期清理，不影响主业务
```

---

## DDL 变更

### 1. 主表新增字段

```sql
ALTER TABLE site_robots_txt
    ADD COLUMN status           VARCHAR(20)  DEFAULT 'success' COMMENT '当前状态: success/fail',
    ADD COLUMN fail_count       INT          DEFAULT 0         COMMENT '累计失败次数',
    ADD COLUMN last_fail_reason VARCHAR(100)                   COMMENT '最近一次失败原因';
```

### 2. 新增异常历史表

```sql
CREATE TABLE site_robots_fail_log (
    id          BIGINT       PRIMARY KEY AUTO_INCREMENT,
    host        VARCHAR(500) NOT NULL                  COMMENT '站点 host',
    domain      VARCHAR(200)                           COMMENT '顶级域名',
    url         VARCHAR(1000)                          COMMENT '实际请求 URL',
    fail_reason VARCHAR(100)                           COMMENT '失败原因: cf_blocked/captcha_blocked 等',
    http_code   INT                                    COMMENT 'HTTP 状态码',
    ext         TEXT                                   COMMENT '附加信息（原始响应片段等）',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
    INDEX idx_host        (host),
    INDEX idx_domain      (domain),
    INDEX idx_create_time (create_time)
) COMMENT='robots.txt 爬取失败历史日志';
```

---

## 代码变更

### 新增 Entity

```java
@Data
public class RobotsTxtFailLogEntity {
    private Long   id;
    private String host;
    private String domain;
    private String url;
    private String failReason;
    private Integer httpCode;
    private String ext;
    private Date   createTime;
}
```

### 新增 Mapper 方法

```java
// RobotsTxtFailLogMapper.java
@Insert("INSERT INTO site_robots_fail_log (host, domain, url, fail_reason, http_code, ext) " +
        "VALUES (#{host}, #{domain}, #{url}, #{failReason}, #{httpCode}, #{ext})")
void insertFailLog(RobotsTxtFailLogEntity entity);
```

```java
// RobotsTxtMapper.java 新增
@Update("UPDATE site_robots_txt " +
        "SET status = 'fail', fail_count = fail_count + 1, last_fail_reason = #{failReason}, ext = #{ext} " +
        "WHERE id = #{id}")
void updateToFail(RobotsTxtEntity entity);
```

### saveFailRobotsTxt 改造逻辑

```java
private void saveFailRobotsTxt(SitemapRequest request, String url, CrawledResult result, String failReason) {
    try {
        String host = request.getHost();
        if (StringUtils.isBlank(host)) {
            host = url;
        }

        // 1. 解析失败原因
        String actualFailReason = failReason;
        Integer httpCode = null;
        if (result != null) {
            httpCode = result.getHttpStatusCode();
            if (result.getContent() != null && StringUtils.isNotBlank(result.getContent().getData())) {
                String data = result.getContent().getData();
                if (data.contains("error code: 1005") || data.contains("error code: 1020")) {
                    actualFailReason = "cf_blocked_" + (data.contains("1005") ? "1005" : "1020");
                } else if (data.contains("captcha") || data.contains("CAPTCHA") || data.contains("验证码")) {
                    actualFailReason = "captcha_blocked";
                } else if (data.toLowerCase().contains("cloudflare") && httpCode != null && httpCode == 403) {
                    actualFailReason = "cf_blocked";
                }
            }
        }

        // 2. 始终 INSERT 一条历史日志（完整保留每次失败）
        RobotsTxtFailLogEntity logEntity = new RobotsTxtFailLogEntity();
        logEntity.setHost(host);
        logEntity.setDomain(UrlUtils.getTopDomain(host));
        logEntity.setUrl(url);
        logEntity.setFailReason(actualFailReason);
        logEntity.setHttpCode(httpCode);
        robotsTxtFailLogMapper.insertFailLog(logEntity);

        // 3. 主表 upsert：更新最新状态 + 累加失败次数
        JSONObject extJson = new JSONObject();
        extJson.put("status", "fail");
        extJson.put("fail_reason", actualFailReason);
        if (httpCode != null) {
            extJson.put("http_code", httpCode);
        }

        RobotsTxtEntity entity = new RobotsTxtEntity();
        entity.setUrl(url);
        entity.setHost(host);
        entity.setDomain(UrlUtils.getTopDomain(host));
        entity.setExt(extJson.toJSONString());

        RobotsTxtEntity existing = robotsTxtMapper.getRobotsTxtByHost(host);
        if (existing == null) {
            // 首次记录，status=fail, fail_count=1
            robotsTxtMapper.insertRobotsTxt(entity);
        } else {
            // 已有记录，只更新失败状态，fail_count+1
            entity.setId(existing.getId());
            entity.setFailReason(actualFailReason);
            robotsTxtMapper.updateToFail(entity);
        }

        log.info("saveFailRobotsTxt saved, host:{}, fail_reason:{}, http_code:{}", host, actualFailReason, httpCode);
    } catch (Exception e) {
        log.error("saveFailRobotsTxt error, url:{}", url, e);
    }
}
```

---

## 方案对比

| | 纯 INSERT 历史表 | 主表 + 历史表（本方案） |
|---|---|---|
| 历史追溯 | ✅ | ✅ |
| 查某 host 当前状态 | ❌ 需扫描大量记录 | ✅ 主表一条记录直接查 |
| 成功→失败状态流转 | ❌ 难判断 | ✅ status 字段清晰标记 |
| 数据量控制 | ❌ 无限增长 | ✅ 历史表可按时间定期清理 |
| 调度逻辑复用 | ❌ 无法作为调度依据 | ✅ 主表可直接驱动重试逻辑 |

---

## 历史表清理策略

历史表仅用于分析，不影响主业务，建议定期清理：

```sql
-- 保留最近 30 天数据
DELETE FROM site_robots_fail_log
WHERE create_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
```

可通过定时任务或数据库事件定期执行。

---

## 后续扩展建议

- **报警**：基于 `fail_count` 阈值，对持续失败的 host 触发告警
- **重试策略**：主表 `status=fail` 的记录，可按 `fail_count` 分级控制重试间隔（指数退避）
- **CF 专项处理**：`fail_reason` 为 `cf_blocked` 的 host，自动切换代理策略