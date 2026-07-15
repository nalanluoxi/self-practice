# 模块：llm-crawler-client

> 最后更新：2026-05-27

## 模块职责

Client 模块是对外发布的 SDK jar 包，仅包含接口定义和数据模型，供上游调用方（其他服务）通过 Thrift RPC 调用爬虫服务。本身不含任何业务逻辑。

## 包结构

```
llm-crawler-client/src/main/java/com/sankuai/llm/spider/crawler/
├── request/
│   ├── BaseRequest.java          # 请求基类
│   ├── CrawlerRequest.java       # 通用爬虫请求（含 url/domain/downloader/proxy 等字段）
│   └── sitemap/
│       ├── SitemapRequest.java   # Sitemap 抓取请求
│       └── SitemapType.java      # Sitemap 类型枚举
├── response/
│   └── CrawlerResponse.java      # 爬虫响应体
└── rpc/
    └── CrawlerRpcService.java    # Thrift RPC 服务接口（@ThriftService）
```

## 核心功能

| 功能 | 关键类/方法 | 说明 |
|------|-------------|------|
| RPC 接口定义 | `CrawlerRpcService#crawlTask` | Thrift 接口，接收抓取任务 |
| 通用请求模型 | `CrawlerRequest` | 包含 url、domain、downloader 类型、proxy、unit、group 等 |
| Sitemap 请求 | `SitemapRequest` | 继承基类，增加 type、ext（dispatchTs、batchId）字段 |

## 对外接口

| 接口 | 类型 | 说明 |
|------|------|------|
| `CrawlerRpcService#crawlTask` | Thrift RPC | 发送抓取任务到 Server |

## 依赖关系

- 被依赖：`llm-crawler-server`（实现了 `CrawlerRpcService`），以及所有外部调用方
- 依赖模块：无业务依赖，仅依赖 Lombok / Thrift SDK / Rhino / Lion
