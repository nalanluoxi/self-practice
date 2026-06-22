# AI开发面试题：Agent + RAG 系统

> 面向Java后端开发者，结合Spring AI项目实践，覆盖RAG系统与Agent开发核心考点

---

## 一、RAG 系统基础

### 1.1 什么是 RAG？它解决了什么问题？

**RAG（Retrieval-Augmented Generation，检索增强生成）** 是一种将外部知识检索与大语言模型生成能力结合的技术架构。

**解决的核心问题：**

| 问题 | 说明 |
|------|------|
| 知识截止问题 | LLM 训练数据有截止日期，RAG 可注入最新知识 |
| 幻觉问题 | LLM 倾向于编造答案，RAG 提供事实依据，减少幻觉 |
| 私域知识问题 | 企业内部文档无法训练到公开模型中，RAG 可动态检索 |
| 上下文窗口限制 | 不能把所有文档塞入 Prompt，RAG 只检索相关片段 |
| 成本问题 | 微调成本极高，RAG 是轻量级的知识注入方式 |

---

### 1.2 RAG 完整流程

```
用户提问
   │
   ▼
[意图识别] → 判断是否需要检索、检索哪个知识库
   │
   ▼
[提示词重写] → 将口语化问题改写为检索友好的语义查询
   │
   ▼
[双路检索]
   ├── 意图检索（基于意图向量，精准匹配）
   └── 全局向量检索（基于问题向量，覆盖面广）
   │
   ▼
[召回结果合并去重]
   │
   ▼
[Rerank 精排] → Cross-Encoder 对召回结果重排序
   │
   ▼
[构造 Prompt] → 将 Top-K 片段拼入系统提示词
   │
   ▼
[LLM 生成回答]
```

---

### 1.3 RAG vs Fine-tuning 对比

| 维度 | RAG | Fine-tuning |
|------|-----|-------------|
| 知识更新 | 实时，更新向量库即可 | 需重新训练，周期长 |
| 成本 | 低（推理+向量检索） | 高（GPU训练资源） |
| 可解释性 | 高（可追溯来源文档） | 低（黑盒） |
| 知识边界 | 清晰（依赖入库文档） | 模糊（混入预训练知识） |
| 适用场景 | 私域知识问答、文档检索 | 风格迁移、专业领域语言适配 |
| 幻觉控制 | 较好（有检索依据） | 较差（知识混淆风险高） |
| 冷启动 | 快 | 慢 |

**结论：** 企业私域知识问答场景，优先选 RAG；需要模型掌握特定语言风格或专业术语时，可考虑 Fine-tuning 或 RAG + Fine-tuning 结合。

---

### 1.4 向量检索原理

**核心思路：** 将文本映射为高维向量（Embedding），通过计算向量间相似度实现语义检索。

**相似度计算方式：**

| 方法 | 公式特点 | 适用场景 |
|------|----------|----------|
| 余弦相似度 | 衡量方向夹角，忽略模长 | 文本语义相似（最常用） |
| 内积（Dot Product） | 方向+模长都考虑 | 归一化向量时等价余弦 |
| 欧氏距离 | L2距离，衡量空间距离 | 图像特征等绝对位置场景 |

**索引算法：**

- **HNSW（Hierarchical Navigable Small World）**：图结构，查询快（O(log N)），内存占用大，pgvector/Milvus 均支持
- **IVF（Inverted File Index）**：聚类分桶，先找最近的桶再搜索，速度快但精度略低
- **Flat（暴力检索）**：精度最高，数据量大时慢，适合小规模或离线场景

**pgvector 示例：**

```sql
-- 创建向量索引（HNSW）
CREATE INDEX ON documents USING hnsw (embedding vector_cosine_ops);

-- 相似度查询，取 Top-5
SELECT id, content, 1 - (embedding <=> '[0.1, 0.2, ...]') AS similarity
FROM documents
ORDER BY embedding <=> '[0.1, 0.2, ...]'
LIMIT 5;
```

---

## 二、知识入库（Ingestion Pipeline）

### 2.1 完整入库流程

```
原始文档（PDF/Word/MD/HTML）
   │
   ▼
[MD5 去重] → 文件哈希比对，跳过已入库文档
   │
   ▼
[Apache Tika 解析] → 提取纯文本
   │
   ▼
[Chunking 分块] → 固定大小512 token + 50 token overlap
   │
   ▼
[Embedding 生成] → bge-large-zh / text-embedding-v3
   │
   ▼
[异步批量入库] → 写入 pgvector / Milvus
   │
   ▼
[定时任务兜底] → 扫描入库失败记录，补偿重试
```

---

### 2.2 文档解析：Apache Tika

**为什么选 Tika？**
- 支持 PDF、Word、Excel、HTML、Markdown 等 200+ 格式
- 自动检测文件类型（MIME Type Detection）
- Java 生态，依赖简单

**Spring AI 中使用 Tika：**

```java
// pom.xml 引入 spring-ai-tika-document-reader

TikaDocumentReader reader = new TikaDocumentReader(resource);
List<Document> documents = reader.get();
```

**常见坑：**
- PDF 扫描件（图片型PDF）需 OCR，Tika 无法直接提取文字
- 表格内容提取后结构丢失，需额外处理

---

### 2.3 分块策略（Chunking）对比

| 策略 | 描述 | 优点 | 缺点 | 适用场景 |
|------|------|------|------|----------|
| 固定大小分块 | 按 token 数切分（如512） | 简单、均匀 | 可能截断语义 | 通用文档 |
| 句子/段落分块 | 按句号/换行切分 | 语义完整 | 块大小不均匀 | 结构化文档 |
| 递归字符分块 | 按段落→句子→词语递归切分 | 尽量保留语义 | 实现复杂 | 代码、长文章 |
| 语义分块 | 用模型判断语义边界 | 质量最高 | 成本高，慢 | 高质量知识库 |
| 滑动窗口分块 | 固定大小+Overlap | 上下文连续 | 存储冗余 | 问答类场景 |

**本项目方案：固定大小 512 token + 50 token Overlap**

---

### 2.4 Overlap（重叠区）的作用

**问题背景：** 固定大小切分可能将一句完整的话切断，导致两个 Chunk 分别只有半句话，检索时语义缺失。

**Overlap 解决方案：** 相邻两个 Chunk 共享 50 token 的内容。

```
Chunk 1: [token 1   ~ token 512]
Chunk 2: [token 463 ~ token 974]  ← 前50个token与Chunk1重叠
Chunk 3: [token 925 ~ token 1436] ← 前50个token与Chunk2重叠
```

**效果：**
- 保证跨块边界的语义连续性
- 代价：存储量增加约 10%（50/512 ≈ 10%），可接受

---

### 2.5 Embedding 模型选型

| 模型 | 维度 | 特点 | 适用 |
|------|------|------|------|
| bge-large-zh | 1024 | 中文语义最强，MTEB中文榜头部 | 中文知识库（本项目本地开发） |
| bge-m3 | 1024 | 多语言，支持稠密+稀疏+多向量 | 多语言混合场景 |
| text-embedding-v3 | 1536/3072 | 阿里通义，中英双语均衡 | 生产环境，API调用 |
| text-embedding-ada-002 | 1536 | OpenAI，英文为主 | 英文场景 |

**选型原则：**
1. 中文场景优先 bge 系列
2. 生产环境考虑 API 稳定性和吞吐量
3. Embedding 维度影响向量库存储和检索性能，不必盲目追求高维度

---

### 2.6 MD5 去重设计

**目的：** 防止同一文档重复入库，浪费存储和 Embedding API 调用费用。

**实现：**

```java
// 计算文件 MD5
String md5 = DigestUtils.md5DigestAsHex(fileBytes);

// 查库：是否已存在该 MD5
boolean exists = documentRepository.existsByMd5(md5);
if (exists) {
    log.info("文档已存在，跳过入库: {}", md5);
    return;
}

// 入库后保存 MD5 记录
documentRepository.save(new DocumentRecord(md5, fileName, ...));
```

**扩展：** 如果文档支持增量更新（内容变更），可改为"MD5变更则删旧入新"策略。

---

### 2.7 异步入库 + 定时任务兜底

**为什么要异步：** Embedding 生成耗时（每批次数百毫秒），同步入库会阻塞上传接口，用户体验差。

**设计方案：**

