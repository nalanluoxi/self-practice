# RAG 难点深挖：文档入库 ETL 流水线 + 双策略分块

> 选题理由：文档入库是 RAG 系统的数据基础，质量直接决定检索效果。
> 这个模块涉及异步解耦、文档解析、分块算法、向量化、容错设计等多个层次，
> 技术深度高，和业务强绑定，面试官极易在此深挖。

---

## 一、业务背景（为什么要这么设计）

**RAG 系统的数据流向**：
```
用户上传文档
    ↓
[入库流水线]：文档 → 文本 → 分块 → 向量化 → 存向量库
    ↓
[对话时]：用户问题 → 向量检索 → 取出相关 chunk → 注入 Prompt → LLM 回答
```

入库质量的三个核心问题：
1. **文本提取**：PDF 有文字型和图片型两种，普通 Reader 对扫描件完全失效
2. **分块策略**：切得太小 → 单块语义不完整；切得太大 → 噪声多，LLM 无法聚焦
3. **向量化可靠性**：Embedding 模型 API 偶发故障，入库过程中挂了怎么办

---

## 二、完整入库流水线设计

### 2.1 全链路时序

```
用户 POST /knowledge/document/upload
    ↓
KnowledgeDocumentServiceImpl.upload()   ← @Transactional
    ① 文件字节写入 t_knowledge_document_file（PG blob 字段）
    ② 文档元数据写入 t_knowledge_document（状态 pending）
    ③ ingestionService.ingest(docId)     ← 立刻返回，上传接口响应 200
         ↓
         [ingestionExecutor 线程池，异步执行]
    ④ 从 DB 读取文件字节
    ⑤ 解析文本（按文件类型分支）
    ⑥ 分块（按文件类型选策略）
    ⑦ 元数据增强（可选：关键词 + 摘要）
    ⑧ 逐块调用 VectorStore.add()
         → RoutingEmbeddingService（带熔断）→ Ollama bge-m3
         → 写入 PgVector
    ⑨ 更新文档状态：success / failed
```

### 2.2 为什么上传接口和入库流水线要解耦（@Async 的必要性）

**不解耦的问题**：
- 一份 100 页 PDF 入库可能要几分钟（解析 + 逐块向量化 N 次 API 调用）
- 上传接口同步等待 → HTTP 请求超时（默认 30s），用户以为失败，重复上传
- 重复上传 → 重复入库 → 向量库里有重复数据 → 检索时重复结果

**解耦后**：
- 上传接口只做：存文件字节 + 写元数据（pending）+ 触发异步任务，<100ms 返回
- 前端轮询文档状态字段（pending → success/failed），异步感知入库结果

**`@Async("ingestionExecutor")` 的工作原理**：
- Spring 创建一个独立的 `ThreadPoolTaskExecutor`（`ingestionExecutor`）
- `@Async` 注解让 Spring AOP 把方法调用包装为异步提交到该线程池
- 调用方的线程立刻返回，不等 `ingest()` 执行完毕

**为什么要用独立线程池，不复用 Spring 默认 `SimpleAsyncTaskExecutor`**：
- `SimpleAsyncTaskExecutor` 每次都创建新线程，没有上限，高并发上传时线程爆炸
- 独立 `ingestionExecutor` 有核心线程数、最大线程数、队列容量的配置，资源可控

### 2.3 事务边界设计的关键点

`upload()` 方法上有 `@Transactional`，包含了：
- `t_knowledge_document_file` 的文件字节写入
- `t_knowledge_document` 的元数据写入

**注意**：`ingestionService.ingest(docId)` 是 `@Async` 调用，在 `@Transactional` 方法内部调用异步方法时，Spring 会在事务提交后才真正提交异步任务到线程池。

这保证了：**异步线程拿到 docId 去查 DB 时，文档记录一定已经提交**，不会出现「异步线程先启动，主线程事务还没提交，查不到记录」的竞争。

---

## 三、文本解析：三路分支 + OCR 兜底

### 3.1 三类文件的解析策略

```java
if ("pdf") {
    // 先尝试文字图层提取
    PagePdfDocumentReader reader = new PagePdfDocumentReader(resource, config);
    String text = reader.get()...;

    if (text.isBlank()) {
        // 文字图层为空 → 图片型 PDF → OCR 兜底
        return ocrPdf(fileBytes);
    }
    return text;
}
if ("md" / "markdown") {
    // MarkdownDocumentReader：保留代码块、引用块、横线结构
    MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
    return reader.get()...;
}
// txt 及其他
return new String(fileBytes, StandardCharsets.UTF_8);
```

### 3.2 图片型 PDF 的 OCR 流程（最有技术深度的点）

