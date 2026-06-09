# Spark 从入门到面试

> 面向 Java 后端开发者，涵盖核心概念、原理、调优、面试题

---

## 一、Spark 核心概念

### RDD / DataFrame / Dataset 区别

| 特性 | RDD | DataFrame | Dataset |
|------|-----|-----------|---------|
| 类型安全 | 编译时检查 | 运行时检查 | 编译时检查 |
| 优化引擎 | 无 | Catalyst 优化 | Catalyst 优化 |
| 序列化 | Java/Kryo | Tungsten 编码 | Tungsten 编码 |
| API 风格 | 函数式 | SQL/DSL | 强类型 DSL |
| 适用场景 | 自定义复杂逻辑 | SQL 分析、ETL | 类型安全的 ETL |

**选型建议**：
- 需要 SQL 表达力 → DataFrame/Spark SQL
- 需要类型安全 + 优化 → Dataset（Scala/Java）
- 需要底层自定义控制 → RDD

### Spark 架构

```
Driver（主程序）
  ├── SparkContext / SparkSession
  ├── DAGScheduler（划分 Stage）
  └── TaskScheduler（分配 Task）

Executor（工作节点）
  ├── 执行 Task
  ├── 缓存数据（Storage Memory）
  └── 计算数据（Execution Memory）
```

- **Driver**：负责解析代码、构建 DAG、调度 Task
- **Executor**：真正执行计算的进程，每个节点可有多个
- **Master/Worker**（Standalone模式）：资源管理，YARN 模式下对应 ResourceManager/NodeManager

### 懒执行（Lazy Evaluation）

Transformation（如 map/filter）不立即执行，只记录操作链。直到 Action（如 collect/save）触发时，才真正计算。

好处：Catalyst 优化器可以对整个 DAG 进行全局优化（谓词下推、列裁剪等）。

---

## 二、宽窄依赖与 Stage 划分

### 窄依赖（Narrow Dependency）

父 RDD 的每个分区只被子 RDD 的**一个分区**使用，数据不需要跨节点传输，可以在同一个 Task 内流水线执行。

**例子**：`map`、`filter`、`flatMap`、`union`、`mapPartitions`

```
父分区1 → 子分区1
父分区2 → 子分区2
父分区3 → 子分区3
```

### 宽依赖（Wide Dependency / Shuffle Dependency）

父 RDD 的每个分区被子 RDD 的**多个分区**使用，数据需要跨节点重新分发，**必须触发 Shuffle**。

**例子**：`groupByKey`、`reduceByKey`、`join`（非 Broadcast）、`distinct`、`sortBy`、`repartition`

```
父分区1 → 子分区1、子分区2、子分区3
父分区2 → 子分区1、子分区2、子分区3
```

### Stage 划分规则

Spark 按**宽依赖**切分 DAG 为多个 Stage：
- 遇到宽依赖 → 切断，上游为一个 Stage，下游为新 Stage
- Stage 内部全是窄依赖，可以流水线执行
- Stage 之间串行，必须等上一个 Stage 完成

```
Stage1: filter → map           (窄依赖，流水线)
        ↓ Shuffle（宽依赖，切断）
Stage2: groupByKey → map       (窄依赖，流水线)
        ↓ Shuffle
Stage3: join → filter → save
```

---

## 三、Shuffle 原理

### Shuffle 流程

**Shuffle Write（Map 端）**：
1. Map Task 计算完成后，按 key 的 hash 值将数据写入不同的磁盘文件（每个 Reduce Task 对应一个文件）
2. 数据先写入内存缓冲区，缓冲区满后溢写（spill）到磁盘，最后合并（merge）

**Shuffle Read（Reduce 端）**：
1. Reduce Task 从各 Map 节点拉取属于自己的数据
2. 数据在内存中排序聚合，内存不足时溢写磁盘

### Shuffle 四大代价

1. **磁盘 IO**：Map 端写磁盘 + Reduce 端读磁盘
2. **网络 IO**：数据跨节点传输，大表 join 时网络传输量极大
3. **内存压力**：Reduce 端聚合时占用大量内存，量大时触发溢写
4. **Stage 串行等待**：Shuffle 是 Stage 边界，上游 Stage 全部完成才能开始下游

### Sort-Based Shuffle（JDK 1.6+ 默认）