```
上传接口
   │
   ▼
将文档记录写入 DB（状态=PENDING）
立即返回 202 Accepted
   │
异步线程池
   │
   ▼
读取 PENDING 记录 → Tika解析 → Chunking → Embedding → 入向量库
   │
   ▼
更新状态为 DONE / FAILED
   │
定时任务（每5分钟）
   │
   ▼
扫描 FAILED 或超时 PENDING 记录 → 重试 → 超过3次标记为 DEAD
```

**Spring 实现片段：**

```java
// 异步入库
@Async("knowledgeExecutor")
public void asyncIngest(Long documentId) {
    // 解析 → 分块 → Embedding → 入库
}

// 定时兜底
@Scheduled(fixedDelay = 5 * 60 * 1000)
public void retryFailedIngestions() {
    List<DocumentRecord> failedList = documentRepository
        .findByStatusAndRetryCountLessThan(Status.FAILED, 3);
    failedList.forEach(doc -> asyncIngest(doc.getId()));
}
```

**好处：**
- 接口响应快，用户体验好
- 定时任务兜底，保证最终一致性
- 重试次数上限，防止死循环

---

## 三、检索与精排

### 3.1 稠密检索 vs 稀疏检索

| 维度 | 稠密检索（Dense） | 稀疏检索（Sparse） |
|------|------------------|------------------|
| 表示方式 | 高维浮点向量（如1024维） | 高维稀疏向量（词频/TF-IDF/BM25） |
| 代表算法 | HNSW、IVF、Flat | BM25、TF-IDF |
| 优势 | 语义理解强，能处理同义词、近义词 | 精确词匹配，关键词命中率高 |
| 劣势 | 对罕见词、专有名词（如型号、ID）效果差 | 无法理解语义，换个说法就找不到 |
| 典型场景 | 语义问答、概念检索 | 代码搜索、商品ID检索、法规条文检索 |

---

### 3.2 混合检索（Hybrid Search）

**核心思路：** 稠密检索 + 稀疏检索各自召回，通过 RRF（Reciprocal Rank Fusion）或加权融合合并结果。

**RRF 公式：**

```
RRF_score(doc) = Σ 1 / (k + rank_i(doc))
```
其中 k 通常取 60，rank_i 为文档在第 i 路检索中的排名。

**优势：**
- 语义检索兜底同义词/近义词问题
- 词匹配检索兜底专有名词/精确词问题
- 二者互补，召回率显著提升

**Milvus 中的混合检索：**

```java
// Spring AI + Milvus 混合检索示意
SearchRequest request = SearchRequest.builder()
    .query(rewrittenQuery)
    .topK(20)
    .similarityThreshold(0.6)
    .build();

List<Document> results = vectorStore.similaritySearch(request);
```

---

### 3.3 本项目双路并行检索设计

**两路检索的区别：**

| 路 | 检索目标 | 向量来源 | 目的 |
|----|----------|----------|------|
| 意图检索 | 意图向量库 | 意图节点的标准描述向量 | 精准命中意图分类，缩小召回范围 |
| 全局向量检索 | 全量知识块向量库 | 文档 Chunk 的 Embedding | 广泛召回相关知识片段 |

**并行执行（CompletableFuture）：**

```java
CompletableFuture<List<Document>> intentFuture = CompletableFuture
    .supplyAsync(() -> intentSearch(query), searchExecutor);

CompletableFuture<List<Document>> globalFuture = CompletableFuture
    .supplyAsync(() -> globalVectorSearch(query), searchExecutor);

// 等待两路结果
List<Document> intentResults = intentFuture.get(3, TimeUnit.SECONDS);
List<Document> globalResults = globalFuture.get(3, TimeUnit.SECONDS);

// 合并去重（按文档ID去重）
List<Document> merged = merge(intentResults, globalResults);
```

**好处：** 两路检索并行，总耗时 = max(t1, t2)，而非 t1 + t2。

---

### 3.4 Rerank 精排原理

**为什么需要 Rerank：**
向量检索的 Top-20 结果，相似度排名不等于与用户问题的相关性排名。Rerank 用更重量级的模型对候选结果重新打分。

**Bi-Encoder vs Cross-Encoder：**

| 维度 | Bi-Encoder（向量检索） | Cross-Encoder（Rerank） |
|------|----------------------|------------------------|
| 原理 | 问题和文档分别编码，计算余弦 | 问题+文档拼接后一起过模型 |
| 速度 | 快（向量预存，ANN检索） | 慢（每对都要完整推理） |
| 精度 | 中（语义近似） | 高（深度交互注意力） |
| 用途 | 大规模召回（Top-100） | 小规模精排（Top-5~10） |

**典型 Rerank 模型：** `bge-reranker-large`、`bce-reranker-base`

**Spring AI 集成示例：**

```java
// 召回后调用 Rerank API，取 Top-5 精排结果拼入 Prompt
List<Document> reranked = rerankService.rerank(query, merged, 5);
```

---

### 3.5 召回率提升方法

| 方法 | 说明 |
|------|------|
| 提示词重写 | 将口语问题改写为检索友好的语义描述，扩展同义词 |
| HyDE（假设文档扩展） | 先让 LLM 生成一段假设性答案，用答案的向量去检索 |
| 增大 Top-K | 先召回 Top-50，再 Rerank 取 Top-5 |
| 混合检索 | 稠密+稀疏互补 |
| 调低相似度阈值 | 放宽召回门槛，配合 Rerank 过滤 |
| 多查询扩展 | 一个问题生成 3-5 个变体，分别检索后合并 |
| 元数据过滤 | 先按类目/时间等字段过滤，再向量检索（减少噪音） |

---

### 3.6 向量数据库选型

| 数据库 | 特点 | 适用场景 |
|--------|------|----------|
| pgvector | PostgreSQL 扩展，SQL 生态，运维成本低 | 中小规模，已有 PG 的项目（本项目本地开发） |
| Milvus | 分布式，高性能，支持混合检索，云原生 | 大规模生产（本项目生产环境） |
| Weaviate | 内置混合检索，GraphQL API，易上手 | 中等规模，需要混合检索 |
| Qdrant | Rust实现，性能好，支持 payload 过滤 | 高性能要求场景 |
| Chroma | 轻量，Python 生态，本地开发友好 | 原型验证、小规模 |
| Pinecone | 全托管，运维成本极低 | 快速上线，不想维护基础设施 |

**本项目选型逻辑：**
- 本地开发用 pgvector：无需额外服务，复用 PostgreSQL（Supabase 提供）
- 生产环境用 Milvus：支持更大规模、混合检索、水平扩展

---

## 四、对话流水线设计

### 4.1 意图识别

**目的：** 判断用户问题属于哪个业务领域/知识库，决定后续走哪条检索路径，避免全量检索浪费性能。

**常见方案对比：**

| 方案 | 原理 | 优点 | 缺点 |
|------|------|------|------|
| 向量相似度分类 | 问题向量与意图向量库比对 | 无需标注大量样本，灵活扩展 | 边界意图容易混淆 |
| LLM 分类 | 直接让大模型判断意图 | 理解能力强 | 延迟高，成本高 |
| 规则/关键词匹配 | 正则/关键词命中意图 | 速度极快 | 泛化性差，维护成本高 |
| 微调分类模型 | BERT类模型做多分类 | 精准，推理快 | 需要标注数据，迭代周期长 |

**本项目方案：意图向量库（Embedding 相似度）**
- 将意图树中每个节点的标准描述 Embedding 入库
- 用户问题 Embedding 后，与意图向量库做相似度检索
- 取 Top-1 意图节点（相似度需 > 阈值，否则走全局检索）

---

### 4.2 意图树三级结构

```
根意图
├── 一级意图：产品咨询
│   ├── 二级意图：价格查询
│   │   ├── 三级意图：单品价格
│   │   └── 三级意图：套餐价格
│   └── 二级意图：功能介绍
├── 一级意图：售后服务
│   ├── 二级意图：退换货政策
│   └── 二级意图：维修流程
└── 一级意图：技术支持
    └── ...
```

**设计要点：**
- 树结构存 DB，支持动态扩展，不需要重新训练
- 每个节点存储：节点ID、名称、描述、父节点ID、关联知识库ID
- 意图检索时，命中叶子节点后，向上追溯到根路径，确定知识库范围
- 新增意图节点只需填写描述并 Embedding 入库，无需修改代码

---

### 4.3 提示词重写（Query Rewriting）

**问题：** 用户问题往往口语化、省略上下文（如"它的价格是多少？"，"它"指什么？），直接拿去检索效果差。