**什么是图片型 PDF**：
- 扫描仪扫出来的 PDF，每一页是一张图片，没有文字图层
- 用 `PagePdfDocumentReader` 提取出来的文本是空的
- 如果不处理，这类文档入库后向量化的是空字符串，检索永远召回不了

**OCR 处理链**：
```
byte[] pdfBytes
    ↓ PDFBox Loader.loadPDF()
PDDocument（PDF 文档对象）
    ↓ PDFRenderer.renderImageWithDPI(pageIndex, 300)
BufferedImage（每页渲染为 300 DPI 图片）
    ↓ Tesseract.doOCR(image)
String（该页识别出的文字）
    ↓ 所有页拼接
完整文本
```

**关键参数**：
- **300 DPI**：分辨率越高 OCR 精度越好，但渲染越慢、内存占用越大。300 DPI 是 OCR 精度和性能的常用平衡点（150 DPI 精度差，600 DPI 太慢）
- **`chi_sim+eng`**：中英文混合识别，支持中文文档
- **最多处理 30 页**：防止超长 PDF 把内存撑爆（每页 300DPI 图片约 10-20MB）

**Tesseract 的坑**：
- 需要提前安装 Tesseract 和语言包（macOS: `brew install tesseract tesseract-lang`）
- tessdata 目录不存在时，native 层会 SIGSEGV，直接 kill JVM（不是 Java 异常，catch 不住）
- 解法：在调用 Tesseract 前**先检查目录是否存在**，不存在直接返回空串并打 error 日志，不进入 native 层

```java
File tessDir = new File(tessDataPath);
if (!tessDir.exists() || !tessDir.isDirectory()) {
    log.error("[OCR] tessdata 目录不存在: {}，跳过 OCR", tessDataPath);
    return "";  // 早退，不调用 Tesseract native
}
```

---

## 四、分块策略：固定大小 vs 结构感知

### 4.1 为什么分块策略是 RAG 质量的核心

**分块的本质**：把一篇长文档切成多个小片段（chunk），每个 chunk 独立向量化存入向量库。
用户提问时，向量检索返回的是 chunk，不是整篇文档，LLM 基于 chunk 回答。

**切块太小的问题**：
```
原文：「Spring AOP 基于代理模式实现，JDK 动态代理要求目标类实现接口，
        CGLIB 通过生成子类代理不需要接口。」

切块后（太小）：
  chunk1: 「Spring AOP 基于代理模式实现，JDK 动态代理要求目标类实现接口，」
  chunk2: 「CGLIB 通过生成子类代理不需要接口。」

用户问：「AOP 用 CGLIB 还是 JDK 代理，有什么区别？」
  → 向量检索可能只召回 chunk2，chunk1 丢失，答案不完整
```

**切块太大的问题**：
- chunk 包含大量无关内容，向量化后噪声大，检索精度低
- 把几千字的 chunk 全部注入 Prompt，token 消耗爆炸

### 4.2 固定大小分块（FixedSizeChunker）

**适用场景**：PDF、TXT 等无明显结构的纯文本。

**核心算法**（滑动窗口 + 语义边界对齐）：
```
start = 0
while start < len:
    targetEnd = start + chunkSize      // 理想切割点
    end = adjustToBoundary(targetEnd)  // 尝试对齐到语义边界

    chunk = text[start : end]
    chunks.add(chunk)

    nextStart = max(0, end - overlapSize)  // 下一块起点，保留 overlap
    start = nextStart
```

**边界对齐优先级**（`adjustToBoundary` 的逻辑）：
1. **换行符** `\n`（最优边界，段落结束）
2. **中文句末标点** `。！？`
3. **英文句末标点** `.!?`（但必须后跟空白，防止切断 URL 的域名，如 `example.com`）

**URL 归一化处理**（一个很细的工程细节）：
PDF 里的 URL 经常被换行拆成两行：
```
原文：「详情请访问 https://open.dingtalk.
com/document/api」
```
如果不处理，`https://open.dingtalk.` 和 `com/document/api` 被分到两个 chunk，两个都是无效 URL，检索时完全没用。

解法：检测到 URL 开头（`http://` 或 `https://`）后进入 `inUrl` 状态，遇到换行时判断是 URL 被断行（如上一个字符是 `.` 下一行开头是字母），则吞掉换行符把 URL 拼接完整。

### 4.3 结构感知分块（StructureAwareChunker）

**适用场景**：Markdown 文档（技术文档、知识库文章）。

**为什么 Markdown 需要特殊处理**：

Markdown 有明确的语义结构：
```markdown
# Spring AOP 原理

基于代理模式实现...

## JDK 动态代理

```java
Proxy.newProxyInstance(...)
```

## CGLIB 代理

...
```