Map 端将所有数据写入一个排序文件 + 索引文件，Reduce 端按索引拉取。
- 优点：减少文件数量，降低文件句柄压力
- 相比 Hash-Based Shuffle（每个 Map 对每个 Reduce 生成一个文件），文件数从 M×R 降低到 2M

---

## 四、Spark 内存模型

### Executor 内存分区

```
Executor JVM 堆内存
├── Reserved Memory（保留，约 300MB）
├── User Memory（用户自定义数据结构，默认 40%）
└── Spark Memory（默认 60%）
    ├── Storage Memory（缓存RDD/广播变量）
    └── Execution Memory（Shuffle、聚合、排序）
```

**动态内存分配**：Storage 和 Execution 之间可以互相借用，哪个需要就多分配给哪个（Unified Memory Manager）。

### 堆外内存

通过 `spark.memory.offHeap.enabled=true` 开启，使用 OS 直接内存，避免 GC 影响，适合大数据量场景。

---

## 五、常用算子

### Transformations（懒执行）

| 算子 | 说明 | 宽/窄 |
|------|------|-------|
| `map(f)` | 每个元素应用函数 | 窄 |
| `flatMap(f)` | map + 展平 | 窄 |
| `filter(f)` | 过滤 | 窄 |
| `mapPartitions(f)` | 按分区处理，减少函数调用开销 | 窄 |
| `union(other)` | 合并两个 RDD | 窄 |
| `distinct()` | 去重 | **宽** |
| `groupByKey()` | 按 key 分组，所有 value 聚合 | **宽** |
| `reduceByKey(f)` | 按 key 聚合，Map 端预聚合 | **宽** |
| `sortBy(f)` | 排序 | **宽** |
| `join(other)` | 按 key join | **宽** |
| `repartition(n)` | 重新分区（Shuffle） | **宽** |
| `coalesce(n)` | 减少分区（不 Shuffle）| 窄 |

### groupByKey vs reduceByKey（重要）

**groupByKey**：
- 先 Shuffle 所有数据到 Reduce 端，再聚合
- 网络传输量大，内存压力大

**reduceByKey**：
- Map 端**先预聚合**，再 Shuffle，最后 Reduce 端聚合
- 网络传输量大幅减少（相同 key 的数据在 Map 端先合并）
- **性能远优于 groupByKey，尽量用 reduceByKey**

### Actions（触发执行）

`collect()`、`count()`、`save()`、`reduce(f)`、`foreach(f)`、`first()`、`take(n)`

---

## 六、Spark SQL + UDF

### SparkSession 使用（Java）

```java
SparkSession spark = SparkSession.builder()
    .appName("MyApp")
    .master("yarn")
    .config("spark.sql.shuffle.partitions", "200")
    .enableHiveSupport()
    .getOrCreate();

// 读 Hive 表
Dataset<Row> df = spark.sql("SELECT * FROM my_table WHERE dt='2026-06-08'");

// DataFrame API
df.filter(col("domain").isNotNull())
  .groupBy("domain")
  .agg(count("url").as("url_count"))
  .show();
```

### UDF 注册与使用（Java）

```java
// 注册 UDF：根据 compositeScore 计算等级
spark.udf().register("calcGrade", (Double score) -> {
    if (score >= 0.75) return 1;
    if (score >= 0.50) return 2;
    if (score >= 0.25) return 3;
    return 5;
}, DataTypes.IntegerType);

// 使用 UDF
spark.sql("SELECT domain, calcGrade(composite_score) as grade FROM score_table");
```

---

## 七、数据倾斜

### 定义

Shuffle 后，大量数据被分配到少数几个 Task，这些 Task 处理时间远超其他 Task，整个 Stage 被拖死。

**根本原因**：某些 key 的数据量远大于其他 key（如热门域名 vs 小站点）。

**识别方式**：Spark UI → Stage 详情 → Task 列表，看是否有少数 Task 执行时间是其他 Task 的 10 倍以上。

### 五种解决方案

#### 方案 1：Map Join（Broadcast Join）
适合大表 join 小表（小表能放入内存，一般 < 几百MB）

```sql
-- Spark SQL hint
SELECT /*+ BROADCAST(b) */ a.*, b.retain_rate
FROM big_table a JOIN small_table b ON a.domain = b.domain
```

