# 模块：llm-crawler-server

> 最后更新：2026-05-27

## 模块职责

Server 是整个爬虫系统的核心执行模块。负责：消费 Mafka MQ 任务、根据灰度策略选择 Downloader 下载网页、处理 Sitemap 发现、执行截图+HTML 抓取、上传结果到 S3、发送抓取结果到下游 MQ。

## 包结构

```
llm-crawler-server/src/main/java/com/sankuai/llm/spider/crawler/
├── ApplicationLoader.java          # Spring Boot 启动入口
├── config/                         # 配置（S3、代理重定向策略、Lion 配置）
├── constant/                       # 常量（CAT 埋点名、HTTP 常量、黑名单后缀）
├── consumer/                       # MQ 消费者（核心入口）
│   ├── CrawlerRequestConsumer      # 通用网页抓取消费者
│   ├── SitemapRequestConsumer      # Sitemap 抓取消费者
│   ├── ScreenshotRequestConsumer   # 截图/HTML 抓取消费者
│   ├── WebRequestConsumer          # Web 抓取消费者（备用）
│   └── CustomCrawlerRequestConsumer# 自定义抓取消费者
├── controller/                     # HTTP 接口层
│   ├── CrawlerController           # /crawl/api/** 爬虫任务提交
│   ├── ScreenshotController        # /screenshot/** 截图任务提交
│   ├── SitemapController           # Sitemap 相关接口
│   ├── ProxyController             # 代理管理接口
│   ├── SiteConfigController        # 站点配置管理
│   ├── SiteStrategyController      # 站点策略管理
│   └── StatMonitorController       # 统计监控接口
├── crane/                          # 定时任务（Crane 调度）
│   ├── ChromeUtilCrane             # Chrome 工具定时维护
│   ├── ProxySetUpdateCrane         # 代理池定时刷新
│   └── SiteConfigCrane             # 站点配置定时同步
├── dao/                            # 数据访问层
│   ├── AllSiteInfoMapper           # 全量站点信息表
│   ├── RobotsTxtMapper             # robots.txt 缓存表
│   └── SiteConfigMapper            # 站点配置表
├── downloader/                     # 下载器（多种实现）
│   ├── WebDownloader               # 标准 HTTP 下载
│   ├── JSDownloader / JSDownloader2# JS 渲染下载
│   ├── PlaywrightDownLoader        # Playwright 浏览器下载
│   ├── SitemapDownloader           # Sitemap 专用下载
│   ├── CamoufoxDownloader          # 指纹浏览器（Camoufox）下载
│   ├── AntibotDownloader           # Antibot 反爬下载
│   └── FileDownloader              # 文件类资源下载
├── model/                          # 数据模型
├── rpc/                            # Thrift RPC 实现
│   └── CrawlerRpcServiceImpl       # 实现 client 模块接口
├── screenshot/                     # 截图抓取子模块
│   ├── ScreenshotService           # 截图业务编排（调 Playwright → S3 → MQ）
│   ├── ScreenshotPublishService    # 截图结果 MQ 发布
│   └── model/                      # 截图请求/结果模型
├── service/                        # 核心业务服务
│   ├── CrawlerService              # 通用爬虫下载调度（Rhino 线程池，支持重试/灰度）
│   ├── ScreenshotPlaywrightService # Playwright 截图具体执行（最核心截图服务）
│   ├── S3Service                   # S3 文件上传/下载/分片
│   ├── HbaseService                # HBase 读写
│   ├── RedisService                # Redis 去重/缓存
│   ├── CellarService               # Cellar（Tair）缓存
│   ├── LinkExtractService          # 链接提取
│   ├── SiteConfigService           # 站点配置查询
│   ├── SiteStrategyService         # 站点策略（Redis 布隆过滤器）
│   ├── PlaywrightService           # Playwright 实例管理
│   ├── CacheCenterClient           # JS 缓存中心客户端
│   ├── CrawlResultPublishService   # 通用抓取结果 MQ 发布
│   ├── DocCrawlResultPublishService# 文档抓取结果 MQ 发布
│   └── TimeCrawlResultPublishService# 时序抓取结果 MQ 发布
│   └── proxy/
│       ├── ProxySetService         # 代理集合服务
│       ├── ProxySetProvider        # 代理获取提供者
│       └── LionFileConfigService   # Lion 文件配置服务（JS 灰度站点文件）
├── sitemap/                        # Sitemap 发现子模块
│   ├── SitemapService              # Sitemap 抓取核心逻辑（robots.txt → sitemap 解析 → 投递）
│   ├── SitemapPublishService       # Sitemap URL 结果 MQ 发布
│   ├── SearchApiService            # 搜索 API（百优/Bing）调用
│   └── WebArchiveService           # Web Archive 辅助服务
└── util/                           # 工具类
    ├── UrlUtils                    # URL 解析/顶级域名提取
    ├── FilterUtils                 # 黑名单/正则过滤
    ├── ThreadPoolUtils             # 线程池工具
    ├── MonitorUtils                # CAT 监控工具封装
    └── ...
```

