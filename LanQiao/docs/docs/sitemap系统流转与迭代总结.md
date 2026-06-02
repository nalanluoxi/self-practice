# Sitemap 系统流转与迭代总结

> 整理时间：2026-06-02
> 涉及项目：llm-spider-crawler（crawler 服务）、llm-spider-jobs（Spark 离线任务）
> 业务背景：美团 FRADAY 组（longcat 大模型数据团队），为大模型训练语料构建网页抓取-解析-清洗全流程体系；sitemap 环路为其中独立的「站点新链发现」子系统，目标是通过 robots.txt → sitemap.xml 递归解析，向下游抓取服务持续输送高质量新链接。

---

## 一、系统全链路流转图

```
【离线调度层 llm-spider-jobs】
        │
        ├─ ① RobotsMsgSendingJob（msgsending 分支）
        │     读取 mart_llm_data.llm_host_robots_url
        │     构造 type=robots 消息，延时 6s 发送
        │     → Topic: llm_sitemap_url
        │
        ├─ ② UpdateFrequencyTableJob（dev/sitemap-frequency-delivery 分支）[T+1 离线运行]
        │     输入：search_api_result_dt（抓取结果） + bloomfilter新链表
        │           + llm_crawler_domain_save_info（留存质量） + spider_site_info（黑名单）
        │     计算 compositeScore = 0.5*newUrlScore + 0.5*fuzzyScore
        │     等级映射 → grade 1(HIGH)/2(MIDDLE)/3(LOW)/4(DISUSE)/5(PENDING)
        │     降级限速：每次最多降 1 级
        │     输出：
        │       → DI 表（每日快照）：llm_data_di_sitemap_frequency_delivery
        │       → DF 表（累积表）：llm_data_df_sitemap_frequency_delivery  ← ③ 的数据来源
        │
        └─ ③ FrequencyDeliverySitemapJob（dev/sitemap-frequency-delivery 分支）[依赖 ② 产出]
              读取 ② 产出的 DF 表最新 dt
              按 grade 参数过滤（grade IN (1,2,3,...) 可配置）
              构造 type=sitemap 消息（ext 携带 batchId/dispatchTs/grade）
              → Topic: llm_sitemap_url（延时 6s）


【在线消费层 llm-spider-crawler】
        ↓
        ③ SitemapRequestConsumer.receive()
              预处理漏斗（顺序过滤）：
              1. JSON 反序列化
              2. ext.dispatchTs 过期检测（默认 48h 阈值，lion 配置）
              3. URL 格式校验 / 长度校验 / 重复字符检测（checkDomainSet 触发精细校验）
              4. 正则黑名单过滤（sitemap_regular_filter_list）
              5. 黑名单域名/后缀过滤（FilterUtils.isValid）
              6. 站点级黑名单（PreprocessUtil.isOnBlackList）
              7. Redis 去重（llm_sitemap_url:{url}）
              8. 动态 domain QPS 限流（LimiteUtils）→ 重投队列
              9. 集群/单节点/站点三级 Rhino 限流 → 重投队列
              10. suspend 开关检测
              11. 内存阈值检测（exceedMaxInProcessMem）
              12. 提交线程池（SynchronousQueue，线程数 lion 配置）
              │
              │  线程池满则 synchronized 阻塞等待
              │  超时（MAX_WAIT_TIME，lion 配置）后重投，最多重试 2 次
              ↓
        ④ SitemapService.download()
              按 type 分流：
              ┌─ robots → downLoadRobotsTxt()
              │     HTTP 下载 robots.txt
              │     解析 sitemap URL 列表
              │     写入 MySQL robots_txt 表（upsert）
              │     若 crawlSitemap=true，对每个 sitemapUrl 发送消息
              │         → SitemapPublishService.sendRequest(sitemapUrl, sitemap, ext)
              │         → Topic: llm_sitemap_url（回环）
              │
              └─ sitemap → downLoadSitemap()
                    Redis 查重（同一 URL 不重复抓取）
                    HTTP 下载 sitemap.xml / siteindex.xml
                    写入 Redis 去重键（过期时间按 grade 差异：1级=3h, 2级=5h, 其他=lion配置）
                    parseSitemapAndSend()：
                    ┌─ SiteMapIndex（索引文件）
                    │     → 对每个子 sitemapUrl 发送消息
                    │     → Topic: llm_sitemap_url（回环，深度可达 N 级）
                    │
                    └─ SiteMap（叶子文件）
                          提取所有 page URL（urlList）
                          分批（batchSendResultSize）发送结果
                          → SitemapPublishService.sendResultList()
                          → Topic: sitemapResultTopic（结果 Topic）


【结果消费层（下游）】
        ↓
        ⑤ 下游消费 sitemapResultTopic
              SitemapUrlResult 结构：
              - reqId, url（JSON 数组）, host, domain, startTime
              - ext：{count, urlType:"list", sitemapUrl, batchId, dispatchTs}
              结果落入 search_api_result_dt（含 ext.sitemapUrl 溯源字段）


【离线评估层 llm-spider-jobs（T+1）】
        ↓
        ⑥ search_api_result_dt 数据就绪后，触发 UpdateFrequencyTableJob
              （即离线调度层 ② 的详细逻辑，此处为实际运行时序位置）
              对每条抓取结果按 sitemapUrl 聚合，计算 hit_batch_cnt
              与 bloomfilter/domain_save_info/spider_site_info 关联打分
              → 写入 DF 表 / DI 表，供 ③ 下一轮投递使用
              ↓
        ⑦ FrequencyDeliverySitemapJob（③）读 DF 表最新 dt，按 grade 过滤投递
              → 回到 Topic: llm_sitemap_url，形成完整闭环
```