固定大小分块可能把代码块切成两半：
```
chunk1: 「...基于代理模式实现\n\n## JDK 动态代理\n\n```java\nProxy.newProxyInstance(...)」
chunk2: 「\n```\n\n## CGLIB 代理\n...」
```
chunk1 的代码块没有闭合，chunk2 的代码块没有开头，两个 chunk 单独看都是损坏的内容。

**结构感知分块三步走**：

**第一步：扫描，识别结构块**

线性扫描文本，将内容划分为四类块：
| 块类型 | 识别规则 | 处理方式 |
|--------|---------|---------|
| Heading | `^#{1,6}\s+.*$` | 独立块，不与相邻内容合并 |
| CodeFence | ` ```开头` 到 ` ```结束` | 整段代码作为一个块，不在内部切割 |
| Atomic | `![]()` 图片或 `[]()` 独立链接行 | 独立块 |
| Para | 其他连续非空行（按空行分段） | 按预算合并 |

**第二步：打包，按预算合并相邻块**
```
min = chunkSize / 2        // 块最小尺寸，太小则继续合并
target = chunkSize         // 目标尺寸
max = chunkSize × 4/3      // 块最大尺寸，超过则开新块

当前块累积大小 < max：继续并入下一个块
超过 max：
    当前已超 min → 结束当前块，开始新块
    当前未到 min → 忍一次超出，并入下一块（宁可稍大也不要过小）
```

**第三步：物化，生成最终文本，追加 overlap**
- 把 [start, end) 坐标区间转成字符串
- 如果 overlap > 0，在每个块开头追加上一块的尾部 N 个字符
- 目的：保证相邻 chunk 有重叠内容，跨块的语义不会完全断裂

**Overlap 的直觉解释**：
```
文档：...AAABBBCCC...
              ↑ 实际切割点

chunk1 = ...AAA（包含 overlap 的部分 BBB）
chunk2 = （重复上一块尾部 BBB）...BBBCCC...

这样「BBB 附近的语义」在两个 chunk 里都有覆盖，不会因为恰好落在切割点而两个 chunk 都召回不到
```

### 4.4 元数据增强（可选层，提升召回精度）

每个 chunk 存入向量库时，附带 metadata：
```java
metadata = {
    "doc_id": "123",
    "kb_id": "456",
    "doc_name": "Spring框架手册.pdf",
    "file_type": "pdf",
    "chunk_index": 3,
    "total_chunks": 27
}
```

**关键词增强**（`KeywordMetadataEnricher`）：
- 用 LLM 从每个 chunk 提取 5 个关键词，写入 `metadata.excerpt_keywords`
- 检索时可以结合关键词过滤，提升精准度
- 开关控制：`ingestion.enrichment.enable-keyword=true`

**摘要增强**（`SummaryMetadataEnricher`）：
- 为每个 chunk 生成三类摘要，写入 metadata：
  - `PREVIOUS`：上一个 chunk 的摘要（补充前文上下文）
  - `CURRENT`：当前 chunk 的摘要（提升本块语义密度）
  - `NEXT`：下一个 chunk 的摘要（让当前块"预知"后续内容）
- 目的：解决跨 chunk 语义断裂问题，让每个 chunk 自带前后文摘要
- 代价：每个 chunk 额外 2-3 次 LLM 调用，入库耗时大幅增加，默认关闭

---

## 五、向量化容错：逐块写入 + 熔断路由

### 5.1 为什么逐块写入而不是批量 add

```java
// 代码里是这样做的：
for (int i = 0; i < springDocs.size(); i++) {
    try {
        vectorStore.add(List.of(springDocs.get(i)));  // 每块单独写
        successCount++;
    } catch (Exception e) {
        log.warn("第 {}/{} 块写入失败，跳过", i+1, springDocs.size());
        // 不 throw，继续处理下一块
    }
}
```

逐块写入的好处：
- **单块失败不中断**：一篇 100 块的文档，即使第 30 块向量化超时，其余 99 块正常入库，文档状态标记 success（部分成功）
- **批量 add 的问题**：批量调用时任何一块失败，整批回滚，100 块全部重试，成本翻倍

**文档状态的语义**：
- `pending`：刚上传，等待异步处理
- `success`：至少有一块写入成功（`successCount > 0`）
- `failed`：所有块都失败，需要重新处理

### 5.2 Embedding 熔断路由（RoutingEmbeddingService）

**设计和 Chat 熔断器完全一致**（三态 CLOSED → OPEN → HALF_OPEN），差异点：

- Chat 熔断器结合了 **SSE 首包探测**（因为流式响应难以判断是否真正成功）
- Embedding 熔断器是**同步调用**，直接 try/catch 判断成功失败，不需要首包探测

