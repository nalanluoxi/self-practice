# 策略评估 Agent 与知识库系统

> 分支：`dev/20260611-llm-spider-agent-lyl`
> 核心目标：在爬虫服务内嵌一个 LLM 驱动的 Agent，自动对域名跑多策略评估、决策最优爬取策略，并支持知识库积累经验。

---

## 一、整体架构

```
浏览器（agent-chat.html）
    │  SSE 流式 / REST
    ▼
CrawlerController（/crawl/api/strategy/agent/stream）
    │
    ▼
StrategyAgentService          ← 核心 Agent：多轮评估 + LLM 决策
    ├── LlmClient              ← 调内部 LLM（LongCat-2.0-Preview）
    ├── CrawlerService         ← 并发抓取各策略
    ├── SiteStrategyService    ← 结果写入 Redis
    ├── KnowledgeBaseService   ← 知识库检索 / 存储
    └── KbQueryExpander        ← LLM 扩写检索词

ChatHistoryService             ← 对话历史（CacheCenter 存储）
KnowledgeBaseService           ← 知识库（CacheCenter 存储）
```

---

## 二、前端页面

**访问地址：** `http://localhost:8080/agent-chat.html`（服务启动在 8080 端口）

**页面结构：**
- 左侧侧边栏：会话列表，支持新建/置顶/重命名/删除
- 右侧主区：对话区 + 输入框

**输入框支持三类输入：**

```
1. 贴链接 → 触发策略评估流程（自动跑 12 种策略对比）
   例：帮我分析下 https://example.com

2. 自由对话 → 追问上轮结果、解释概念、分析原因
   例：上一个站点 UA 选了哪个？为什么？

3. 存知识库 → 把当前结论持久化到知识库
   例：把刚才 cf 那段记一下 / 存到 KB
```

**输入框选项：**
- `domain`（可选）：手动指定域名
- `评估完写入 Redis`：勾选后评估完直接 apply 到线上
- `开启知识库检索`：勾选后评估过程中检索知识库辅助 LLM 诊断

**另有两个页面：**
- `kb.html`：知识库管理页，支持查看/搜索/删除知识库条目，首页右上角 📚 跳转
- `strategy.html`：原有爬取策略配置页

---

## 三、StrategyAgentService — 多轮评估核心

**文件：** `StrategyAgentService.java`

### 3.1 支持的 12 种爬取策略

| 策略名 | API 路径 | 浏览器 | 额外等待 |
|--------|---------|--------|---------|
| `static` | `/crawl/api/static/mock` | chrome | 无 |
| `static_house` | `/crawl/api/statichouse/mock` | chrome | 无 |
| `js` | `/crawl/api/js/mock` | chrome | 无 |
| `js_aload` | `/crawl/api/js/mock` | chrome | aload |
| `js_house` | `/crawl/api/jshouse/mock` | chrome | 无 |
| `js_house_aload` | `/crawl/api/jshouse/mock` | chrome | aload |
| `js_house_firefox` | `/crawl/api/jshouse/mock` | firefox | 无 |
| `js_house_firefox_aload` | `/crawl/api/jshouse/mock` | firefox | aload |
| `js_house_full_load` | `/crawl/api/jshouseload/mock` | chrome | 无 |
| `js_house_full_load_firefox` | `/crawl/api/jshouseload/mock` | firefox | 无 |
| `camoufox` | `/crawl/api/camoufox/mock` | chrome | 无 |
| `antibot` | `/crawl/api/antibot/mock` | chrome | 无 |

策略按「轻→重」排序，优先推荐最轻的有效策略。

### 3.2 多轮评估流程

```
evaluate(req)
    │
    ├─ 1. get_memory：查 Redis 是否有该域名的历史记忆（TTL 30天）
    │
    ├─ 2. 多轮循环（默认最多 5 轮）
    │       │
    │       ├─ 第 1~2 轮：全量测 12 种策略（并发抓取）
    │       ├─ 第 3+ 轮：只测 uncertain + currentBest
    │       │
    │       ├─ crawlParallel()：CompletableFuture 并发抓取
    │       ├─ judgeOne()：每个策略判定 effective/ineffective/uncertain
    │       │       ├─ HTTP 状态码判断（200/403/429...）
    │       │       └─ judgeQualityByLlm()：LLM 判断 HTML 是否为正常内容
    │       │
    │       ├─ recompute()：更新每个策略状态
    │       └─ 收敛检查：
    │               ├─ 无 uncertain 策略 → 收敛
    │               └─ 连续两轮结论一致 → 收敛
    │
    ├─ 3. diagnose_failure：失败策略查知识库 + LLM 诊断原因
    │       └─ kbQueryExpander.expand() → knowledgeBaseService.search()
    │
    ├─ 4. compare_quality：多个有效策略时精细对比 HTML 质量
    │       └─ compareQuality()：LLM 对比两段 HTML 选更完整的
    │
    ├─ 5. apply_strategy：勾选写入 Redis 时调 siteStrategyService.saveRedisConfig()
    │
    └─ 6. save_memory：把结论存入 Redis（agent_strategy_memory，TTL 30天）
```