---

## 二、数据流转闭环总结

```
                 ┌────────────────────────────────────────────────┐
                 │   llm-spider-jobs 离线调度                      │
                 │                                                │
                 │  ① RobotsMsgSendingJob                         │
                 │    (llm_host_robots_url → MQ)                  │
                 │                                                │
                 │  ② UpdateFrequencyTableJob  [T+1 依赖结果层]    │
                 │    search_api_result_dt + bloomfilter           │
                 │    + domain_save_info + spider_site_info        │
                 │    → 打分 → DF 表 / DI 表                      │
                 │                  ↓                             │
                 │  ③ FrequencyDeliverySitemapJob                  │
                 │    (读 DF 表最新 dt，按 grade 过滤 → MQ)        │
                 └──────────────┬─────────────────────────────────┘
                                │ Topic: llm_sitemap_url
                                ↓
                 ┌──────────────────────────────────────┐
                 │   llm-spider-crawler 在线消费          │
                 │                                      │
                 │  SitemapRequestConsumer              │
                 │       → 过滤漏斗                      │
                 │       → 线程池                        │
                 │       → SitemapService.download()    │
                 │                                      │
                 │  robots.txt 下载                      │
                 │       → 解析出 sitemapUrl             │
                 │       → 发回 llm_sitemap_url（回环）  │
                 │                                      │
                 │  sitemap.xml 下载                     │
                 │       → SiteMapIndex → 发回（回环）   │
                 │       → SiteMap → 发 sitemapResult   │
                 └──────────────┬───────────────────────┘
                                │ Topic: sitemapResultTopic
                                ↓
                 ┌──────────────────────────────────────────────────┐
                 │   下游 Page URL 消费                               │
                 │   (search_api_result_dt 入库，含 ext.sitemapUrl)   │
                 └──────────────┬───────────────────────────────────┘
                                │ 离线 T+1
                                ↓
                 ┌──────────────────────────────────────────────────┐
                 │   ② UpdateFrequencyTableJob                       │
                 │   search_api_result_dt + bloomfilter              │
                 │   + domain_save_info（留存率/量打分）              │
                 │   + spider_site_info（黑名单过滤）                 │
                 │   → 聚合计算 compositeScore → 等级映射 → 降级限速  │
                 │   → 写 DF 表（累积）/ DI 表（每日快照）            │
                 └──────────────┬───────────────────────────────────┘
                                │ DF 表就绪
                                ↓
                 ┌──────────────────────────────────────────────────┐
                 │   ③ FrequencyDeliverySitemapJob                   │
                 │   读 DF 表最新 dt，按 grade 过滤                  │
                 │   → Topic: llm_sitemap_url（回到离线调度层入口）   │
                 └──────────────────────────────────────────────────┘
```