**动态切换模型的机制**：
```java
EmbeddingRequest request = new EmbeddingRequest(
    List.of(text),
    OllamaOptions.builder()
        .model(candidate.getModel())   // 运行时覆盖模型名
        .build()
);
EmbeddingResponse response = ollamaEmbeddingModel.call(request);
```
- 底层只有一个 `OllamaEmbeddingModel` Bean（Spring AI 注入的）
- 通过 `OllamaOptions` 在每次调用时动态指定不同模型名，实现「一个 Bean，多个候选模型」
- 相比多个 Bean 各自注入不同模型，节省了 Spring 容器管理开销

---

## 六、深挖问题清单（按难度排序）

### Level 1 — 业务理解

**Q: 为什么文件字节存数据库而不是对象存储（S3/OSS）？**
> 当时选择存 DB（PG 的 bytea 字段）主要是简化部署，不依赖额外的存储服务。
> 代价：文件多了 DB 体积膨胀，大文件读写慢。
> 生产环境应该用对象存储，DB 只存 storageKey（S3 路径），入库时从 S3 下载文件字节。

**Q: 文档状态 pending 期间用户发问能正常回答吗？**
> 能，只是回答不到这篇文档的内容。因为 pending 状态说明向量还没写入，
> 检索时自然搜不到，LLM 会基于其他已入库的文档回答。
> 这是 eventual consistency（最终一致性）的典型场景，入库完成前有短暂的"盲区"。

### Level 2 — 技术深度

**Q: StructureAwareChunker 的 coalesceGaps 方法是做什么的？**
> 相邻两个块之间可能有空白行（块间隙），这些间隙如果单独成块会产生大量只有换行符的"空白块"，
> `coalesceGaps` 把这些间隙并入前一个块，避免产生无意义的空块，减少噪声。

**Q: 固定大小分块里 overlap 的上限为什么是 chunkSize - 1？**
> 如果 overlap >= chunkSize，下一块的起点 = end - overlap，可能 <= start，
> 导致下一块和当前块完全重叠或起点倒退，进入死循环（每次 start 不推进）。
> 所以强制 overlap < chunkSize，保证每次 start 至少前进 1 个字符。

**Q: 为什么 Markdown 里英文 `.` 不总是切割边界？**
> URL 里有大量 `.`（如 `example.com`），如果无条件按 `.` 切割，URL 会被切断。
> 代码里对英文 `.` 的处理是：必须后跟空白字符（空格/换行）才视为句末，
> URL 中的 `.` 后面跟的是字母，所以不会被误切。

**Q: SummaryMetadataEnricher 的三种摘要（PREVIOUS/CURRENT/NEXT）是怎么生成的？**
> Spring AI 的 `SummaryMetadataEnricher` 接收整个 chunks 列表，对每个 chunk 调用 LLM：
> - CURRENT: 对自身内容生成摘要
> - PREVIOUS: 对前一个 chunk 的内容生成摘要，写入当前 chunk 的 metadata
> - NEXT: 对后一个 chunk 的内容生成摘要，写入当前 chunk 的 metadata
> 第一个 chunk 没有 PREVIOUS，最后一个没有 NEXT，边界处自动跳过。

### Level 3 — 故障与边界

**Q: 如果 OCR 识别出来的文字质量很差怎么办？**
> OCR 文字质量取决于原始图片质量（模糊、倾斜、低对比度都会导致识别错误）。
> 当前没有质量评估，直接入库。
> 改进：OCR 后计算识别文字的置信度（Tesseract 返回每个词的置信度），
> 置信度过低的块标记为"低质量"，入库时降权或跳过。

**Q: 同一篇文档重复上传怎么处理？**
> 当前没有做重复检测，会重复入库（向量库里有两份）。
> 改进：上传时对文件内容取 MD5，DB 里记录 content_hash，
> 相同 hash 的文件直接复用已入库的向量，不重新处理。

**Q: 向量库里的 chunk 和 DB 里的文档记录如何保持一致？删除文档时向量怎么清理？**

> 见下方专题章节「七、DB 与向量库一致性」，这是一个值得单独深挖的设计问题。

### Level 4 — 优化方向

**Q: 100 块文档逐块向量化要调用 100 次 Embedding API，太慢了怎么优化？**
> 方向一：批量向量化（batch embedding），大多数 Embedding API 支持一次传入多个文本，一次调用处理 N 块
> 方向二：并行向量化（CompletableFuture + 线程池），多块同时请求，但要限制并发防止打爆 API 限频
> 当前逐块串行的好处：单块失败可精确重试，不影响其他块；并行化后需要额外处理部分失败重试逻辑

---

## 七、DB 与向量库一致性专题

### 7.1 为什么这是一个难题

DB（MySQL/PG）和 PgVector 是**两个独立的存储系统**，它们之间没有分布式事务支持。
任何涉及两个系统的写操作，都天然存在部分成功的风险：