**重写目标：**
1. 补全指代（根据历史对话补全"它"、"这个"等代词）
2. 关键词扩展（加入同义词、相关词）
3. 语义规范化（口语 → 书面/检索友好表达）

**实现：** 用轻量 LLM（如 gpt-3.5 / qwen-turbo）执行改写，控制延迟。

```java
String systemPrompt = """
    你是一个查询改写助手。根据对话历史，将用户最新问题改写为一个
    独立的、完整的、检索友好的问题。只输出改写后的问题，不要解释。
    """;

String rewrittenQuery = chatModel.call(
    new SystemMessage(systemPrompt),
    new UserMessage("历史：" + history + "\n当前问题：" + userQuery)
);
```

**注意：** 重写本身也有延迟，可与检索并行或用流式实现，避免串行等待。

---

### 4.4 多轮上下文管理与摘要压缩

**问题：** 多轮对话中，历史消息不断累积，超出 LLM 上下文窗口（如 4K/8K token）。

**解决方案：滑动窗口 + 摘要压缩**

```
轮次 1-5：直接保留原始消息
轮次 6+：触发压缩
   │
   ▼
将最早的 N 轮消息发给 LLM 生成摘要
   │
   ▼
用摘要替换原始消息（摘要 + 最近 3 轮原始消息）
```

**Spring AI 实现：**

```java
// 使用 MessageChatMemoryAdvisor 管理历史
ChatClient chatClient = ChatClient.builder(chatModel)
    .defaultAdvisors(
        new MessageChatMemoryAdvisor(chatMemory, sessionId, 10) // 保留最近10轮
    )
    .build();

// 超出阈值时触发摘要（自定义 Advisor）
public class SummaryMemoryAdvisor implements RequestResponseAdvisor {
    @Override
    public AdvisedRequest adviseRequest(AdvisedRequest request, Map<String, Object> context) {
        List<Message> history = chatMemory.get(sessionId, Integer.MAX_VALUE);
        if (countTokens(history) > TOKEN_THRESHOLD) {
            String summary = summarize(history.subList(0, history.size() - 3));
            // 用摘要替换旧消息
            rebuildMemory(sessionId, summary, history.subList(history.size() - 3, history.size()));
        }
        return request;
    }
}
```

---

### 4.5 短路路由设计

**目的：** 并非所有问题都需要检索，无效检索浪费延迟和资源。

**短路条件判断：**

```
用户问题
   │
   ├── 闲聊/问候类 → 直接 LLM 回答，跳过检索
   ├── 意图识别置信度极低 → 引导用户澄清
   ├── 命中缓存（相同/相似问题已有答案）→ 直接返回缓存
   └── 需要检索 → 走完整 RAG 流水线
```

**意图置信度阈值示例：**

```java
SearchResult intentResult = intentSearch(query);
double confidence = intentResult.getSimilarity();

if (confidence < 0.5) {
    // 短路：意图不明确，返回澄清引导语
    return "抱歉，我不太理解您的问题，请问您是想咨询...？";
} else if (confidence >= 0.5 && confidence < 0.7) {
    // 降级：走全局向量检索，不做意图检索
    return globalSearch(query);
} else {
    // 正常：走双路检索
    return dualSearch(query);
}
```

---

### 4.6 空检索兜底策略

**问题：** 检索结果为空，或所有结果相似度均低于阈值，此时直接调用 LLM 容易产生幻觉。

**兜底策略：**

| 策略 | 说明 |
|------|------|
| 固定兜底回复 | "暂未找到相关信息，请联系人工客服" |
| LLM 通用回答 | 告知 LLM 无检索结果，让其基于通用知识谨慎回答 |
| 降级到上级意图 | 当前意图无结果，扩展到父意图的知识库重新检索 |
| 引导澄清 | 让 LLM 反问用户，引导补充更多信息 |

**代码示例：**

```java
List<Document> retrieved = rerank(dualSearch(query), 5);

if (retrieved.isEmpty() || retrieved.get(0).getScore() < MIN_SCORE) {
    // 空检索兜底：通知 LLM 没有找到相关文档
    String fallbackPrompt = """
        注意：知识库中未找到与该问题相关的内容。
        请基于你的通用知识谨慎回答，并明确告知用户该答案不来自知识库，
        建议用户联系专业人员确认。
        用户问题：%s
        """.formatted(userQuery);
    return chatModel.call(fallbackPrompt);
}

// 正常流程：拼入检索结果
String context = buildContext(retrieved);
return chatModel.call(buildRagPrompt(context, userQuery));
```

---

<!-- PART1_END -->

## 五、Agent 开发

### 5.1 什么是 Agent？与普通 LLM 应用的区别

**Agent 定义：** 以大语言模型为核心决策引擎，能够感知环境、规划任务、调用工具并持续执行直到目标完成的自主系统。

**核心特征对比：**

| 维度 | 普通 LLM 应用 | Agent |
|------|--------------|-------|
| 状态 | 无状态（单次问答） | 有状态（多轮记忆+工作记忆） |
| 规划能力 | 无 | 能将复杂目标分解为子步骤 |
| 工具调用 | 无（纯文本输出） | 可调用外部工具（搜索/代码执行/API） |
| 执行模式 | 单次推理 | 循环推理直到任务完成 |
| 自主性 | 被动响应 | 主动规划、主动行动 |
| 典型场景 | 问答、摘要 | 自动化流程、多步推理、跨系统操作 |

**本项目 Agent 特征：**
- 有状态：维护多轮对话历史 + RagTrace 上下文
- 工具调用：支持多工具并行调用（@Tool 注解）
- MCP 协议：通过 mcp-server 暴露工具给外部 Agent 使用

---

### 5.2 ReAct 框架（Reasoning + Acting）

**核心思想：** 让 LLM 交替进行"推理"和"行动"，每次行动的观察结果作为下一轮推理的输入。

**循环结构：**

```
Thought（思考）：分析当前状态，决定下一步做什么
   │
   ▼
Action（行动）：调用工具（如搜索、计算、查数据库）
   │
   ▼
Observation（观察）：获取工具执行结果
   │
   ▼
Thought（再次思考）：基于观察结果，判断是否继续行动或输出最终答案
   │
   ▼
（循环直到 Thought 判断任务完成，输出 Final Answer）
```

**ReAct Prompt 示例：**

```
你是一个能够使用工具的助手。按照以下格式回答：

Thought: 你对当前状况的分析
Action: 工具名称
Action Input: {"参数": "值"}
Observation: （工具返回结果，由系统填入）
... （重复 Thought/Action/Observation）
Thought: 我已经知道最终答案了
Final Answer: 最终回答
```

**优势：**
- 推理过程透明，可调试
- 工具调用有依据，减少盲目行动
- 支持多步骤任务分解

---

### 5.3 工具调用（Tool Calling / Function Calling）完整流程

**标准流程：**

```
1. 用户发送请求
       │
       ▼
2. LLM 分析请求，判断需要调用哪个工具
   └─ 输出：{"tool": "searchKnowledge", "args": {"query": "退款政策"}}
       │
       ▼
3. 框架层解析工具调用指令，路由到对应实现
       │
       ▼
4. 工具执行，返回结果
   └─ 返回：{"result": "退款需在7天内申请..."}
       │
       ▼
5. 将工具结果注入对话上下文（ToolMessage）
       │
       ▼
6. LLM 基于工具结果生成最终回答
```

**Spring AI @Tool 注解用法：**

```java
@Component
public class KnowledgeTools {

    @Tool(description = "从知识库检索与问题相关的文档片段，输入为用户问题字符串")
    public String searchKnowledge(String query) {
        List<Document> docs = retrievalService.retrieve(query);
        return docs.stream()
            .map(Document::getContent)
            .collect(Collectors.joining("\n---\n"));
    }

    @Tool(description = "查询用户的订单状态，输入为订单号")
    public String queryOrderStatus(String orderId) {
        return orderService.getStatus(orderId).toString();
    }
}

// 注册工具到 ChatClient
ChatClient chatClient = ChatClient.builder(chatModel)
    .defaultTools(knowledgeTools)  // 自动扫描 @Tool 方法
    .build();
```

**工具描述质量至关重要：** description 写得越清晰，LLM 越能准确判断何时调用、如何传参。

---

### 5.4 MCP 协议（Model Context Protocol）

**定义：** Anthropic 提出的开放协议，定义了 AI 模型与外部工具/数据源之间的标准通信接口，类似 AI 领域的"USB 接口"。