**核心回环路径（3条）：**
1. `llm_host_robots_url → MQ → robots.txt 下载 → sitemapUrl → MQ → sitemap.xml 下载 → pageUrl`（站点初始发现回环）
2. `SiteMapIndex → 子 sitemapUrl → MQ → SiteMap → pageUrl`（多层级 sitemap 深度展开，可 N 层递归）
3. `pageUrl 结果 → search_api_result_dt → UpdateFrequencyTableJob 打分 → DF 表 → FrequencyDeliverySitemapJob 投递 → MQ`（频度评估闭环，T+1）

---

## 三、关键配置（Lion 动态配置）

| 配置键 | 说明 |
|--------|------|
| `sitemap_thread_num` | 线程池大小，支持动态热更新 |
| `sitemap_max_wait_time` | 线程池满时最大等待时长（默认 36s） |
| `sitemap_stale_threshold_hours` | 过期消息丢弃阈值（默认 48h） |
| `sitemap_crawl_status_expire_time` | sitemap 去重 Redis 过期时间（默认） |
| `sitemap_common_config.isSuspend` | 暂停消费开关 |
| `sitemap_common_config.crawlSitemap` | 是否解析 sitemap 并发现子链接 |
| `sitemap_common_config.maxInProcessMemInBytes` | 最大内存上限 |
| `sitemap_black_domain_list` | 域名黑名单 |
| `sitemap_regular_filter_list` | 作弊站点正则过滤列表 |
| `sitemap_check_url_domain` | 启用精细 URL 校验的域名集合 |
| `sitemap_batch_send_result_size` | 结果批量发送大小 |

---

## 四、MQ Topic 汇总

| Topic | 方向 | 消息类型 | 说明 |
|-------|------|----------|------|
| `llm_sitemap_url` | 上游→crawler / crawler 内部回环 | SitemapRequest | robots/sitemap 下载请求 |
| `sitemapResultTopic` | crawler → 下游 | SitemapUrlResult | page URL 结果列表 |
| `timeResultTopic` | crawler → 下游 | PostLink 列表 | 时效性流量链接（TIME_GROUP） |

---

## 五、频度等级定义

| Grade | Code | 含义 | 调度间隔（批次数） |
|-------|------|------|------------------|
| HIGH | 1 | 高频，质量高 | 6批次命中≥1即升 |
| MIDDLE | 2 | 中频 | 4 |
| LOW | 3 | 低频 | 2 |
| DISUSE | 4 | 废弃保活，留存率>0 | 每日1次 |
| PENDING | 5 | 黑名单/纯垃圾，不投递 | 0 |

**降级限速**：每次评估最多降 1 级（如 1→2，不能直接 1→3）

---

## 六、关键数据指标（各阶段业务产出）