### 3.3 策略状态机

每个策略维护一个 `StrategyState`，状态流转：

```
uncertain（初始）
    ├─ 多轮 effective 证据    → confirmed_effective
    ├─ 多轮 ineffective 证据  → confirmed_ineffective
    └─ 证据不足（URL 用完）   → majorityVote() 多数决
```

### 3.4 LLM 调用的 Prompt

**HTML 质量判断（judgeQualityByLlm）：**
```
你是一个网页质量判断专家。判断以下HTML是否为正常内容页（非反爬/空壳/挑战页）。
输出严格的 JSON：{"result": "effective"/"ineffective"/"uncertain", "reason": "..."}
```

**策略对比（compareQuality）：**
```
对比两个策略抓取的HTML，选出内容更完整的一个。
输出 JSON：{"winner": "strategyA"/"strategyB", "reason": "..."}
```

**结果总结（summarizeForChat）：**
```
你是爬虫策略专家。下面是策略评估 Agent 对某域名的完整评估结果 JSON，请用中文 markdown 输出一段简洁的总结，包含：
1) 推荐的最佳策略及置信度，一句话说明理由
2) 用表格列出每个策略的判定，表头：策略 | 判定 | 关键原因
3) 如果有 applied=true，提示用户已写入 Redis 生效；否则提示需手动 apply
```

---

## 四、LlmClient — LLM 调用客户端

**文件：** `LlmClient.java`

| 字段 | 值 |
|------|-----|
| 接口地址 | `https://aigc.sankuai.com/v1/openai/native/chat/completions` |
| 模型 | `LongCat-2.0-Preview` |
| 连接超时 | 10s |
| 读取超时 | 60s |

**主要方法：**
- `chat(systemPrompt, userMessage)` — 单轮对话
- `chatWithHistory(messages)` — 带历史的多轮对话

---

## 五、ChatHistoryService — 对话历史

**文件：** `ChatHistoryService.java`，存储在 CacheCenter（seed：`agent_chat`）

**数据模型：**

```
ChatConversation
    ├─ id / title / domain
    ├─ createdAt / updatedAt / pinned
    └─ turns: List<ChatTurn>
            ├─ role（user/assistant）
            ├─ text（对话文本）
            ├─ finalJson（评估结果 JSON）
            └─ progressJson（流式过程快照）
```

**TTL 策略：**
| 类型 | TTL |
|------|-----|
| 普通会话 | 4 小时 |
| 置顶会话 | 7 天（每次写续期） |
| 会话索引 | 7 天 |

**关键方法：**
- `create()` — 新建会话，生成 UUID
- `appendTurn(id, turn)` — 追加一轮对话，续期 TTL
- `setPinned(id, bool)` — 置顶/取消置顶
- `list()` — 列出所有会话（过滤已过期的"幽灵 meta"）
- `delete(id)` — 删除会话并从索引移除

---

## 六、KnowledgeBaseService — 知识库

**文件：** `KnowledgeBaseService.java`，存储在 CacheCenter（seed：`kb_spider`）

**数据模型：**

```
Doc（完整文档）
    ├─ id / title / content / tags
    └─ createdAt / updatedAt

Entry（索引摘要，用于检索打分）
    ├─ id / title / summary（content 截断 200 字）
    └─ tags
```

**检索算法：TF-IDF 字符级 n-gram 打分**

```
search(query, topK):
    1. 对 query 分词（字符 n-gram）
    2. 计算每个 Entry 的 summary 与 query 的 TF-IDF 相似度
    3. 返回 top-K，附带 snippet（命中片段高亮）
```