**解决的核心问题：**

| 问题 | Function Calling 的局限 | MCP 的解决方案 |
|------|------------------------|---------------|
| 标准化 | 各家 API 格式不同（OpenAI/Claude/Gemini 各异） | 统一协议，工具实现一次可被任意模型调用 |
| 跨系统工具共享 | 工具与模型强绑定 | 工具作为独立服务，通过协议暴露 |
| 动态工具发现 | 工具列表静态配置 | Client 可动态查询 Server 提供哪些工具 |
| 安全隔离 | 工具代码在主应用内 | 工具运行在独立进程/服务中，权限隔离 |

**MCP vs Function Calling：**

| 维度 | Function Calling | MCP |
|------|-----------------|-----|
| 范围 | 单次请求内的工具声明 | 跨会话、跨系统的工具服务协议 |
| 部署 | 工具代码嵌入应用 | 工具作为独立 MCP Server 部署 |
| 复用 | 工具绑定特定应用 | 任意 MCP Client 均可调用 |
| 发现 | 静态声明 | 动态 list_tools 查询 |
| 适用 | 简单单体应用 | 企业级工具平台、多 Agent 生态 |

**本项目 MCP 实现：**

```
ragent/bootstrap（MCP Client）
      │  HTTP JSON-RPC
      ▼
ragent/mcp-server（MCP Server，端口 9099）
      │
      ├── tool: searchKnowledge（调用 bootstrap 检索 API）
      ├── tool: queryIntentTree（返回意图树结构）
      └── tool: getDocumentById（按 ID 获取文档内容）
```

mcp-server 作为独立 Spring Boot 服务运行，通过 HTTP 与 bootstrap 通信，使外部 Agent（如 Claude Desktop、其他 MCP Client）可直接调用本系统的知识检索能力。

---

### 5.5 工具调用幻觉防护

**幻觉类型：**
1. **参数幻觉**：LLM 编造不存在的参数值（如虚构订单号）
2. **工具幻觉**：调用不存在的工具名
3. **结果幻觉**：忽略工具返回结果，编造答案

**防护手段：**

| 手段 | 说明 | 实现方式 |
|------|------|----------|
| JSON Schema 约束 | 严格定义工具入参格式、类型、枚举值 | @Tool 注解配合参数类型声明 |
| 结构化输出 | 强制模型按 Schema 输出，非合法 JSON 则拒绝 | response_format: json_object |
| temperature=0 | 降低随机性，提高参数生成的确定性 | ChatOptions.temperature(0.0) |
| 工具描述质量 | description 明确输入含义、取值范围、示例 | 编写高质量 @Tool description |
| 输出校验层 | 调用工具前校验参数，调用后校验结果格式 | 自定义 ToolCallInterceptor |
| 拒绝策略 | 参数非法时返回错误，不执行工具，让 LLM 重试 | 工具实现内抛出 IllegalArgumentException |

```java
@Tool(description = "查询订单状态。orderId 必须是以 'ORD' 开头的订单编号，例如 ORD-20240101-001")
public String queryOrderStatus(
    @ToolParam(description = "订单编号，格式：ORD-YYYYMMDD-NNN") String orderId
) {
    // 参数校验：防止 LLM 传入幻觉参数
    if (!orderId.matches("ORD-\\d{8}-\\d{3}")) {
        return "错误：订单号格式不合法，请提供正确格式的订单号";
    }
    return orderService.getStatus(orderId).toString();
}
```

---

### 5.6 Multi-Agent 多智能体协作

**Orchestrator + Worker 模式：**

```
用户请求
    │
    ▼
Orchestrator Agent（主控）
    │ 任务拆分
    ├──────────────────────┬──────────────────────┐
    ▼                      ▼                      ▼
Worker A               Worker B               Worker C
（信息检索）            （数据分析）            （报告撰写）
    │                      │                      │
    └──────────────────────┴──────────────────────┘
                           │ 结果汇聚
                           ▼
                   Orchestrator 整合
                           │
                           ▼
                       最终输出
```

**任务拆分策略：**
- **串行拆分**：任务有依赖关系（A 的输出是 B 的输入），顺序执行
- **并行拆分**：任务独立，CompletableFuture 并发执行，提升效率
- **动态拆分**：Orchestrator 根据执行结果动态决定下一步（类似 ReAct）

**结果汇聚注意事项：**
- 各 Worker 结果可能格式不统一，需规范化后再交给 Orchestrator
- Worker 失败处理：单个 Worker 失败不应阻断整体，需定义降级策略
- 循环检测：防止 Orchestrator ↔ Worker 陷入无限循环（设置最大迭代次数）

---

### 5.7 Agent 可靠性设计

**核心问题：** LLM 推理本身不确定，工具调用可能超时/失败，如何保证 Agent 任务最终完成？

| 可靠性维度 | 设计方案 |
|-----------|----------|
| 重试机制 | 工具调用失败后自动重试（指数退避，最多3次） |
| Fallback 降级 | 主工具不可用时，自动切换到备用工具或简化流程 |
| 超时控制 | 每次工具调用设置超时（如3秒），防止卡死 |
| 幂等性 | 工具调用带唯一 requestId，服务端防重，重试安全 |
| 最大迭代上限 | ReAct 循环设置最大步骤数（如10步），防止无限循环 |
| 状态持久化 | 长任务将每步状态写 DB，断点续跑 |

```java
// Spring AI 中设置 Agent 最大迭代次数
ChatClient chatClient = ChatClient.builder(chatModel)
    .defaultOptions(ChatOptions.builder()
        .maxToolCallRounds(10)  // 最多 10 轮工具调用循环
        .build())
    .build();

// 工具调用超时控制（在工具实现中）
@Tool(description = "...")
public String externalApiCall(String param) {
    return CompletableFuture.supplyAsync(() -> externalService.call(param))
        .get(3, TimeUnit.SECONDS);  // 超时抛出 TimeoutException → LLM 收到错误后可重试
}
```

---

## 六、模型路由与高可用

### 6.1 多模型路由设计

**背景：** 单一模型存在单点故障、限流、成本过高等风险，需要多模型按优先级动态切换。

**候选列表 + 优先级配置（application.yaml）：**

```yaml
ai:
  chat:
    candidates:
      - name: qwen-max
        provider: dashscope
        priority: 1          # 数字越小优先级越高
        enabled: true
      - name: deepseek-v3
        provider: siliconflow
        priority: 2
        enabled: true
      - name: qwen-turbo
        provider: dashscope
        priority: 3
        enabled: true        # 兜底廉价模型
```

**路由决策逻辑（ModelRoutingExecutor）：**

```
按 priority 升序遍历候选模型
    │
    ▼
检查 ModelHealthStore 中该模型的熔断器状态
    ├── OPEN（已熔断）→ 跳过，尝试下一个
    ├── HALF_OPEN（探测中）→ 允许一个请求通过（探测请求）
    └── CLOSED（正常）→ 选中，发起调用
    │
    ▼
调用成功 → 记录成功，熔断器计数重置
调用失败 → 记录失败，累计达阈值则触发熔断
    │
    ▼
所有模型均 OPEN → 抛出 NoAvailableModelException
```

---

### 6.2 三态熔断器在 AI 网关中的应用

**三态转换图：**

```
         失败次数 >= threshold
CLOSED ─────────────────────────► OPEN
  ▲                                  │
  │                                  │ 等待 openDurationMs
  │                                  ▼
  │          探测请求成功         HALF_OPEN
  └──────────────────────────────────┘
                    │
                    │ 探测请求失败
                    ▼
                  OPEN（重置等待计时）
```

**状态说明：**

| 状态 | 含义 | 行为 |
|------|------|------|
| CLOSED | 模型健康，正常服务 | 所有请求正常通过，统计失败次数 |
| OPEN | 模型故障，已熔断 | 请求直接跳过，不发起调用 |
| HALF_OPEN | 等待探测，半开 | 放行一个请求，成功则恢复 CLOSED，失败则回 OPEN |

**本项目关键配置：**

```yaml
ai:
  selection:
    failure-threshold: 5        # 连续失败 5 次触发熔断
    open-duration-ms: 30000     # OPEN 状态持续 30 秒后转 HALF_OPEN
```

**ModelHealthStore 核心逻辑：**