```
场景一：删除文档
  ① DB 逻辑删除 deleted=1  ✅
  ② PgVector 删除向量       ❌ 网络超时
→ 结果：DB 已删，向量残留（"幽灵 chunk"），检索时仍会召回

场景二：更新文档内容（重新入库）
  ① 触发异步入库，新 chunk 写入 PgVector  ✅
  ② 旧 chunk 清理                         ❌ 未执行
→ 结果：向量库里新旧 chunk 并存，召回时新旧内容混杂，答案混乱

场景三：入库中途失败
  ① 前 50 块写入 PgVector  ✅
  ② 第 51 块 Embedding 超时，后续全部跳过
→ 结果：文档状态 success（>0块成功），但向量库只有部分内容，检索不完整
```

### 7.2 当前项目的实际实现（诚实交代）

**删除文档**（`KnowledgeDocumentServiceImpl.delete()`）：

```java
@Transactional(rollbackFor = Exception.class)
public void delete(Long docId) {
    // ① 逻辑删除文件字节
    jdbcTemplate.update("DELETE FROM t_knowledge_document_file WHERE storage_key = ?", storageKey);
    // ② 逻辑删除文档元数据（MyBatis-Plus @TableLogic，设 deleted=1）
    documentMapper.deleteById(documentDO);
    // ③ ← 向量清理：当前代码里没有！这是一个已知的不一致风险点
}
```

**知识库删除**（`KnowledgeBaseServiceImpl.delete()`）：
- 有前置保护：知识库下还有文档时拒绝删除（要求先把文档删干净）
- 同样没有清理 PgVector 的逻辑

**当前状态总结**：项目里 DB 删除和向量清理是脱节的，存在幽灵 chunk 的风险。
这是一个可以在面试中主动承认的设计缺陷，并展开讲"我知道问题在哪，生产里该怎么改"。

### 7.3 一致性问题的本质分类

理解这个问题要先区分两种不一致：

| 类型 | 描述 | 危害 |
|------|------|------|
| **DB 有，向量库没有**（漏入库） | 文档上传后入库失败 | 该文档内容永远检索不到 |
| **向量库有，DB 没有**（幽灵chunk） | 删除文档后向量未清理 | 检索到已删除文档的内容，用户困惑 |
| **向量库部分，DB 全量**（不完整入库） | 入库中途失败 | 检索只能召回文档的一部分内容 |

三种情况的严重性：幽灵 chunk > 不完整入库 > 漏入库（漏入库用户可以重新上传解决）。

### 7.4 生产可行的解决方案（由简到难）

#### 方案一：同步删除（最简单，当前项目应该改成这样）

```java
@Transactional(rollbackFor = Exception.class)
public void delete(Long docId) {
    // 1. 先删向量（失败则整体回滚，DB 不删）
    try {
        vectorStore.delete(
            FilterExpressionBuilder.eq("doc_id", docId.toString())
        );
    } catch (Exception e) {
        throw new BusinessException("向量清理失败，删除中止: " + e.getMessage());
    }
    // 2. 向量清理成功后再删 DB
    jdbcTemplate.update("DELETE FROM t_knowledge_document_file WHERE storage_key = ?", ...);
    documentMapper.deleteById(docId);
}
```

**优点**：实现简单，最终状态一致。

**缺点**：
- `@Transactional` 控制的是 DB 事务，不能回滚 PgVector 的操作
- 如果第 2 步 DB 删除失败，向量已经删了但 DB 还在，反向不一致
- 真正的原子性保证不了

**适用场景**：数据量小、一致性要求不极端（幽灵 chunk 偶尔出现可接受）。

---

#### 方案二：软删除向量 + 检索过滤（不改架构，靠应用层过滤）

核心思路：向量从不真正删除，而是在 metadata 里加一个 `deleted=1` 标记；检索时过滤掉被标记的 chunk。

```java
// 删除时：只在向量 metadata 里打标记
// 这个操作需要 PgVector 支持 metadata 更新（或重新写入带新 metadata 的 chunk）

// 检索时：过滤条件加上 deleted != 1
SearchRequest request = SearchRequest.builder()
    .query(query)
    .filterExpression(
        new FilterExpressionBuilder()
            .ne("deleted", "1")  // 过滤掉软删除的 chunk
            .build()
    )
    .build();
```

**优点**：向量不删除，没有「删了向量但 DB 没删」的反向不一致。

**缺点**：
- 向量库里积累大量 deleted=1 的垃圾数据，占用存储
- PgVector 的 metadata 更新不如 DB 方便（通常要 delete + re-insert）
- 需要定期批量清理 deleted 向量（类似 DB 的物理删除任务）

---

#### 方案三：状态机 + 异步补偿（生产推荐，最稳）

核心思路：把删除操作变成状态机，通过 DB 里的状态驱动向量清理，靠重试保证最终一致。