```java
// Java API
spark.conf().set("spark.sql.autoBroadcastJoinThreshold", "100mb");
Dataset<Row> result = bigDf.join(broadcast(smallDf), "domain");
```

#### 方案 2：加盐打散（Salting）
适合大表 join 大表，某些 key 极度倾斜

```java
// 步骤1：倾斜 key 加随机盐（0-9）
Dataset<Row> saltedA = bigTableA.withColumn("salted_key",
    concat(col("domain"), lit("_"), (rand().multiply(10).cast("int").cast("string"))));

// 步骤2：另一张表对应复制 10 份
Dataset<Row> expandedB = bigTableB.withColumn("salt", explode(array(
    lit(0), lit(1), lit(2), lit(3), lit(4),
    lit(5), lit(6), lit(7), lit(8), lit(9))))
    .withColumn("salted_key", concat(col("domain"), lit("_"), col("salt").cast("string")));

// 步骤3：按 salted_key join
Dataset<Row> joined = saltedA.join(expandedB, "salted_key");

// 步骤4：去盐聚合
Dataset<Row> result = joined.groupBy("domain").agg(...);
```

#### 方案 3：过滤大 Key 单独处理

```java
// 识别倾斜 key
List<String> skewedKeys = Arrays.asList("baidu.com", "taobao.com");

// 分成两部分
Dataset<Row> normalDf = df.filter(!col("domain").isin(skewedKeys.toArray()));
Dataset<Row> skewedDf = df.filter(col("domain").isin(skewedKeys.toArray()));

// 分别处理后 union
Dataset<Row> normalResult = normalDf.join(otherDf, "domain");
Dataset<Row> skewedResult = skewedDf.join(broadcast(otherDf.filter(...)), "domain");
Dataset<Row> finalResult = normalResult.union(skewedResult);
```

#### 方案 4：提高并行度

```java
spark.conf().set("spark.sql.shuffle.partitions", "2000");  // 默认200，调大
```

治标不治本，只是把数据分得更散，不能解决根本的 key 倾斜问题。

#### 方案 5：AQE（Adaptive Query Execution，Spark 3.0+）

```java
spark.conf().set("spark.sql.adaptive.enabled", "true");
spark.conf().set("spark.sql.adaptive.skewJoin.enabled", "true");
```

自动检测倾斜分区并拆分，无需手动干预。推荐 Spark 3.0+ 直接开启。

---

## 八、性能调优

### 1. Broadcast Join（最重要）

```java
// 设置广播阈值（超过此大小不自动广播）
spark.conf().set("spark.sql.autoBroadcastJoinThreshold", "100mb");

// 手动指定广播
import static org.apache.spark.sql.functions.broadcast;
Dataset<Row> result = df1.join(broadcast(df2), "key");
```

### 2. 缓存（cache vs persist）

```java
// cache()：等价于 persist(StorageLevel.MEMORY_AND_DISK)
df.cache();

// persist()：可以指定存储级别
import org.apache.spark.storage.StorageLevel;
df.persist(StorageLevel.MEMORY_AND_DISK_SER());  // 序列化存内存+磁盘
df.persist(StorageLevel.DISK_ONLY());             // 只存磁盘

// 用完记得释放
df.unpersist();
```

**存储级别选择**：
- 数据能放入内存 → `MEMORY_ONLY`（最快）
- 内存不够 → `MEMORY_AND_DISK`
- 内存紧张 → `MEMORY_AND_DISK_SER`（序列化减少内存占用）

### 3. 并行度设置

```java
// Shuffle 分区数（影响 groupBy/join 后的分区数）
spark.conf().set("spark.sql.shuffle.partitions", "400");  // 默认200

// 经验值：分区数 ≈ 数据量(GB) × 2 ~ 4，每个分区约 128MB
```

### 4. 序列化优化

```java
SparkConf conf = new SparkConf()
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

// 注册常用类（提高 Kryo 效率）
conf.registerKryoClasses(new Class[]{MyClass.class, OtherClass.class});
```

Kryo 序列化比 Java 默认序列化快 10 倍，内存占用减少 3-5 倍。

### 5. 避免不必要 Shuffle