```java
public class ModelHealthStore {
    // key: modelName, value: CircuitBreakerState
    private final ConcurrentHashMap<String, CircuitBreakerState> stateMap = new ConcurrentHashMap<>();

    public boolean isAvailable(String modelName) {
        CircuitBreakerState state = stateMap.get(modelName);
        if (state.getStatus() == OPEN) {
            // 检查是否到达探测时间
            if (System.currentTimeMillis() - state.getOpenTime() >= openDurationMs) {
                state.transitionTo(HALF_OPEN);
                return true;  // 放行探测请求
            }
            return false;  // 仍在 OPEN 期，拒绝
        }
        return true;  // CLOSED 或 HALF_OPEN（已放行一个）
    }

    public void recordSuccess(String modelName) {
        stateMap.get(modelName).transitionTo(CLOSED);
    }

    public void recordFailure(String modelName) {
        CircuitBreakerState state = stateMap.get(modelName);
        state.incrementFailure();
        if (state.getFailureCount() >= failureThreshold) {
            state.transitionTo(OPEN);
        }
    }
}
```

---

### 6.3 三层限流设计

**设计目标：** 保护系统（全局QPS）+ 公平分配资源（用户等级）+ 保护单模型（模型调用限流）。

**三层结构：**

| 层级 | 限流粒度 | 限流目标 | 算法 |
|------|----------|----------|------|
| 系统级（全局） | 整个系统 | 最大并发数 / 总QPS | 令牌桶 / 信号量 |
| 用户级 | 单用户 | 按用户等级差异化限流 | Redis ZSET 滑动窗口 |
| 模型级 | 单模型 | 控制对特定模型的调用频率 | Redis ZSET 滑动窗口 |

**Redis ZSET 滑动窗口限流（@ChatRateLimit 实现）：**

```lua
-- Lua 脚本（原子操作，防并发竞争）
-- KEYS[1]: 限流 key（如 "rate:user:123"）
-- ARGV[1]: 当前时间戳（毫秒）
-- ARGV[2]: 窗口大小（毫秒，如 60000 = 1分钟）
-- ARGV[3]: 最大请求数

local now = tonumber(ARGV[1])
local window = tonumber(ARGV[2])
local limit = tonumber(ARGV[3])
local key = KEYS[1]

-- 删除窗口外的旧记录
redis.call('ZREMRANGEBYSCORE', key, 0, now - window)

-- 统计窗口内的请求数
local count = redis.call('ZCARD', key)

if count >= limit then
    return 0  -- 限流拒绝
end

-- 记录本次请求
redis.call('ZADD', key, now, now .. '-' .. math.random())
redis.call('EXPIRE', key, math.ceil(window / 1000))
return 1  -- 允许通过
```

**用户等级差异化：**

```java
// 根据用户等级获取对应限流配额
int maxRequests = switch (user.getLevel()) {
    case VIP    -> 100;  // VIP 用户：100次/分钟
    case PRO    -> 50;   // 专业版：50次/分钟
    case FREE   -> 10;   // 免费版：10次/分钟
};
```

**超限处理（Pub/Sub 排队）：** 超限请求不直接拒绝，而是发布到 Redis 频道排队，等待配额释放后由 Subscriber 唤醒继续处理。

---

### 6.4 SSE 流式输出原理

**HTTP 长连接（Server-Sent Events）：**

```
客户端                        服务端
   │── GET /chat/stream ────►│
   │◄── HTTP 200 ────────────│
   │    Content-Type:        │
   │    text/event-stream    │
   │◄── data: {"token":"你"}  │  ← 每个 token 立即推送
   │◄── data: {"token":"好"}  │
   │◄── data: {"token":"！"}  │
   │◄── data: [DONE]          │  ← 结束标志
   │── （连接关闭）            │
```

**与 WebSocket 对比：**

| 维度 | SSE | WebSocket |
|------|-----|-----------|
| 方向 | 单向（服务端 → 客户端） | 双向 |
| 协议 | 基于 HTTP | 独立协议（ws://） |
| 重连 | 自动重连（浏览器内置） | 需手动实现 |
| 适用 | 流式推送（AI 输出） | 实时双向通信（聊天室） |

**Spring WebFlux / Flux 实现：**

```java
@GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<ServerSentEvent<String>> streamChat(@RequestParam String query) {
    return chatClient.prompt()
        .user(query)
        .stream()
        .chatResponse()
        .map(response -> {
            String token = response.getResult().getOutput().getContent();
            return ServerSentEvent.<String>builder()
                .data(token)
                .build();
        })
        .doOnComplete(() -> log.info("流式输出完成"));
}
```

---

### 6.5 ProbeStreamBridge：首 Chunk 错误检测

**问题背景：** SSE 流式输出一旦开始（HTTP 200 已发送），就无法再改变状态码。若模型报错（如 API 限流、内容过滤），错误信息会混入正常 token 流，客户端收到脏数据。

**解决方案：缓冲首个 Chunk，检测错误后再决定是否下发**

```
LLM 返回 Flux<Token>
    │
    ▼
ProbeStreamBridge.probe()
    │ 缓冲第一个 chunk（等待 probeTimeoutMs）
    │
    ├── 首 chunk 是错误信号 ──► 抛出异常，HTTP 响应为 500，不开启 SSE
    │
    └── 首 chunk 正常 ─────────► 将缓冲的 chunk + 后续 chunk 一起通过
                                 开启 SSE 连接，正常推送
```

**核心逻辑示意：**

```java
public Flux<String> probe(Flux<String> source) {
    return source
        .publish(shared -> {
            Mono<String> firstChunk = shared.next();
            return firstChunk.flatMapMany(first -> {
                if (isErrorChunk(first)) {
                    return Flux.error(new ModelResponseException(first));
                }
                // 首 chunk 正常，拼回完整流
                return Flux.concat(Flux.just(first), shared);
            });
        });
}
```

**好处：** 保证 SSE 连接建立前完成错误检测，避免向用户输出半截错误内容。

---


## 七、RAG 系统评估

### 7.1 评估三维度概览

RAG 系统的效果评估不能只看"模型回答是否流畅"，需要从三个独立维度评估：

| 维度 | 评估对象 | 核心问题 |
|------|----------|----------|
| 检索质量 | 向量检索 + Rerank 的召回结果 | 找到的文档是否与问题相关？ |
| 生成质量 | LLM 基于检索结果生成的答案 | 回答是否忠实于检索内容？是否有幻觉？ |
| 端到端效果 | 整体问答链路 | 用户的问题最终是否得到了准确、有用的回答？ |

**评估重要性：** 不做评估就无法判断优化方向。例如召回率低 → 优化检索；忠实度低 → 优化 Prompt；端到端差 → 系统性问题。

---

### 7.2 检索质量评估指标

**Recall@K（召回率@K）：**

```
Recall@K = 相关文档中被 Top-K 结果召回的数量 / 总相关文档数量
```

- K=5 时：如果共有 4 篇相关文档，Top-5 结果中包含了 3 篇，则 Recall@5 = 3/4 = 75%
- **衡量覆盖度**：系统是否找全了相关内容

**Precision@K（精确率@K）：**

```
Precision@K = Top-K 结果中相关文档数量 / K
```

- K=5 时：Top-5 结果中有 3 篇相关，Precision@5 = 3/5 = 60%
- **衡量准确度**：召回的结果中有多少是真正相关的

**MRR（Mean Reciprocal Rank，平均倒数排名）：**

```
MRR = (1/|Q|) × Σ (1 / rank_i)
```

- 对每个查询，找到第一个相关文档的排名位置，取倒数，再对所有查询取平均
- 例：第一个相关文档排在第3位 → 得分 1/3
- **衡量排名质量**：最相关的文档是否排在前面

**实际使用：**

```java
// 构造评估数据集（人工标注的 <问题, 相关文档列表> 对）
List<EvalCase> cases = loadEvalDataset();

double totalRecall = 0;
for (EvalCase c : cases) {
    List<String> topK = retrieve(c.getQuery(), 5);
    long hits = topK.stream().filter(c.getRelevantDocs()::contains).count();
    totalRecall += (double) hits / c.getRelevantDocs().size();
}
double avgRecall = totalRecall / cases.size();
log.info("Recall@5 = {}", avgRecall);
```

---

### 7.3 生成质量评估指标

**Faithfulness（忠实度）：**
- 回答中的每个陈述是否都能在检索到的上下文中找到依据
- 低忠实度 = 幻觉（模型编造了不在检索结果中的内容）
- 评估方式：让 LLM 逐句判断回答中的陈述是否有文档支撑

```
忠实度 = 有文档支撑的陈述数 / 回答中的总陈述数
```