**RAG 上下文构建：**
```java
buildRagContext(query, topK, maxChars):
    → 检索 top-K 条
    → 拼成：[1] title\n内容\n\n[2] title\n内容...
    → 截断到 maxChars 字符
```

**TTL：** 1 年，每次写操作续期（`upsert` 时重置）

---

## 七、KbQueryExpander — 知识库检索词扩写

**文件：** `KbQueryExpander.java`

**作用：** 在查知识库前，用 LLM 把原始问题扩展成多个语义相关的检索词，提升召回率。

**Prompt：**
```
你是知识库检索词生成器。用户给一段问题，你输出 3~6 个最可能命中相关知识的检索词。
要求：包含原词、同义词、上位/下位概念、领域近义术语、中英对照；
只输出严格合法的 JSON 数组，例 ["早餐","早饭","晨食","breakfast"]
```

**示例：**
```
输入：cloudflare 反爬怎么绕
输出：["cloudflare", "CF绕过", "反爬", "挑战页", "bypass", "challenge page"]
```

短纯 ASCII 输入（< 4 字符）跳过扩写，直接返回原词。

---

## 八、完整调用链（流式 SSE 路径）

```
POST /crawl/api/strategy/agent/stream
    │
    ▼
CrawlerController:962（produces="text/event-stream"）
    │
    ▼
StrategyAgentService.evaluate(req, listener)
    │
    ├─ listener.onEvent("start", ...)          → data: {type:start, ...}
    ├─ listener.onEvent("memory_hit", ...)     → data: {type:memory_hit, ...}
    ├─ listener.onEvent("round_start", ...)    → data: {type:round_start, ...}
    ├─ listener.onEvent("crawl_done", ...)     → data: {type:crawl_done, ...}
    ├─ listener.onEvent("judge_done", ...)     → data: {type:judge_done, ...}
    ├─ listener.onEvent("kb_queries", ...)     → data: {type:kb_queries, ...}
    ├─ listener.onEvent("kb_hit", ...)         → data: {type:kb_hit, ...}
    ├─ listener.onEvent("round_end", ...)      → data: {type:round_end, ...}
    ├─ listener.onEvent("converged", ...)      → data: {type:converged, ...}
    ├─ listener.onEvent("applied", ...)        → data: {type:applied, ...}
    └─ listener.onEvent("final", ...)          → data: {type:final, result:...}

前端 EventSource 接收，renderEvent() 按 type 渲染不同样式的卡片
```

---

## 九、HTTP 接口汇总

| 接口 | 方法 | 说明 |
|------|------|------|
| `/crawl/api/strategy/agent` | POST | 同步评估（等全部跑完返回） |
| `/crawl/api/strategy/agent/stream` | POST | 流式评估（SSE，实时推进度） |
| `/crawl/api/chat/create` | POST | 新建会话 |
| `/crawl/api/chat/list` | GET | 获取会话列表 |
| `/crawl/api/chat/{id}` | GET | 获取单个会话详情 |
| `/crawl/api/chat/{id}/append` | POST | 追加一轮对话 |
| `/crawl/api/chat/{id}/pin` | POST | 置顶/取消置顶 |
| `/crawl/api/chat/{id}/rename` | POST | 重命名会话 |
| `/crawl/api/chat/{id}/dispatch/stream` | POST | 带历史的流式对话（SSE） |
| `/crawl/api/chat/{id}/kb/save/stream` | POST | 把当前结论存入知识库（SSE） |
| `/crawl/api/kb/add` | POST | 手动添加知识库条目 |
| `/crawl/api/kb/search` | GET | 搜索知识库 |
| `/crawl/api/kb/list` | GET | 列出所有知识库条目 |
| `/crawl/api/kb/{id}` | GET | 获取知识库条目详情 |

---

## 十、爬取策略对比（strategy/compare）

**接口：** `POST /crawl/api/strategy/compare`，实现在 `CrawlerService.java:1164`

### 与 Agent 的区别

| | strategy/compare | strategy/agent（Agent） |
|--|-----------------|----------------------|
| 判定方式 | **纯数值**：HTTP 状态码 + 内容长度比值 | **LLM 语义判断**：理解 HTML 是否为正常内容页 |
| 速度 | 快（无 LLM 调用） | 慢（每个策略都调 LLM） |
| 准确度 | 低（只看长度，反爬空壳也可能 200+长内容） | 高（能识别 CF 挑战页、空壳页） |
| 适用场景 | 快速初筛、批量扫描 | 深度分析单个域名 |