| 阶段 | 时间 | 指标 | 数值 |
|------|------|------|------|
| 基线（限流建立前） | 2025-11 | 集群 QPS | 6~8 |
| 多线程重构后 | 2025-11-17 | 集群 QPS | 400 |
| 多线程重构后 | 2025-11-17 | 小时抓取量 | 2.5w → 96w（38×） |
| 多线程重构后 | 2025-11-17 | 日新链发现量 | 2~3 亿 → 10~14 亿（最高 75 亿） |
| 自适应限流后 | 2025-11-27 | 峰值 QPS | 1417 |
| 自适应限流后 | 2025-11-27 | 英文留存量 | 2000w → 1.2 亿 |
| 自适应限流后 | 2025-11-27 | 全量任务（1000w 站点）耗时 | 3~4 h |
| gz 压缩包支持后 | 2025-12-17 | 日新链发现量 | 3.79 亿 → 52.6 亿（14×） |
| gz 压缩包支持后 | 2025-12-17 | 压缩包占比 | 4.08%~16.23% |
| 频度调度上线后 | 2026-02 | 每日投递规模 | 770w 站点 / 天 |
| 5.5 亿站点扩量 | 2026-02~03 | 总体投递量 | 分片投递 5.5 亿站点 |
| 频度调度v2上线后 | 2026-05 | sitemap日新链产出 | **100~200 亿/天**（峰值200亿，提升约10倍） |
| 频度调度多批次命中 | 2026-05-10~15 | 日均多批次命中sitemap | **~413 万**（峰值493万/0514） |
| 频度调度URL增量 | 2026-05-10~14 | 日均额外贡献URL | **~908 万**（峰值1140万/0514） |
| sitemap模糊留存贡献 | 2026-04-18~24 | 贡献留存总量 | **约 85 亿** |
| 最新状态（2026-05） | 2026-05 | 每日 robots 投递规模 | 3000w 中文站点 |
| 中文站点挖掘 | 2026-06 | 中文站点挖掘数量 | **4000 万** |

---

## 七、迭代历史总结

### llm-spider-jobs（dev/sitemap-frequency-delivery 分支）

| 时间 | 关键迭代 |
|------|---------|
| 2026-01-29 | 频度调度初版上线，搭建 DF/DI 表结构，测试 sitemap 频率调度累计表逻辑 |
| 2026-02-02 | 字段对齐，切换正式表，解耦 dt 参数与 UpdateFrequency 任务 |
| 2026-02-09 | 放宽调度阈值，对 1/2/3 级直接发送 |
| 2026-03-03~12 | 回捞 grade4 数据，5.5 亿站点分片投递（分片0/1/2） |
| 2026-04-07 | 降低并发，设置 repartition 为 5，之后扩到 50 个 partition |
| 2026-04-17~24 | 频度调度策略迭代（调整策略1/2），动态分母计算，发送等级从参数获取 |
| 2026-04-27~28 | 修复 info 表 domain/dt 使用逻辑，修复类型转换 |
| 2026-05-06 | 修改与 info 表不强依赖（info 表有数据用，没有也能运行） |

### llm-spider-jobs（dev/sitemap_msg_sending 分支）

| 时间 | 关键迭代 |
|------|---------|
| 2026-01~02 | 初版 770w 站点每天投递，5.5 亿站点分片投递 |
| 2026-03~04 | 770w 站点/767w 站点调优，Spark 生产者初始化时机优化（driver 端创建） |
| 2026-04-10 | ext 添加 batchId / dispatchTs 字段，同一批次使用同一 dispatchTs |
| 2026-04-13~14 | 修改 ext 填写方式，50 个 partition，修改依赖 |
| 2026-05-27 | 投递中文站点 3000w robots |

### llm-spider-crawler（sitemap 相关迭代）