**Answer Relevance（答案相关性）：**
- 回答是否真正回应了用户的问题（有没有答非所问）
- 评估方式：让 LLM 基于回答反推可能的问题，与原问题做语义相似度对比

**Context Relevance（上下文相关性）：**
- 检索到的上下文中，有多大比例是真正对回答有用的
- 衡量"噪音"程度：检索结果是否引入了无关内容干扰 LLM

**三者关系：**

```
Context Relevance ──影响──► Faithfulness ──影响──► Answer Relevance
（检索精度）                  （生成忠实度）           （最终回答质量）
```

---

### 7.4 RAGAS 评估框架

**RAGAS（RAG Assessment）** 是目前最主流的 RAG 自动化评估框架，可以在没有人工标注答案（ground truth）的情况下，利用 LLM 自动评估 RAG 系统质量。

**核心指标：**

| 指标 | 评估内容 | 是否需要 Ground Truth |
|------|----------|----------------------|
| faithfulness | 回答是否忠实于上下文 | 否 |
| answer_relevancy | 回答是否切题 | 否 |
| context_precision | 上下文中有用内容的比例 | 是 |
| context_recall | 相关信息是否都被检索到 | 是 |

**使用方式（Python）：**

```python
from ragas import evaluate
from ragas.metrics import faithfulness, answer_relevancy, context_recall

dataset = Dataset.from_dict({
    "question": ["退款政策是什么？"],
    "answer":   ["可以在7天内申请退款..."],
    "contexts": [["退款需在购买后7天内申请..."]],
    "ground_truth": ["购买后7天内可无条件退款"]
})

result = evaluate(dataset, metrics=[faithfulness, answer_relevancy, context_recall])
print(result)
# {'faithfulness': 0.92, 'answer_relevancy': 0.88, 'context_recall': 0.85}
```

**局限性：** RAGAS 本身依赖 LLM 评估，存在评估者偏差（LLM 的主观性），不能完全替代人工评估。

---

### 7.5 人工质检实践

**适用场景：** RAGAS 自动评估有局限（如业务逻辑正确性、行业术语准确性），需要领域专家介入。

**抽样策略：**

| 抽样方式 | 说明 |
|----------|------|
| 随机抽样 | 从全量对话日志随机抽取，评估整体基线 |
| 分层抽样 | 按意图类别分层，确保每类场景都有覆盖 |
| 低置信度抽样 | 重点抽查 Rerank 分数低、或 LLM 回答中含"不确定"的对话 |
| 用户反馈驱动 | 优先评估被用户点踩/投诉的对话 |

**评分表设计：**

| 评分维度 | 1分（差） | 3分（中） | 5分（好） |
|----------|----------|----------|----------|
| 检索相关性 | 检索结果完全无关 | 部分相关 | 高度相关 |
| 回答准确性 | 事实错误 | 基本正确 | 完全准确 |
| 回答完整性 | 严重遗漏关键信息 | 基本完整 | 全面覆盖 |
| 表达清晰度 | 逻辑混乱 | 基本清楚 | 清晰易懂 |

**闭环机制：**
- 低分对话 → 分析根因（检索问题/Prompt 问题/模型问题）
- 典型错误案例 → 加入回归测试集，防止后续版本退化
- 高频错误类型 → 优化对应模块（改分块策略/调整 Rerank 阈值/重写 Prompt）

---


## 八、高频面试题 30 道

> 结合候选人项目背景（Spring AI、pgvector、双路检索、三态熔断器、MCP 等），以场景题为主。

---

### Q1：RAG 和直接让大模型回答相比，核心优势是什么？什么情况下 RAG 反而不适合？
**参考答案：** RAG 的核心优势在于三点：解决知识截止问题（可注入最新文档）、减少幻觉（回答有文档依据可追溯）、支持私域知识（无需微调）。不适合的场景：知识库文档质量极差时（Garbage In Garbage Out，检索噪音反而干扰生成）；问题本身需要复杂推理而非信息检索时（如数学推导），RAG 帮助有限；问题与知识库领域完全不匹配时，检索结果全是噪音，倒不如直接让 LLM 回答。

---

### Q2：你的项目用了双路并行检索，两路分别是什么，为什么要两路而不是一路？
**参考答案：** 第一路是意图检索，将用户问题与意图向量库比对，精准匹配意图节点，缩小知识库范围；第二路是全局向量检索，对全量文档 Chunk 做语义检索，覆盖面广。只用意图检索容易漏掉跨意图的相关文档；只用全局检索会引入大量无关噪音、精度低。两路并行（CompletableFuture）后合并去重，总耗时是 max(t1,t2) 而非 t1+t2，兼顾精度与召回率。

---

### Q3：向量检索用余弦相似度和欧氏距离有什么区别？pgvector 中如何选择？
**参考答案：** 余弦相似度衡量向量方向的夹角，与模长无关，适合文本语义相似场景（不同长度的文本 Embedding 后模长不同，但语义相近的方向一致）。欧氏距离衡量空间绝对距离，适合模长有意义的场景（如图像特征）。pgvector 中用 `<=>` 操作符对应余弦距离，`<->` 对应 L2（欧氏），文本 RAG 场景应选 `<=>` 并创建 `vector_cosine_ops` 索引。

---

### Q4：分块大小设为 512 token，Overlap 设为 50 token，这两个数字怎么确定的？
**参考答案：** 分块大小需平衡两个矛盾：块太小则单块缺乏足够上下文，LLM 无法理解；块太大则语义模糊，相似度分散，召回精度降低，且超出 Embedding 模型的推荐输入长度（bge-large-zh 推荐 512）。Overlap 解决块边界语义截断问题，设 10% 左右（50/512≈10%）可保证连续性，代价是存储增加约 10%。实际应该通过 A/B 测试，对比不同分块大小的 Recall@5 确定最优值，512+50 是经验起点。

---

### Q5：Rerank 用的是什么模型？它和向量检索的 Bi-Encoder 有什么本质区别？
**参考答案：** 项目用 bge-reranker-large 做 Rerank。本质区别在于编码方式：Bi-Encoder（向量检索）将问题和文档分别独立编码成向量，再计算余弦相似度，速度快但缺少深度交互；Cross-Encoder（Rerank）将问题和文档拼接后一起送入模型，全层注意力同时看两段文本，精度更高但速度慢（每对都要完整推理）。实践中两阶段结合：Bi-Encoder 快速召回 Top-20（粗排），Cross-Encoder 对 20 篇精排取 Top-5（精排），兼顾速度与精度。

---

### Q6：RAG 系统中"幻觉"有哪几种来源？如何针对性防护？
**参考答案：** 来源分三类：一是检索幻觉（检索结果与问题无关，LLM 被无关内容误导）；二是生成幻觉（LLM 忽视检索结果，凭预训练知识编造）；三是空检索幻觉（检索无结果时 LLM 依然输出看似有依据的内容）。防护手段对应：检索幻觉→提高 Rerank 阈值、过滤低分文档；生成幻觉→在 Prompt 中强调"仅基于以下资料回答，不得使用其他知识"；空检索幻觉→检测到空检索时走专用兜底 Prompt，明确告知模型无相关文档。

---

### Q7：你怎么衡量 RAG 系统的召回率？召回率低时怎么排查？
**参考答案：** 用 Recall@K 衡量，需要标注数据集（人工标注每道题的相关文档 ID），计算 Top-K 结果覆盖相关文档的比例。召回率低时排查四个环节：分块问题（关键信息被切断在块边界，调整 Overlap 或换语义分块）；Embedding 质量问题（模型对领域术语语义理解差，换更适合的 Embedding 模型）；检索策略问题（纯向量检索对专有名词效果差，引入混合检索/BM25）；提示词重写问题（口语化问题未改写，扩展关键词或加 HyDE）。

---

### Q8：HyDE 是什么？什么场景下用它效果好？
**参考答案：** HyDE（Hypothetical Document Embeddings，假设文档扩展）：先让 LLM 基于用户问题生成一段"假设性答案文本"，再用这段假设答案的 Embedding 去向量库检索，而非直接用问题的 Embedding 检索。效果好的场景：用户问题非常简短或口语化，直接 Embedding 语义信息不足（如"怎么退款"）；问答型知识库中文档都是答案形式，用假设答案的向量与文档向量空间更匹配。代价是多了一次 LLM 调用，延迟增加，适合对质量要求高、对延迟不敏感的场景。

---