---

### 单 URL 策略对比流程（executeStrategyCompare）

```
executeStrategyCompare(url, domain, rounds, timeout, applyStrategy)
    │
    ├─ 1. 遍历 14 种策略，每种策略执行 rounds 轮抓取
    │       └─ executeSingleStrategy(url, strategyDef, timeout)
    │               ├─ static/static_house → download(request, proxyWay)
    │               ├─ js/js_house/js_house_firefox → runJsStrategy(...)
    │               ├─ js_house_full_load → runJsStrategy(proxyWay=5)
    │               ├─ camoufox → CamoufoxDownloader
    │               └─ antibot → AntibotDownloader
    │
    ├─ 2. 找基准最大内容长度（effectiveMax）
    │       └─ 所有 httpCode=200 轮次中 contentLen 最大值，兜底 5000
    │
    ├─ 3. 判定每个策略是否「有效」
    │       ├─ 有效条件：httpCode=200 且 contentLen/effectiveMax > 0.5（LEN_RATIO_THRESHOLD）
    │       ├─ rounds ≤ 3：要求全部轮次通过
    │       └─ rounds > 3：要求 ≥ 70% 轮次通过
    │
    ├─ 4. 按「策略轻重顺序」选出第一个有效的 → bestStrategy
    │       兜底1：无有效策略但有 contentLen≥5000 → 选最大内容的
    │       兜底2：有 403 → bestStrategy="403"
    │       兜底3：全失败 → bestStrategy="failed"/"error"
    │
    └─ 5. applyStrategy=true → siteStrategyService.saveRedisConfig() 写入 Redis
```

**关键常量：**
```java
LEN_RATIO_THRESHOLD = 0.5          // 内容长度需达到最大值的 50%
MIN_CONTENT_LEN_THRESHOLD = 5000   // 兜底时最小内容长度阈值（5KB）
```

---

### 批量 URL 策略对比（executeBatchStrategyCompare）

对多个 URL 分别跑单 URL 对比，然后**多数投票**选出站点级最优策略：

```
executeBatchStrategyCompare(urls, domain, rounds, ...)
    │
    ├─ 1. 对每个 URL 独立跑 executeStrategyCompare（不 apply）
    │
    ├─ 2. 收集每个 URL 的 bestStrategy，过滤 403/failed/error
    │
    ├─ 3. 按策略名分组计票（voteMap）
    │
    ├─ 4. 选票数最多的策略
    │       票数相同时 → 按策略成本顺序，选成本「更高」的
    │       （成本高的策略能成功说明它必要，优先选）
    │
    └─ 5. applyStrategy=true → 写入 Redis
```

**策略成本顺序（从轻到重）：**
```
static → static_house → js → js_aload → js_house → js_house_aload
→ js_house_firefox → js_house_firefox_aload → js_house_full_load
→ js_house_full_load_firefox → camoufox → antibot
```

---

### 为什么 compare 用「成本最高」而不是「最轻」来打破平票

单 URL 策略对比选**最轻有效策略**（第一个 effective）；
批量对比打破平票时选**成本最高**的，逻辑是：多个 URL 都需要重策略才能抓到，说明该站点确实对低成本策略有防护，选高成本策略更稳。

---

## 十一、新增文件汇总

| 文件 | 类型 | 说明 |
|------|------|------|
| `StrategyAgentService.java` | **新增** | Agent 核心：多轮评估、LLM 决策、记忆管理（920行） |
| `LlmClient.java` | **新增** | LLM HTTP 客户端，调内部 LongCat 模型 |
| `ChatHistoryService.java` | **新增** | 对话历史，CacheCenter 存储，TTL 管理 |
| `KnowledgeBaseService.java` | **新增** | 知识库 CRUD + TF-IDF 检索 |
| `KbQueryExpander.java` | **新增** | LLM 检索词扩写，提升知识库召回 |
| `CacheCenterClient.java` | **新增** | CacheCenter HTTP 客户端封装 |
| `CrawlerNodeIpPool.java` | **新增** | 爬虫节点 IP 池，轮询分配 |
| `agent-chat.html` | **新增** | Agent 对话前端页面，内置 Markdown 渲染 |
| `kb.html` | **新增** | 知识库管理前端页面 |
| `CrawlerController.java` | 修改 | 新增 Agent、会话、知识库相关接口 |