```java
// 错误：使用 groupByKey，全量 Shuffle
df.groupByKey(r -> r.getString(0), Encoders.STRING())
  .mapGroups((key, iter) -> count(iter), ...);

// 正确：使用 reduceByKey，Map 端预聚合
df.rdd().mapToPair(r -> new Tuple2<>(r.getString(0), 1L))
  .reduceByKey(Long::sum);
```

---

## 九、Spark vs MapReduce

| 对比项 | Spark | MapReduce |
|--------|-------|-----------|
| 计算模型 | DAG（有向无环图） | Map + Reduce 两阶段 |
| 中间数据 | 内存（可溢写磁盘） | 必须写磁盘 |
| 速度 | 内存计算快 10-100 倍 | 磁盘 IO 慢 |
| 迭代计算 | 天然支持（缓存） | 每次都要写磁盘 |
| API | 丰富（SQL/DataFrame/流） | 仅 Map/Reduce |
| 容错 | RDD 血统（Lineage）重算 | 写磁盘后重算 |
| 内存要求 | 高 | 低 |

---

## 十、Spark Streaming vs Flink

| 对比项 | Spark Streaming | Flink |
|--------|----------------|-------|
| 处理模型 | 微批（Micro-batch） | 真流处理（Event-driven） |
| 延迟 | 秒级（批次间隔） | 毫秒级 |
| 状态管理 | 有限支持 | 强大的有状态计算 |
| 时间语义 | ProcessingTime 为主 | EventTime/ProcessingTime/IngestionTime |
| Checkpoint | 支持 | 强大的分布式 Checkpoint |
| 吞吐量 | 高（批量处理） | 较高 |
| 成熟度 | 高，生态丰富 | 高，阿里开源，国内广泛使用 |

**选型建议**：
- 对延迟不敏感，需要大批量处理 → Spark Batch
- 需要实时流处理，秒级延迟可接受 → Spark Streaming
- 需要毫秒级实时，有复杂状态计算 → **Flink**

---

## 十一、常见面试题 20 道

### Q1：RDD、DataFrame、Dataset 的区别？

RDD：底层抽象，类型安全，无优化，适合自定义复杂逻辑。
DataFrame：有 Catalyst 优化，但运行时类型检查，适合 SQL 分析。
Dataset：结合两者优点，编译时类型安全+Catalyst优化，Scala/Java 推荐使用。

### Q2：什么是宽依赖和窄依赖？

窄依赖：父RDD每个分区只被子RDD一个分区使用，不Shuffle，可流水线。例：map/filter。
宽依赖：父RDD每个分区被子RDD多个分区使用，必须Shuffle。例：groupByKey/join/distinct。

### Q3：Spark 如何划分 Stage？

按宽依赖（Shuffle 依赖）切分 DAG。遇到宽依赖则切断，上游为一个 Stage，下游为新 Stage。Stage 内部全是窄依赖，可流水线执行。

### Q4：Shuffle 有哪些代价？

磁盘 IO（Map 端写/Reduce 端读）、网络 IO（跨节点传输）、内存压力（Reduce 端聚合）、Stage 串行等待。

### Q5：groupByKey 和 reduceByKey 的区别？

groupByKey：先全量 Shuffle 所有数据到 Reduce 端再聚合，网络传输量大。
reduceByKey：Map 端先预聚合，再 Shuffle，性能远优于 groupByKey。**尽量用 reduceByKey**。

### Q6：什么是数据倾斜？如何解决？

定义：Shuffle 后大量数据分配到少数 Task，导致这些 Task 远慢于其他 Task。
解决：Map Join（广播小表）、加盐打散（Salting）、过滤大Key单独处理、提高并行度、AQE。

### Q7：Broadcast Join 是什么？什么时候用？

将小表广播到每个 Executor 内存，大表本地完成 join，完全避免 Shuffle。
适用场景：大表 join 小表，小表能放入内存（一般 < 几百MB）。

### Q8：cache() 和 persist() 的区别？

cache() 等价于 persist(MEMORY_AND_DISK)。persist() 可以指定存储级别（内存/磁盘/序列化等）。

### Q9：Spark 的懒执行是什么？有什么好处？

Transformation 不立即执行，只记录操作链，Action 触发时才真正计算。
好处：Catalyst 优化器可以对整个 DAG 全局优化（谓词下推、列裁剪、常量折叠等）。

### Q10：Spark vs MapReduce 核心区别？