| 时间 | 分支 | 关键迭代 |
|------|------|---------|
| 2024-11 | release | SitemapService / SitemapRequestConsumer 初版上线 |
| 2025-11-10 | release | 三级限流体系：集群层(Rhino) + 单机层 + Redis domain 精确层，前置黑名单/去重拦截，解决大站挤兑小站问题 |
| 2025-11-17 | release | 多线程重构：SynchronousQueue + ThreadPoolExecutor，receive() 与业务逻辑解耦；Mafka 扩容 200→400 分区；集群 QPS 提升 16× |
| 2025-11-27 | release | 自适应本地限流：LimiteUtils 引入 CounterRateLimiter（ConcurrentSkipListMap 滑动窗口），每秒采样线程池健康度动态调整限流阈值 |
| 2025-12-17 | release | gz 压缩包解析：CompressedUtil.isValid() + getCompressedData()，GZIPInputStream 流式解压，日均处理 400w+ 压缩包，新链 14× 增长 |
| 2026-02-04 | release | 频度调度 v1：resolveExpireTimeByGrade() 按 grade 差异化 Redis TTL（1级=3h, 2级=5h），ext 透传 batchId/dispatchTs/grade |
| 2026-03-19 | release | 策略拦截：@PostConstruct 预编译 regularPatternList，@MdpConfigListener 热更新，90% 垃圾站点拦截 |
| 2026-04-14 | dev/add-sitemap-monitor | 添加 sitemap 监控指标，Mafka 扩容、Lion 配置补充 |
| 2026-04-21 | release | 子链接缓存/查询回写，redis 桶逻辑（按写入时间计数） |
| 2026-04-23~25 | dev/add-sitemap-monitor | 完整漏斗打点（sitemap.funnel.* 系列），Redis recordTraffic 按 batchId+batchHour 统计 |
| 2026-05-06 | release | 频度调度 v2：多维打分（newUrlScore + fuzzyScore + rateScore），动态分母，一天多轮投递 |
| 2026-05-15 | release | sitemap 线程阻塞告警时间通过 lion 配置（10 分钟） |
| 2026-05 | dev/html-page-js-download | 无效站点治理：dispatchTs 过期消息丢弃（staleThresholdHours=48h），失败原因细分持久化 |

---

## 八、test-project 技术验证背景

`/Users/nalan/IdeaProjects/test-project` 存放了 sitemap 系统演进过程中的本地验证脚本，主要包含：

- `compress/CompressedUtil.java`：全格式压缩包解压工具探索版（.gz/.tar.gz/.bz2/.zip/.7z，全注释），为 2025-12-17 gz 上线的扩展版本。最终因其他格式频次低（<10条/h）未上线，策略模式+工厂方法扩展设计保留为参考。
- `compress/TestCompresUtil.java`：本地验证入口，用 .7z/.rar 样例测试解压逻辑。
- `compress/CompressedUtil1.java`：压缩包工具简化版（仅 gz）。
- `compress/LocalListTest.java` / `TestLocal.java`：本地 URL 列表批量测试（压缩包/频度验证）。
- `compress/GetBMUrl.java`：获取 BM 站点 URL 批量脚本（站点批量投递验证）。
- `tempDownLoadRetry/`：临时下载重试脚本（Download/Retry/RetryDownload），用于抓取失败补偿场景验证。
- `test/GetMsg.java`：MQ 消息接收本地验证脚本（消息格式调试）。
- `test/util/SuffixTrie.java`：URL 后缀 Trie 树实现，用于高效后缀黑名单匹配（策略拦截原型）。
- `test/util/JsonFieldExtractor.java`：JSON 字段提取工具，用于 ext 字段解析调试。
- `checkBaYou/check.java`：白名单域名检测脚本（站点管理）。
- `getIp/GetIpList.java`：获取代理 IP 列表脚本（代理管理）。

---

## 七、关键设计决策

1. **Redis 去重双层机制**：Consumer 层消费时去重（入口）+ Service 层下载前去重（sitemap.xml 级别），防止重复抓取
2. **线程池 synchronized 阻塞**：SynchronousQueue + 全局锁等待，背压传导到消费速度，避免线程爆炸
3. **ext 字段透传**：batchId/dispatchTs/grade 在整条调用链（robots→sitemap→page URL 结果）中完整透传，用于漏斗统计和频度评估
4. **降级限速**：频度等级每次最多降 1 档，防止一次抓取结果异常导致大量 URL 直接跌入 PENDING
5. **S3 上传已注释**：robots.txt 和 sitemap.xml 原始内容的 S3 上传逻辑已关闭（代码注释状态），仅将解析结果发 MQ
6. **时效性流量独立 Group**：TIME_GROUP 流量走独立 topic（timeResultTopic），与常规频度流量隔离