### Q9：你的项目中 pgvector 和 Milvus 如何切换？两者在功能上有什么差距？
**参考答案：** 通过 `rag.vector.type` 配置项切换，infra-ai 模块定义统一的 VectorStoreService 接口，`PgVectorStoreService` 和 `MilvusVectorStoreService` 分别实现，Spring `@ConditionalOnProperty` 按配置激活对应实现。功能差距：pgvector 基于 PostgreSQL，运维成本低，支持 SQL 联合查询，但不支持原生混合检索，水平扩展能力有限；Milvus 是专用向量数据库，原生支持混合检索（稠密+稀疏）、多向量字段、分布式水平扩展，适合大规模生产场景，但运维复杂度更高（需维护 etcd + MinIO + Milvus 集群）。

---

### Q10：向量数据库中的 HNSW 索引是什么原理？和 IVF 有什么区别？
**参考答案：** HNSW（分层可导航小世界图）是图结构近似最近邻索引，将向量构建成多层图，高层稀疏（长距离跳跃）、低层稠密（精确搜索），查询时从高层入口快速逼近目标区域，复杂度约 O(log N)。IVF（倒排文件索引）先对所有向量做 K-Means 聚类，查询时先找最近的几个聚类中心，再在这些簇内暴力搜索。对比：HNSW 精度更高、构建后不需要重新训练，但内存占用大；IVF 内存友好，支持量化压缩（IVF_PQ），但需要定期重新训练聚类中心，且对分布不均匀的数据效果较差。本项目 pgvector 默认使用 HNSW。

---

### Q11：Embedding 模型选 bge-large-zh 还是 text-embedding-v3，怎么决策？
**参考答案：** 决策考量四个维度：语言和领域（纯中文场景 bge-large-zh 在 MTEB 中文榜靠前，中英混合场景 text-embedding-v3 更均衡）；部署方式（本地部署/私有化选 bge，不想维护推理服务选 API 的 text-embedding-v3）；吞吐量（bge 本地推理受 GPU 资源限制，text-embedding-v3 是云端 API 弹性扩缩）；成本（bge 一次部署长期使用，text-embedding-v3 按 token 计费，大量入库时成本可观）。本项目开发环境选 bge-large-zh（本地推理，零成本），生产选 text-embedding-v3（稳定、弹性、无需维护推理服务）。

---

### Q12：混合检索中 RRF 融合公式里 k=60 这个参数什么含义？能调吗？
**参考答案：** RRF 公式 `score = Σ 1/(k + rank_i)` 中，k 是平滑参数，防止排名靠前（rank=1）的文档得分过于主导（若 k=0 则 rank=1 得分为1，远超 rank=2 的0.5）。k=60 是经验默认值，来自原始论文，意味着排名靠前几位和中间位次的得分差距不会太悬殊，有利于融合两路不同量级的排名信号。实际可以调：若希望更重视排名靠前的结果，减小 k（如 k=20）；若希望更均匀融合，增大 k（如 k=100）。最优值应通过离线评估（Recall@K / MRR 指标）确定，不应随意调整。

---

### Q13：知识库文档更新了，如何保证向量库和原始数据库的一致性？
**参考答案：** 核心挑战是跨系统事务（DB 更新和向量库更新不在同一事务中）。本项目用 RocketMQ 事务消息保证：先更新 DB（文档状态=PROCESSING），同时发送事务消息（入库事件）；事务消息提交后，消费者异步执行向量库的删旧插新操作；若消费失败则重试（最多3次），超过重试次数标记为 FAILED，由定时任务扫描补偿。删旧插新时按 `documentId` 匹配删除旧 Chunk，再插入新 Chunk，通过 MD5 判断文档是否真正变更（避免无意义的重入库）。

---

### Q14：意图识别用向量相似度分类，如果用户问题模糊导致意图置信度低，怎么处理？
**参考答案：** 设置三档阈值：置信度 >= 0.7 走双路检索（意图检索+全局检索）；0.5 ≤ 置信度 < 0.7 跳过意图检索，直接走全局向量检索（宁可检索范围大，也不要因意图误判导致漏检）；置信度 < 0.5 判定为意图不明确，有两种策略：一是返回澄清引导语让用户补充信息，二是走全局检索兜底（适合对用户体验要求高、不希望打断用户的场景）。本项目 `rag.search.channels.vector-global.confidence-threshold` 默认配置 0.6 作为分界点，可灵活调整。

---

### Q15：pgvector 存向量的表设计有什么注意事项？
**参考答案：** 关键设计点：向量维度必须在建表时固定（`embedding vector(1024)`），后续无法修改，若换 Embedding 模型（维度不同）需重建表；向量字段不能设 NOT NULL 的同时又允许空值导致索引失效；HNSW 索引建立在向量列上（`USING hnsw (embedding vector_cosine_ops)`），建索引前应先批量插入数据（索引是静态构建，大量插入后建索引比边插边建效率高）；业务查询常用的过滤字段（如 `knowledge_base_id`）要加 B-Tree 索引，配合向量检索做预过滤（WHERE 条件先缩小候选集，再 ANN 检索），避免全量向量扫描。

---

### Q16：文档入库失败率高，你会从哪些方向排查？
**参考答案：** 分三层排查：解析层（Tika 解析 PDF 扫描件失败→需 OCR 支持；特殊格式文档编码问题→检查字符集）；Embedding 层（API 限流导致批量请求429错误→降低并发量、加指数退避重试；单个文本超长→在 Chunking 阶段控制最大 token 数）；入库层（pgvector 向量维度与表定义不一致→检查 Embedding 模型是否切换过；数据库连接池耗尽→调整连接池大小）。实践中应在每个环节打详细日志，记录具体失败原因，结合定时任务扫描 FAILED 记录中的 `error_message` 字段统计高频错误类型。

---

### Q17：什么是 ReAct 框架？你的 Agent 是否用到了 ReAct 模式？
**参考答案：** ReAct（Reasoning + Acting）让 LLM 交替进行推理（Thought）和行动（Action），每次行动的结果（Observation）反馈给 LLM 驱动下一轮推理，循环直到任务完成。Spring AI 的工具调用本质上就是 ReAct 的实现：LLM 决定调用哪个工具（Thought→Action），框架执行工具（Action），结果以 ToolMessage 注入对话（Observation），LLM 再次推理（Thought）。本项目中多工具调用场景（如先检索知识库、再查订单状态、最后综合回答）就是典型的 ReAct 循环，通过 `maxToolCallRounds=10` 限制最大迭代轮次，防止无限循环。

---

### Q18：Function Calling 中，如果 LLM 把参数搞错了（幻觉），你如何防护？
**参考答案：** 四道防线：第一道，Prompt 层——在工具 description 中写明参数格式、示例、枚举取值范围，让 LLM 少犯错；第二道，Schema 约束——声明参数类型、required 字段、pattern（如正则 `^ORD-\\d{8}-\\d{3}$`），部分模型支持 structured output 强制按 Schema 生成；第三道，调用前校验——工具实现入口做参数合法性校验，返回清晰的错误信息（而非抛异常），LLM 收到错误后可自行重试；第四道，temperature=0——降低生成随机性，参数生成更确定。如果三次重试仍失败，上报并返回托底答案。

---

### Q19：MCP 协议在你项目中具体起什么作用？如果不用 MCP 可以怎么替代？
**参考答案：** 本项目 mcp-server 将知识检索、意图树查询等能力封装为标准 MCP Tool，外部 Agent（如 Claude Desktop、其他 Agent 框架）无需了解内部实现，通过 MCP 协议即可调用。核心价值是能力复用和标准化：一套工具实现可被任意 MCP Client 调用，不需要为每个调用方单独开发 SDK。不用 MCP 的替代方案：一是直接暴露 REST API + OpenAPI 文档，Agent 通过 HTTP Tool 调用（灵活但非标准，不同 Agent 框架接入方式各异）；二是把工具代码直接嵌入 Agent 应用（耦合强，无法跨系统复用）。MCP 的优势在于工具生态标准化，劣势是协议本身还在演进，生产落地需关注版本兼容性。

---

### Q20：Multi-Agent 系统中，如何防止 Orchestrator 和 Worker 之间陷入无限循环？
**参考答案：** 三层防护：第一层，最大迭代次数（硬上限）——在 Orchestrator 层设置全局步骤计数，超过阈值（如20步）强制终止并返回部分结果；第二层，循环检测——记录每步的 Action+Input 哈希，若出现重复则判定为循环，中断并报错；第三层，任务完成判断——Orchestrator 在每轮推理后评估"任务是否已完成"（Final Answer 判断），而非只依赖 Worker 反馈。实践中还需给 Orchestrator 的 Prompt 明确写入"如果已有足够信息，不要继续调用工具，直接输出答案"，减少不必要的工具调用。