Spark 基于内存计算+DAG，中间结果不需要写磁盘，速度快 10-100 倍。
MapReduce 每个阶段必须写磁盘，IO 代价高，不适合迭代计算。

### Q11：AQE（Adaptive Query Execution）是什么？

Spark 3.0+ 引入，运行时根据实际数据统计自动优化执行计划。主要功能：自动合并小分区、自动检测并处理数据倾斜、自动选择 Join 策略。

### Q12：Spark 内存模型中 Storage Memory 和 Execution Memory 是什么？

Storage Memory：用于缓存 RDD、广播变量等。
Execution Memory：用于 Shuffle、聚合、排序等计算过程。
两者可以动态互借（Unified Memory Manager）。

### Q13：什么是 coalesce 和 repartition 的区别？

repartition(n)：重新分区，Shuffle，可以增加或减少分区数。
coalesce(n)：只能减少分区数，不 Shuffle（合并分区），比 repartition 更高效。

### Q14：Spark 任务变慢如何排查？

1. 打开 Spark UI，找到慢 Stage
2. 看 Task 分布：少数 Task 极慢 → 数据倾斜；全部 Task 慢 → 内存不足/数据量暴增
3. 看 GC Time：GC 时间长 → 增加 Executor 内存
4. 看 Shuffle Read/Write：量异常大 → 上游数据量暴增或倾斜

### Q15：如何优化 Hive/Spark SQL？

分区裁剪（只读需要分区）、列裁剪（只查需要列）、Broadcast Join（小表广播）、谓词下推（WHERE 条件前置过滤）、避免 SELECT *。

### Q16：Spark 的容错机制是什么？

RDD 通过**血统（Lineage）**记录转换链，某个分区数据丢失时，沿血统重新计算。
对于迭代计算，可以对中间 RDD 做 checkpoint 持久化，避免重算整个血统。

### Q17：Flink 和 Spark Streaming 的核心区别？

Spark Streaming 是微批处理（秒级延迟），Flink 是真正的流处理（毫秒级）。Flink 有更强的状态管理和精确的 EventTime 支持，适合实时计算场景。

### Q18：什么情况下用 mapPartitions 替代 map？

map 对每个元素调用一次函数，mapPartitions 对每个分区调用一次函数。
当函数初始化代价高（如建立数据库连接），用 mapPartitions 可以复用连接，大幅减少开销。

### Q19：Spark 为什么比 Hadoop MapReduce 更适合机器学习？

机器学习通常需要多次迭代计算（如梯度下降）。MapReduce 每次迭代都要写读磁盘；Spark 可以将中间数据 cache 在内存，多次迭代只需内存读取，速度快 100 倍以上。

### Q20：如何设置合适的 shuffle partitions 数量？

经验公式：分区数 ≈ 总数据量(GB) × 2 ~ 4，每个分区约 128MB。
默认值 200 在数据量很大时会导致每个分区数据过多（OOM/溢写）；数据量小时会有大量空分区浪费。
Spark 3.0+ 开启 AQE 后可以自动调整合并小分区。

---

## 十二、实战技巧

### 调试建议

```java
// 开发阶段用 local 模式
SparkSession spark = SparkSession.builder()
    .master("local[4]")  // 本地4线程
    .appName("debug")
    .getOrCreate();

// 查看执行计划
df.explain(true);  // 输出物理执行计划

// 查看分区情况
System.out.println("分区数: " + df.rdd().getNumPartitions());

// 查看数据分布（排查倾斜）
df.groupBy("key").count().orderBy(desc("count")).show(20);
```

### 常用配置速查

```properties
# Shuffle 分区数（默认200，生产环境按数据量调整）
spark.sql.shuffle.partitions=400

# 广播 join 阈值（超过不自动广播）
spark.sql.autoBroadcastJoinThreshold=100mb

# 开启 AQE（Spark 3.0+，强烈推荐）
spark.sql.adaptive.enabled=true
spark.sql.adaptive.skewJoin.enabled=true
spark.sql.adaptive.coalescePartitions.enabled=true

# Kryo 序列化（比 Java 序列化快）
spark.serializer=org.apache.spark.serializer.KryoSerializer

# Executor 内存配置
spark.executor.memory=8g
spark.executor.memoryOverhead=2g

# 堆外内存
spark.memory.offHeap.enabled=true
spark.memory.offHeap.size=4g
```