**文档状态扩展**：
```
pending  → 上传后，等待入库
success  → 入库完成，向量就绪
failed   → 入库失败
deleting → 用户触发删除，正在清理向量
deleted  → 向量清理完成，安全删除
```

**删除流程**：
```
① 用户点击删除
   → DB 文档状态改为 deleting（立刻返回响应，不等向量清理）

② 后台异步任务（定时轮询 status=deleting 的记录）
   → 调用 vectorStore.delete(doc_id=xxx) 清理向量
   → 成功：DB 状态改为 deleted（或物理删除）
   → 失败：记录错误，等下次轮询重试（可设最大重试次数）
```

**优点**：
- 用户侧无感知（操作立刻返回）
- 向量清理失败自动重试，最终一定能清干净
- DB 状态是向量清理的唯一依据，不会遗漏

**缺点**：
- deleting 状态期间用户查不到该文档（已从列表消失），但检索时还可能召回旧向量
- 需要额外的后台任务（定时任务或消息队列驱动）

---

#### 方案四：事务消息（最严格，参考 ragent 生产项目）

在另一个更完整的 `ragent` 项目里（CLAUDE.md 里有提到），文档入库使用了 **RocketMQ 事务消息**保证 DB 写入和消息发送的原子性：

```
① DB 写入文档记录（half message，事务未提交）
   ↓
② 执行本地事务（DB commit）
   ↓ 成功
③ 确认消息（消息对消费者可见）
   ↓
④ 消费者（ingestion 服务）消费消息，执行向量写入
   ↓ 失败
⑤ RocketMQ 自动重投，消费者重试
```

这个方案保证「DB 写入成功 ↔ 消息一定被消费」，向量清理通过消息驱动，失败自动重试。

**代价**：引入 RocketMQ，架构复杂度大幅提升，适合生产级 RAG 系统。

### 7.5 不一致发现与修复：定期一致性校验

无论选哪个方案，都建议加一个**定期校验任务**兜底：

```
每天凌晨执行：
  1. 查 DB 里所有 status=success 的 doc_id 列表
  2. 查 PgVector 里 metadata.doc_id 的 distinct 集合
  3. 对比差集：
     - DB 有但向量库没有 → 重新触发入库（漏入库修复）
     - 向量库有但 DB 没有 → 删除向量（幽灵 chunk 清理）
  4. 输出校验报告，告警异常数量
```

这个任务是系统的"安全网"，即使线上偶发不一致，也能在每天校验时发现并修复。

### 7.6 面试标准回答模板（STAR 结构）

> **S（背景）**：DB 和 PgVector 是两套独立存储，不支持分布式事务，删除/更新操作存在部分成功的风险。
>
> **T（问题）**：需要保证文档删除时向量同步清理，避免幽灵 chunk 影响检索结果。
>
> **A（我的方案）**：
> 当前项目用的是同步删除策略——先清理向量，再删 DB 记录；向量清理失败时抛异常中止操作，不删 DB，保证「DB 有，向量库有」的正向一致性。这适合当前的数据规模。
> 如果要做到更强的一致性保证，生产环境应该引入状态机（deleting 中间态）+ 异步补偿重试，或者用 RocketMQ 事务消息驱动向量操作。同时配合每日一致性校验任务做兜底。
>
> **R（权衡）**：三个方案的一致性强度递增，但实现复杂度也递增。选哪个取决于业务对"幽灵 chunk 存活多久"的容忍度。

**Q: 检索精度不好时，从分块角度有什么优化思路？**
> 1. **缩小 chunkSize**：每块更小，语义更纯，但召回需要更多块，token 消耗增加
> 2. **增大 overlap**：减少跨块语义断裂，但会增加向量库存储量
> 3. **父子块检索（Parent Document Retriever）**：向量化用小块（精度高），召回后返回包含该小块的大块（上下文完整），检索精度和上下文完整性兼顾
> 4. **HyDE（Hypothetical Document Embeddings）**：检索前用 LLM 生成一段"假设性答案"，用假设性答案的向量去检索，比直接用问题向量效果更好

---

## 八、刁钻追问清单（简历内容延伸，鉴别真假的关键）

> 这些问题表面上没写在简历里，但都从简历每一句话里能挖出来。能答上来证明真做过，答不上来会被认为是 copy 的。

---

### 关于 @Async 异步入库

**Q: `@Async` 方法必须在不同的 Bean 里调用，为什么？自己调自己会怎样？**
> `@Async` 依赖 Spring AOP 代理。Spring 代理的工作方式是：外部调用时，调用的是代理对象，代理把方法包装成异步提交。
> 如果同一个 Bean 内部 `this.ingest()` 调用，绕过了代理，直接调原始对象，`@Async` 完全失效，变成同步调用。
> 所以 `DocumentIngestionService.ingest()` 必须从 `KnowledgeDocumentServiceImpl`（另一个 Bean）调用，才能触发异步。
> 相同道理的还有 `@Transactional`——自调用也会失效。