---

### Q21：Agent 工具调用需要幂等性，你如何设计？
**参考答案：** 幂等设计分两层：工具调用层——每次 Agent 调用工具时生成唯一 requestId（`UUID + agentSessionId + stepNo`），工具实现检查 Redis/DB 中该 requestId 是否已执行过，已执行则直接返回缓存结果（幂等消费）；业务操作层——对有副作用的操作（如下单、发邮件）在业务表中用 `unique(requestId)` 唯一约束或分布式锁防重。查询类工具（如知识库检索）天然幂等，无需特殊处理。本项目通过 `@IdempotentConsume` 注解（Redis SETNX 实现）在关键工具入口自动做幂等拦截。

---

### Q22：你的 Agent 系统如何处理工具调用超时？
**参考答案：** 两个层面：单次工具调用超时——每个工具实现内用 CompletableFuture + get(timeout, TimeUnit) 或 WebClient 的 responseTimeout 控制单次调用超时（如3秒），超时抛出 ToolTimeoutException，工具返回错误描述，LLM 收到错误后可决定重试或改变策略；整体 Agent 任务超时——在 Agent 入口设置总超时（如30秒），用 Future.get(30, TimeUnit.SECONDS) 等待整个 ReAct 循环结果，超时后强制中断，返回"处理超时，请稍后重试"。超时后要清理已分配的资源（释放 context、取消未完成的子 Future），避免资源泄漏。

---

### Q23：如果 Agent 调用工具的结果互相矛盾，LLM 应该如何处理？你会怎么设计？
**参考答案：** 矛盾结果是 Multi-Agent 和多工具系统的常见挑战。设计思路：一是 Prompt 显式引导——在 Orchestrator 的系统 Prompt 中写明"如果不同来源的信息矛盾，请标注来源，优先采信更权威的来源，并在回答中说明存在矛盾"；二是来源权重——在工具 description 中标注工具的可信度等级（如"官方数据库">"爬取数据"），让 LLM 有优先级依据；三是冲突检测层——在结果汇聚后用规则或 LLM 做矛盾检测，触发时自动发起澄清问题（询问用户偏好哪个来源）；四是透明输出——不强行合并矛盾信息，直接把两种说法呈现给用户，让用户判断。

---

### Q24：三态熔断器从 OPEN 到 HALF_OPEN，你用的是定时轮询还是被动触发？有什么区别？
**参考答案：** 本项目用被动触发（Lazy Transition）：不启动后台线程定时扫描，而是每次有请求进来时检查熔断器状态，在 `isAvailable()` 方法内判断 `当前时间 - openTime >= openDurationMs` 是否满足，满足则将状态切换为 HALF_OPEN 并放行该请求作为探测。好处：零额外线程开销，实现简单，对低流量场景友好（没有请求时不会无谓切换状态）。定时轮询的优势：即使没有请求，也能在后台自动切换到 HALF_OPEN，适合需要更精确控制探测时机的场景。本项目模型请求频率较高，被动触发足够，且避免了后台线程的资源开销。

---

### Q25：你的限流设计中，用 Redis ZSET 实现滑动窗口，和固定窗口相比有什么优势？有什么缺点？
**参考答案：** 固定窗口的问题：在窗口边界两侧各一瞬间可能突发 2 倍流量（如窗口末尾打满 N 次请求，新窗口开始又立刻打满 N 次）。ZSET 滑动窗口每次都基于"当前时间往前推 windowSize"的真实窗口统计，没有边界突刺，限流更精准。缺点：每次请求需要执行 ZREMRANGEBYSCORE + ZCARD + ZADD 三个操作（通过 Lua 脚本保证原子性），Redis 内存随用户数和请求量增长（每条请求记录一个 ZSET member），高并发场景需评估 Redis 内存压力；相比令牌桶/漏桶，ZSET 方案在极高 QPS 下性能不如纯内存方案，适合百 QPS 级别的用户级限流，不适合万级 QPS 的全局限流（全局限流建议用 Semaphore 或令牌桶）。

---

### Q26：SSE 连接断开后，客户端如何知道从哪里续传？你的系统支持断点续传吗？
**参考答案：** SSE 协议内置断点续传机制：服务端每个 event 可携带 `id` 字段（如递增序号），客户端断线重连时会在请求头带上 `Last-Event-ID: N`，服务端可从该 ID 之后重放未收到的 event。本项目目前未实现服务端的 event 持久化，因此不支持完整断点续传。实际处理：客户端断线重连会发起新的请求，服务端重新执行推理（LLM 重新生成），前端通过会话 ID 关联历史上下文保持对话连续性。若要支持真正的断点续传，需将每个 SSE chunk 持久化到 Redis（有 TTL），重连时从 `Last-Event-ID` 对应位置回放，代价是 Redis 额外存储和复杂度提升，本项目当前场景不必要。

---

### Q27：如果要给 RAG 系统加多租户隔离，你会怎么设计？
**参考答案：** 多租户隔离分三层：数据隔离——向量库用 `tenant_id + knowledge_base_id` 联合标识空间，每次检索带 `WHERE tenant_id = ?` 过滤（pgvector 支持元数据过滤，Milvus 支持 partition 隔离）；访问控制——用户请求经 JWT 解析 tenantId，通过 `UserContext` TTL 传递，所有检索/入库操作自动带上租户标识，防止越权；限流隔离——限流 key 设计为 `rate:tenant:{tenantId}:user:{userId}`，按租户分别配置配额（企业版 vs 免费版不同上限）。注意：向量索引按租户分区可显著提升隔离性和查询性能，但分区数过多会带来元数据管理开销，需权衡。

---

### Q28：线上 RAG 召回质量突然变差，你如何快速排查？
**参考答案：** 按流水线逐层排查：第一步，确认是召回问题还是生成问题——抽取几个典型 case，直接查看 Rerank 后的 Top-5 文档是否与问题相关（若文档本身就不相关，是检索问题；若文档相关但回答仍差，是生成问题）。检索问题排查：检查是否有新文档入库（分块策略/Embedding 模型是否变更）；检查提示词重写是否正常工作（打印重写后的 query）；检查意图识别置信度分布是否异常偏低（意图树新增/删除节点影响分布）；检查 Rerank 阈值是否被改动。生成问题排查：检查 Prompt 模板是否被修改；检查模型版本是否变更（API 供应商静默升级）；检查上下文长度是否超限导致截断。

---

### Q29：LLM 上下文窗口超限了怎么办？你有哪些应对策略？
**参考答案：** 超限有三个来源：历史对话过长、检索结果过多、单个文档 Chunk 过大。对应策略：历史对话——滑动窗口（只保留最近 N 轮）+ 摘要压缩（早期对话替换为摘要，本项目 `rag.memory.summary-start-turns` 控制触发时机）；检索结果过多——Rerank 后严格控制 Top-K（如 Top-5），每个 Chunk 存储时控制最大 token 数，Prompt 模板中为上下文预留固定 token 预算；单 Chunk 过大——入库阶段强制按 maxTokens 截断。兜底：在 Prompt 构建时计算总 token 数，超过模型窗口的 80% 时主动截断最低分的 Chunk，优先保留高分 Chunk 和最近历史。

---

### Q30：RAG 系统端到端延迟高（>3秒），你会从哪些方向优化？
**参考答案：** 分阶段拆解延迟：提示词重写（100-300ms）——可与检索并行启动，或缓存相似问题的重写结果；双路检索（200-500ms）——两路已并行，关键是检索线程池大小和向量库索引优化（确保 HNSW 索引生效，未走索引则全量扫描）；Rerank（200-500ms）——控制送入 Rerank 的候选数（Top-20 → Top-10），或换更轻量的 Rerank 模型；LLM 生成（1-3s）——首 token 延迟（TTFT）最关键，SSE 流式输出可显著改善用户感知；整体优化：对高频相同问题缓存最终答案（Redis，TTL 1小时）；意图识别结果缓存（相同问题的意图节点缓存，避免重复 Embedding 计算）；异步预热（热门知识库的常见问题提前检索缓存）。优先解决 P95 延迟，而非平均延迟。

---

<!-- PART2_END -->