## 核心功能

| 功能 | 关键类/方法 | 说明 |
|------|-------------|------|
| 通用网页抓取 | `CrawlerRequestConsumer#receive` → `CrawlerService#download` | 消费 MQ，灰度选择 Downloader，执行下载 |
| Sitemap 发现 | `SitemapRequestConsumer#receive` → `SitemapService#download` | 解析 robots.txt + sitemap.xml，提取 URL 投递 MQ |
| 截图+HTML 抓取 | `ScreenshotRequestConsumer#receive` → `ScreenshotService#submit` → `ScreenshotPlaywrightService#crawlScreenshot` | 使用 Playwright 渲染，上传截图+HTML 到 S3，结果发 MQ |
| S3 文件上传 | `S3Service#uploadXxx` | 支持普通上传和大文件分片上传 |
| 代理灰度 | `CrawlerRequestConsumer#isCamoufoxGray/isAntibotGray/isJsSpiderGray` | 按域名/比例灰度到不同 Downloader 集群 |
| 站点策略 | `SiteStrategyService#getRedisConfigWithBloom` | Redis 布隆过滤器加速站点策略查询 |

## 对外接口

| 接口 | 类型 | 路径/Topic | 说明 |
|------|------|-----------|------|
| 提交爬虫任务 | HTTP POST | `/crawl/api/js/submit` | 接收 JS 抓取任务（转发来自其他节点） |
| 提交截图任务 | HTTP POST | `/screenshot/submitScreenShotAndSend` | 接收截图任务（转发来自其他节点） |
| 站点配置管理 | HTTP | `/site/config/**` | 增删改查站点抓取配置 |
| 统计监控 | HTTP | `/stat/**` | 查询抓取统计/流量数据 |
| RPC 接口 | Thrift | - | `CrawlerRpcServiceImpl#crawlTask` |
| MQ 消费-通用抓取 | Mafka Consumer | `@MdpMafkaMsgReceive` | 消费通用抓取任务 |
| MQ 消费-Sitemap | Mafka Consumer | `@MdpMafkaMsgReceive` | 消费 Sitemap 抓取任务 |
| MQ 消费-截图 | Mafka Consumer | `@MdpMafkaMsgReceive` | 消费截图抓取任务 |

## 线程池设计

| 线程池 | 位置 | 大小（Lion 动态配置） | 用途 |
|--------|------|----------------------|------|
| 通用爬虫线程池 | `CrawlerRequestConsumer.executor` | `crawler_thread_num` | 并发执行通用抓取 |
| Sitemap 线程池 | `SitemapRequestConsumer.EXECUTOR` | `sitemap_thread_num` | 并发执行 Sitemap 下载 |
| 截图线程池 | `ScreenshotRequestConsumer.EXECUTOR` | `screenshot_thread_num` | 并发执行截图抓取 |
| 网络下载线程池 | `CrawlerService.NET_DOWNLOAD_TASK_THREAD_POOL` | 固定 400 | Rhino 管理的下载执行池 |

## 依赖关系

- 依赖模块：`llm-crawler-client`（使用其 Request/Response 模型）
- 外部依赖：Mafka / Squirrel(Redis) / S3 / Zebra(MySQL) / HBase / Lion / CAT / Rhino