**Q: `@Transactional` 方法内部调 `@Async`，异步任务什么时候能看到事务提交的数据？**
> 这是一个很细的坑：`@Async` 任务是在**当前事务提交之后**才真正提交到线程池（Spring 通过 `TransactionSynchronization.afterCommit()` 钩子实现）。
> 所以异步线程执行时，主线程的 `@Transactional` 已经提交，`docId` 对应的记录一定能查到。
> 如果不是这个机制，会出现"异步线程先启动，主线程还没 commit，查不到记录"的竞争。
> 验证方式：如果把 `ingest()` 改成普通线程（不用 `@Async`），手动在 `@Transactional` 方法里 `new Thread().start()`，就可能出现这个竞争。

**Q: `ingestionExecutor` 线程池满了（队列也满了），新的上传请求会怎样？**
> 取决于线程池的拒绝策略（`RejectedExecutionHandler`）：
> - `AbortPolicy`（默认）：抛 `RejectedExecutionException`，`@Async` 方法直接抛异常，上传接口报 500
> - `CallerRunsPolicy`：调用方线程（HTTP 请求线程）自己执行任务，上传接口被阻塞
> - `DiscardPolicy`：静默丢弃，入库任务丢失，文档永远是 pending 状态
> 生产上应该用 `CallerRunsPolicy` + 监控告警（线程池满了说明入库速度跟不上上传速度，需要扩容或限流上传）。

---

### 关于文档解析

**Q: `PagePdfDocumentReader` 提取出来的文本是空的，你怎么判断是图片型 PDF 还是真的空文档？**
> 代码里的判断是 `text.isBlank()`。但这不完全准确——一个真的空白 PDF（只有几页全白纸）提取出来也是空。
> 更精准的判断：提取文本前先检查 PDF 每页是否包含至少一个 `TextPosition` 对象（PDFBox 的文字图层元素），
> 如果所有页的 `TextPosition` 数量都是 0，才确认是图片型 PDF，触发 OCR。
> 当前代码用 `text.isBlank()` 是一个近似判断，对绝大多数情况够用，但对"有少量不可见字符"的 PDF 可能误判。

**Q: Tesseract OCR 识别中文时，`chi_sim` 是什么？和 `chi_tra` 有什么区别？**
> `chi_sim`：简体中文（Simplified Chinese）。
> `chi_tra`：繁体中文（Traditional Chinese）。
> `chi_sim+eng`：同时识别简体中文和英文（加号表示多语言混合）。
> 如果文档是繁体中文（港台地区内容），用 `chi_sim` 识别率会很低，需要换成 `chi_tra`。

**Q: OCR 每页渲染 300 DPI，30 页文档内存峰值大概多少？**
> A4 纸 300 DPI：2480 × 3508 像素，RGB 3 字节/像素 ≈ **26 MB/页**。
> 30 页峰值（如果同时在内存）：约 **780 MB**。
> 但代码里是逐页处理（for 循环），每页 render → OCR → 结果 append 到 StringBuilder，上一页的 BufferedImage 就可以 GC。
> 实际内存峰值约 1 页 = 26 MB，不是 30 页叠加。

---

### 关于分块算法

**Q: FixedSizeChunker 里 overlap 不能超过 chunkSize-1，为什么？给个具体例子说明死循环是怎么发生的。**
> 假设 `chunkSize=10, overlap=10`：
> - 第一块：start=0, end=10
> - 下一块 start = max(0, 10-10) = 0 ← 没有前进！
> - 无限循环，每次 start 都是 0，永远处理同一段文本。
> 所以代码里强制 `overlap = min(overlap, chunkSize-1)`，保证 `nextStart = end - overlap >= end - (chunkSize-1) = start + 1`，每次至少前进 1 个字符。

**Q: StructureAwareChunker 把代码块整体作为一块，如果一个代码块超过 max（chunkSize 的 4/3 倍）怎么办？**
> 代码里在 `packBlocksToChunks` 里有这个处理：
> ```
> 加入下一个 block 后超过 max：
>     if (size < min) → 忍一次超限，还是合并（宁可超出 max 也不要过小的孤立块）
>     else → 开新 chunk
> ```
> 也就是说，如果一个代码块本身超过 max，它会作为单独一个 chunk 输出，大小超出上限但不会被强行切割。
> 这是一个设计取舍：**语义完整性 > 大小限制**，宁可让这一块超出 max，也不在代码块中间切一刀。

**Q: SummaryMetadataEnricher 的三种摘要（PREVIOUS/CURRENT/NEXT）会让 token 消耗翻几倍？**
> N 个 chunk，生成 CURRENT 需要 N 次调用，PREVIOUS 和 NEXT 也各需要 N 次（边界除外），共约 3N 次 LLM 调用。
> 加上入库时已有的向量化调用（N 次 Embedding），总计约 4N 次 API 调用。
> 一篇 100 块的文档：100 次 Embedding + 300 次 Chat LLM ≈ 在开销最大的方案下入库极慢，默认关闭是合理的。

---

### 关于熔断 + 向量化

**Q: `CircuitBreakerEmbeddingModel` 覆盖了 Spring AI 自动装配的默认 EmbeddingModel，这是怎么实现的？**
> Spring AI 自动装配会创建一个默认的 `EmbeddingModel` Bean（比如 `OllamaEmbeddingModel`）。
> `VectorStoreConfig` 里显式声明了 `@Bean CircuitBreakerEmbeddingModel`，它的类型也实现了 `EmbeddingModel` 接口。
> 当 `PgVectorStore` 需要注入 `EmbeddingModel` 时，Spring 发现有两个候选（默认的 + 我们的），因为我们的 Bean 是在显式 `@Bean` 里传给 `PgVectorStore.builder()` 的，直接指定了，不经过自动装配的类型匹配，所以能精确注入带熔断的版本。

**Q: 向量维度 4096 是怎么确定的？如果模型换了（比如从 bge-m3 换成 text-embedding-3-small），维度变了，向量库里的历史数据怎么处理？**
> 4096 是 Qwen3-Embedding-8B 的输出维度，和具体模型绑定。
> `PgVectorStore.builder().dimensions(4096)` 在 `initializeSchema=true` 时建表时指定列的向量维度。
> 如果换模型且维度不同（比如 text-embedding-3-small 是 1536 维）：
> - PgVector 表的向量列维度已经固定，新向量无法写入
> - 必须**重建表**（或新建表），把历史文档**全部重新向量化**（用新模型跑一遍 ingest）
> - 迁移期间系统不可用（或用双写）
> 这是 Embedding 模型迁移最大的成本，所以选型时要慎重，尽量选维度稳定、长期维护的模型。

---

### 关于整体 RAG 流程

**Q: 一次问答总共调用了几次 LLM？**
> 取决于配置开关，最多的情况（全部开启）：
> 1. `QueryRewriter.rewrite()` → 1 次 Chat LLM
> 2. `IntentClassifier.classify()` → 1 次 Chat LLM
> 3. `DocumentReranker.rerank()`（6 个 chunk）→ 6 次 Chat LLM
> 4. 最终 `streamChat()` → 1 次 Chat LLM（流式）
> **合计 9 次 LLM 调用**，其中 6 次是重排序打分。
> 如果关闭 rerank（`enableRerank=false`）：3 次。
> 如果同时关闭 rewrite（`enableRewrite=false`）：2 次。

**Q: 意图分类是同步调用 LLM，这和检索是串行的吗？延迟怎么控制？**
> 当前流水线是串行的：先 rewrite → 再 classify → 再 retrieve。
> 意图分类和查询改写之间其实可以**并行**：两者都依赖原始 query，互不依赖，可以用 `CompletableFuture` 同时发起，等两者都完成后再进入检索。
> 这是一个已知的优化点，实现后可以节省 1 次 LLM 串行等待时间。

**Q: 对话记忆的 `tryCompress()` 方法没有对 `conversationId` 加锁，并发时会怎样？**
> 这是一个并发 bug。
> 场景：同一用户同时发两条消息（双击发送），两个线程都执行 `add()` → `tryCompress()`。
> 两个线程都读到 `total > 20`，都触发压缩，都取最旧的 11 条 → 都生成摘要 → 都写入摘要 → 都删除原始消息。
> 结果：两条摘要都写入，或者两次删除互相冲突（第二次删除时记录已被第一次删了，IN() 查询返回空）。
> 修复方案：对 conversationId 加分布式锁（Redis SETNX），或者在 `tryCompress()` 里用 DB 行锁（SELECT FOR UPDATE）保证单次压缩。

**Q: 你的 RAG 项目有没有做效果评估？怎么衡量召回率和答题准确率？**
> 当前是定性评估（人工提问对比回答质量），没有做系统的量化评测。
> 标准的 RAG 评测框架（如 RAGAS）会测：
> - **Context Recall**（召回率）：正确答案所需的信息是否在 retrieved chunks 里
> - **Context Precision**（精度）：检索到的 chunks 有多少是真正相关的（噪声比例）
> - **Answer Faithfulness**（忠实度）：LLM 的回答是否完全基于 context，有无幻觉
> - **Answer Relevance**：回答是否真正回答了问题
> 如果要上生产，应该用这套指标跑自动化评测，而不是依赖人工感知。
