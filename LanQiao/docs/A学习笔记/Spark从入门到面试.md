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

**根本原因**：某些 key 的数据量远大于其他 key（如热门域名 baidu/taobao vs 普通小站）。

**识别方式**：Spark UI → Stage 详情 → Task 列表，看是否有少数 Task 执行时间是其他 Task 的 10 倍以上，Shuffle Read 中 Max >> Median。

---

### ⚠️ 核心：先区分场景，再选方案（面试必考）

三种倾斜场景本质不同，解法不能混用：

| 倾斜场景 | 数据特征 | 正确解法 |
|---------|---------|---------|
| **GROUP BY 聚合倾斜** | 某个 key 对应的行数极多，无第二张表 | Key 加盐 + 两阶段聚合 |
| **大表 JOIN 小表** | 一边数据量小，能装进内存 | Map Join（Broadcast） |
| **大表 JOIN 大表，某 key 倾斜** | 两边都大，但热点 key 的 dim 条数极少 | 热冷分离：热点用 Broadcast，其余正常 JOIN，最后 UNION |

**为什么不能都用广播？**
广播要把整张表 collect 到 Driver 再推给所有 Executor，**表必须能装进内存**。
两张大表（各几十 GB）根本放不进去，Driver 直接 OOM。
GROUP BY 场景更是没有第二张表，广播无从谈起。

**为什么不能都用加盐？**
GROUP BY 加盐完全可行，代价极低。
但 JOIN 场景加盐有硬伤：A 表 key 变成 `baidu_3`，B 表必须也有一条 `baidu_3` 才能匹配，
所以 **B 表每条记录必须复制 N 份**（N = 盐的范围）。
B 表 1 亿条 × 10 = 10 亿条，存储和 Shuffle 开销扩大 10 倍，代价很重。
当热点 key 已知且 dim 表条数极少时，热冷分离 + 局部 Broadcast 更精准、开销更小。

---

### 场景一：GROUP BY 聚合倾斜 → Key 加盐 + 两阶段聚合

**背景**：`UpdateFrequencyTableJob` 按 domain `GROUP BY` 统计 URL 数量，
baidu.com 有 5000 万条 URL，全部 Shuffle 到同一 Task，Task 跑了 6 小时。

**原理**：给 domain 加随机盐 `0~9`，把 `baidu.com` 的 5000 万条打散到 10 个 Task，
每个 Task 只处理 500 万条，局部 sum 后再全局 sum，结果完全等价。

```java
import static org.apache.spark.sql.functions.*;

// === 第一阶段：加盐，局部聚合 ===
// 给每行加一个 0~9 的随机盐，拼接成 salt_key
Dataset<Row> salted = df.withColumn("salt_key",
    concat(
        col("domain"),
        lit("_"),
        rand().multiply(10).cast("int").cast("string")
    ));

// 按 salt_key 局部 sum（baidu_0 ~ baidu_9 各自聚合一次，互不干扰）
Dataset<Row> stage1 = salted
    .groupBy("salt_key")
    .agg(
        sum("new_url_count").alias("partial_count"),
        first("domain").alias("domain")   // 恢复真实 domain
    );

// === 第二阶段：去盐，全局聚合 ===
// 把 baidu_0 ~ baidu_9 的 partial_count 再 sum 一次，得到 baidu.com 真实总数
Dataset<Row> result = stage1
    .groupBy("domain")
    .agg(sum("partial_count").alias("total_new_url_count"));
```

```
执行流程可视化：

baidu.com（5000万行）→ 加盐后分散到 10 个分区：
  baidu_0（500万）→ partial_sum = X0
  baidu_1（500万）→ partial_sum = X1
  ...
  baidu_9（500万）→ partial_sum = X9

第二阶段：X0 + X1 + ... + X9 = baidu.com 真实总量 ✅
每个 Task 最大数据量：500万（原来的 1/10）
```

---

### 场景二：大表 JOIN 小表 → Map Join（Broadcast）

**背景**：`url_table`（10亿条）JOIN `domain_config`（1万条域名配置），
`domain_config` 体积不到 10MB，直接广播。

**原理**：Driver 把小表 collect 到本地，序列化后推给所有 Executor，
每个 Executor 把小表放在本地内存 HashMap 里，大表每条记录直接本地查，
**彻底消除 Shuffle**。

```java
import static org.apache.spark.sql.functions.broadcast;

// 方式一：手动指定广播（推荐，明确）
Dataset<Row> result = urlTable
    .join(broadcast(domainConfig), "domain");

// 方式二：设置阈值，让 Spark 自动广播小于该大小的表（默认 10MB）
spark.conf().set("spark.sql.autoBroadcastJoinThreshold", "104857600"); // 100MB

// 方式三：SQL Hint
spark.sql("SELECT /*+ BROADCAST(b) */ a.*, b.config " +
          "FROM url_table a JOIN domain_config b ON a.domain = b.domain");
```

```
为什么不能广播大表：
Driver collect 大表 → Driver OOM（几十 GB 放不进 Driver 内存）
即使 Driver 没 OOM，广播到每个 Executor 也要几十 GB × Executor 数 = 爆内存
```

---

### 场景三：大表 JOIN 大表，热点 key 倾斜 → 热冷分离

**背景**：`url_table`（10亿条）JOIN `domain_info`（1亿条），
两张表都很大无法广播，但 baidu/taobao/jd 三个 domain 各有数千万条，
Shuffle 后全堆到 3 个 Task，整个 Stage 被这 3 个 Task 拖死。

**原理**：
- `domain_info` 中 baidu/taobao/jd 只有 **3 条**元数据，体积极小 → 可以 Broadcast
- `url_table` 中热点 URL 虽然多，但 JOIN 时走 Broadcast Hash JOIN，本地查，无 Shuffle
- 剩余 99.9% 的非热点数据均匀分布，正常 Shuffle JOIN 不倾斜

```java
// 预先识别热点 key（可业务指定，也可动态统计）
List<String> hotKeys = Arrays.asList("baidu.com", "taobao.com", "jd.com");
Object[] hotArr = hotKeys.toArray();

// === 热冷拆分 ===
// url_table 按热点/非热点拆成两份
Dataset<Row> urlHot  = urlTable.filter(col("domain").isin(hotArr));
Dataset<Row> urlCold = urlTable.filter(col("domain").isin(hotArr).not());

// domain_info 按热点/非热点拆成两份
// infoHot 只有 3 条记录（baidu/taobao/jd 的元数据），体积极小
Dataset<Row> infoHot  = domainInfo.filter(col("domain").isin(hotArr));
Dataset<Row> infoCold = domainInfo.filter(col("domain").isin(hotArr).not());

// === 分别处理 ===
// 热点部分：infoHot 只有 3 条 → 安全广播，本地查，零 Shuffle
Dataset<Row> hotResult = urlHot.join(broadcast(infoHot), "domain");

// 非热点部分：去掉热点后数据均匀 → 正常 Shuffle JOIN，不会倾斜
Dataset<Row> coldResult = urlCold.join(infoCold, "domain");

// === 合并结果 ===
Dataset<Row> finalResult = hotResult.union(coldResult);
```

```
为什么「热点 dim 只有 3 条」是关键：
url_table 热点部分：baidu/taobao/jd 共 1.5 亿行（行数多，体积大）
domain_info 热点部分：只有 3 行（每个域名一条元数据，几百字节）

广播的是 domain_info 热点部分（极小）✅
不是广播 url_table 热点部分（很大，不能广播）

如果 domain_info 热点部分也很大（比如每个 domain 有几千条属性）→ 退化为场景四：加盐
```

---

### 场景四（补充）：大表 JOIN 大表，热点 dim 也很大 → 加盐

当两张大表 JOIN，且热点 key 对应的 dim 表也有大量记录时，用加盐方案：

```java
// 步骤 1：A 表（主表）每行加随机盐 0~9
Dataset<Row> saltedA = bigTableA.withColumn("salted_key",
    concat(
        col("domain"),
        lit("_"),
        rand().multiply(10).cast("int").cast("string")
    ));

// 步骤 2：B 表（dim 表）每条记录复制 10 份，分别带盐 0~9
// explode 把数组 [0,1,...,9] 展开成 10 行
Dataset<Row> expandedB = bigTableB
    .withColumn("salt",
        explode(array(lit(0),lit(1),lit(2),lit(3),lit(4),
                      lit(5),lit(6),lit(7),lit(8),lit(9))))
    .withColumn("salted_key",
        concat(col("domain"), lit("_"), col("salt").cast("string")));

// 步骤 3：按 salted_key JOIN（baidu_3 能匹配到 baidu_3）
Dataset<Row> joined = saltedA.join(expandedB, "salted_key");

// 步骤 4：去盐，聚合最终结果
Dataset<Row> result = joined.groupBy("domain").agg(/* ... */);
```

**代价说明**：B 表 1 亿条 × 10 = 10 亿条，Shuffle 数据量增大 10 倍，是不得已的选择。

---

### AQE（Adaptive Query Execution，Spark 3.0+）— 生产推荐

```java
// Spark 3.0+ 默认开启，不需要额外配置
// 如果关闭了，手动开启：
spark.conf().set("spark.sql.adaptive.enabled", "true");
spark.conf().set("spark.sql.adaptive.skewJoin.enabled", "true");
// AQE 在运行时收集 Stage 统计信息，自动检测倾斜分区
// 将超大分区拆成多个子分区并行处理，无需手写 salt 逻辑
```

---

### 方案选型总结

| 方案 | 适用场景 | 改动成本 | 主要缺点 |
|------|---------|---------|---------|
| **Map Join（Broadcast）** | 大表 JOIN **小**表（< 几百MB）| 低 | 小表超内存 OOM |
| **Key 加盐 + 两阶段聚合** | GROUP BY 聚合倾斜 | 中 | 需重写聚合逻辑 |
| **热冷分离 + 局部 Broadcast** | 大表 JOIN 大表，已知热点，dim 条数极少 | 中 | 需提前知道热点 key |
| **加盐（JOIN 场景）** | 大表 JOIN 大表，热点 dim 也很大 | 高 | B 表复制 N 倍，开销重 |
| **提高并行度** | 轻微不均（非热点 key 问题）| 低 | 热点 key 问题无效 |
| **AQE（Spark 3.0+）** | 通用，运行时自动处理 | 极低 | 需 Spark 3.0+ |

**选型决策树：**
```
遇到倾斜
  │
  ├── GROUP BY 聚合？→ Key 加盐 + 两阶段聚合
  │
  └── JOIN 倾斜？
        ├── 一边小（能放内存）→ Broadcast（最优）
        └── 两边都大
              ├── 热点 dim 条数极少（只有几条）→ 热冷分离 + 局部 Broadcast
              └── 热点 dim 也很大 → 加盐（B 表复制 N 份）
              （以上都不好处理 → 开 AQE 让 Spark 自动处理）
```

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

---

## 十三、Spark 部署模式

### Client 模式 vs Cluster 模式

| 对比项 | Client 模式 | Cluster 模式 |
|--------|-------------|--------------|
| Driver 运行位置 | 提交任务的客户端机器（本地） | 集群内某个 Worker/NodeManager 节点 |
| 网络依赖 | Driver 与 Executor 保持长连接，客户端网络断开则任务失败 | Driver 在集群内，网络更稳定 |
| 日志查看 | 直接在本地终端看到 Driver 日志 | 需登录集群节点或通过 Web UI 查看 |
| 适用场景 | 本地调试、交互式开发（spark-shell） | 生产环境批处理任务、长时间运行任务 |
| 资源管理 | 客户端需保持运行 | 提交后客户端可退出 |

```bash
# Client 模式提交
spark-submit --master yarn --deploy-mode client --class com.example.MyJob job.jar

# Cluster 模式提交（生产推荐）
spark-submit --master yarn --deploy-mode cluster --class com.example.MyJob job.jar
```

### 三种集群模式对比

| 模式 | 特点 | 适用场景 |
|------|------|----------|
| Standalone | Spark 自带的调度器，部署简单，无需额外组件 | 独占集群、快速验证 |
| YARN | 与 Hadoop 生态深度集成，共享集群资源 | 企业大数据平台（美团/字节主流） |
| Kubernetes | 容器化部署，弹性伸缩，云原生 | 云环境、微服务架构 |

### YARN 模式角色详解

```
提交命令
  │
  ▼
ResourceManager（RM）          ← 集群资源总管，负责资源分配与任务调度
  │  分配 Container 启动 AM
  ▼
ApplicationMaster（AM）        ← Spark 的 AM 就是 Driver（Cluster模式）
  │  向 RM 申请 Executor Container
  ▼
NodeManager（NM）× N           ← 每台机器上的资源代理，管理本节点 Container
  │  启动 Executor 进程
  ▼
Executor × N                   ← 执行具体 Task
```

**关键流程（YARN Cluster 模式）**：

1. `spark-submit` 向 RM 提交应用，RM 在某个 NM 上启动 AM Container
2. AM 内部启动 Driver（SparkContext），向 RM 申请 Executor 所需的 Container
3. RM 根据可用资源在各 NM 上分配 Container，NM 启动 Executor 进程
4. Executor 向 Driver 注册，Driver 开始调度 Task

**面试追问**：AM 挂了怎么办？→ RM 会重新启动 AM（重试次数由 `yarn.resourcemanager.am.max-attempts` 控制，默认 2）。


---

## 十四、Checkpoint 与 RDD 血统

### Lineage（血统）容错原理

RDD 的核心容错机制：每个 RDD 记录了从父 RDD 如何转换而来的完整依赖链（Lineage）。某个分区数据丢失时，Spark 沿血统链向上回溯，**只重算丢失的分区**，不需要全量重跑。

```
原始数据 → rdd1.map() → rdd2.filter() → rdd3.groupBy() → rdd4.join()
```

若 rdd4 某分区丢失 → 从 rdd3 对应分区重新计算 → 层层回溯到源头。

**血统过长的问题**：迭代算法（如 PageRank、机器学习训练）每轮迭代叠加一层依赖，血统链可能达到数百层。一旦出错，恢复代价极大，且 DAG 图序列化本身也会 OOM。

### 什么时候使用 Checkpoint

| 场景 | 建议 |
|------|------|
| 迭代算法（血统超过 20 层） | 必须 checkpoint |
| 宽依赖（shuffle）后的关键 RDD | 建议 checkpoint |
| 生命周期跨越多个 Action | 建议 checkpoint |
| 简单线性血统，数据可快速重算 | 不需要 |

### Checkpoint vs Cache 区别

| 对比项 | Cache / Persist | Checkpoint |
|--------|-----------------|------------|
| 存储位置 | Executor 内存 / 本地磁盘 | HDFS（可靠分布式存储） |
| 血统是否截断 | 否，血统完整保留 | 是，血统截断，checkpoint 成为新起点 |
| 生命周期 | 应用结束后自动清除 | 持久化，跨应用可复用 |
| 容错能力 | 依赖血统恢复 | 直接从 checkpoint 文件恢复，无需回溯 |
| 开销 | 较小 | 需要额外一次 Action 触发写 HDFS |

**最佳实践**：checkpoint 前先 cache，避免重算两次。

```java
// checkpoint 前先 cache（避免重算）
JavaRDD<String> rdd = sc.textFile("hdfs://...")
    .filter(line -> line.contains("error"))
    .cache();  // 先 cache

// 设置 checkpoint 目录（HDFS 路径）
sc.setCheckpointDir("hdfs:///spark-checkpoints/job1");

rdd.checkpoint();  // 标记需要 checkpoint（懒执行）
rdd.count();       // 触发 Action，checkpoint 实际写入

// 此后 rdd 的血统被截断，从 HDFS 读取
JavaRDD<String> filtered = rdd.filter(line -> line.length() > 10);
filtered.count();
```

**面试追问**：checkpoint 为什么要先 cache？→ checkpoint 触发时 Spark 会重新计算一遍 RDD（从头到尾），如果没有 cache，同一份数据会被计算两次（一次用于之前的 Action，一次用于写 checkpoint）。


---

## 十五、Spark 3.x 新特性

### AQE（Adaptive Query Execution，自适应查询执行）

Spark 3.0 引入，**在运行时根据实际统计信息动态调整执行计划**，解决了静态执行计划的三大痛点：

#### 1. 自动合并小分区（Coalesce Small Partitions）

Shuffle 完成后，大量分区数据极少（甚至为空）。AQE 自动将相邻小分区合并，减少 Task 数量和调度开销。

```properties
spark.sql.adaptive.enabled=true
spark.sql.adaptive.coalescePartitions.enabled=true
spark.sql.adaptive.coalescePartitions.minPartitionNum=1
spark.sql.adaptive.advisoryPartitionSizeInBytes=64mb  # 目标分区大小
```

#### 2. 自动切换 Broadcast Join

执行前统计信息不准（如 filter 过滤了大量数据），导致原本 Sort Merge Join 的表实际很小。AQE 在 Shuffle 后重新评估，若某侧数据量低于阈值，自动切换为 Broadcast Join。

```properties
spark.sql.adaptive.autoBroadcastJoinThreshold=30mb  # 默认等于 autoBroadcastJoinThreshold
```

#### 3. 倾斜 Join 自动处理（Skew Join Optimization）

检测到某分区数据量远超中位数（默认 5 倍且超过 256MB），自动将该分区拆分为多个子分区并行处理，匹配侧分区相应复制。

```properties
spark.sql.adaptive.skewJoin.enabled=true
spark.sql.adaptive.skewJoin.skewedPartitionFactor=5       # 倾斜判定倍数
spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes=256mb
```

**与手动加盐方案的对比**：AQE 自动处理无需改代码，但只适用于 Join 场景；手动加盐可处理 groupBy 等更多算子的倾斜。

### Dynamic Partition Pruning（动态分区裁剪，DPP）

**解决的问题**：星型模型查询中，维度表过滤条件无法提前传递给事实表，导致事实表全量 Scan。

**原理**：将维度表过滤后的分区值集合（作为子查询/广播变量）注入到事实表的 Scan 阶段，在读数据时就跳过不需要的分区。

```sql
-- 示例：查询 2024 年北京地区的订单
SELECT o.order_id, o.amount
FROM orders o
JOIN dim_date d ON o.date_id = d.date_id
JOIN dim_region r ON o.region_id = r.region_id
WHERE d.year = 2024 AND r.city = '北京'
```

DPP 会将 `year=2024` 和 `city=北京` 的 ID 集合广播，orders 表 Scan 时直接过滤对应分区，大幅减少 IO。

**触发条件**：
- 表必须是分区表（Hive 分区表或 Parquet 等分区格式）
- Join Key 必须是分区列
- 维度表侧需要能被广播（或开启 `spark.sql.optimizer.dynamicPartitionPruning.reuseBroadcastOnly=false`）

```properties
spark.sql.optimizer.dynamicPartitionPruning.enabled=true  # 默认开启
```

### Spark Connect（3.4+）

**解决的问题**：传统 Spark 客户端与 Server 强耦合（同进程或 Thrift JDBC），版本绑定严格，语言支持受限。

**原理**：基于 **gRPC + Protobuf** 的客户端-服务端分离架构。客户端（任意语言）构建逻辑计划的 Protobuf 表示，通过 gRPC 发送给 Spark Server，Server 负责优化和执行。

**核心优势**：
- 客户端轻量，不依赖 Spark 核心 jar
- 支持多语言（Python、Scala、Java、R、Go）
- 版本解耦，客户端可跨版本连接 Server
- 天然支持远程调试和多租户

```java
// Spark Connect Java 客户端示例
SparkSession spark = SparkSession.builder()
    .remote("sc://localhost:15002")  // 连接远程 Spark Server
    .build();

Dataset<Row> df = spark.read().parquet("hdfs:///data/logs");
df.filter(col("status").equalTo(200)).show();
```

**面试定位**：Spark Connect 是 Spark 向"云原生计算引擎"演进的关键一步，类似 Flight SQL 对 Arrow 的意义。


---

## 十六、结合项目的高频追问

> 以下问题基于 `llm-spider-jobs` 实际项目场景，面试官看到 Spark 经历必问。

### Q：爬虫日志分析场景，为什么选 Spark 而不是 Flink？

**参考回答**：

我们的 Sitemap URL 频度评分是**离线批处理任务**，有以下特点：

1. **数据有边界**：每天定时跑一次，处理前一天全量数据，天然是有界数据集
2. **延迟要求不高**：T+1 产出频度等级，分钟级延迟无意义
3. **依赖 Hive 表关联**：多张 Hive 表的 JOIN 和聚合，Spark SQL 生态更成熟
4. **状态逻辑简单**：只需要当天数据 + 历史累计表，不需要 Flink 的复杂窗口/状态管理

如果需求变成**实时感知新链接出现**（比如 Sitemap 更新秒级响应），则应切换为 Flink。

### Q：Sitemap URL 频度评分 Job 中，如何设置合适的 shuffle partitions？

**参考回答**：

核心公式：`合理分区数 = Shuffle 输出总数据量 / 目标单分区大小（128MB）`

实际操作步骤：

```java
// 1. 先用默认值跑一次，观察 Shuffle Write 总量（Spark UI -> Stage -> Shuffle Write）
// 假设 Shuffle Write = 20GB

// 2. 计算建议分区数
// 20GB / 128MB ≈ 160 个分区

// 3. 设置（通常取 2 的幂次或核心数的整数倍）
spark.conf().set("spark.sql.shuffle.partitions", "160");

// 4. 开启 AQE 自动合并小分区（兜底策略）
spark.conf().set("spark.sql.adaptive.enabled", "true");
spark.conf().set("spark.sql.adaptive.coalescePartitions.enabled", "true");
```

**项目实际情况**：域名级别聚合（去重后约 500 万域名），Shuffle 数据量约 5-10GB，设置 `shuffle.partitions=100`，开启 AQE 后实际合并到约 60 个分区，每个 Task 约 100-150MB，性能较默认的 200 分区提升约 30%。

### Q：UDF 中的域名提取（EffectiveTldFinder）在 Spark 中如何注册和使用？

**参考回答**：

`EffectiveTldFinder` 来自 `crawler-commons` 库，用于提取有效域名（如从 `news.bbc.co.uk` 提取 `bbc.co.uk`）。

**注册步骤**：

```java
// 1. 定义 UDF（注意：UDF 实现需可序列化，EffectiveTldFinder 是静态工具类，安全）
UDF1<String, String> extractDomainUdf = (String url) -> {
    if (url == null || url.isEmpty()) return null;
    try {
        InternetDomainName domain = EffectiveTldFinder.getAssignedDomain(url, true, true);
        return domain != null ? domain.toString() : null;
    } catch (Exception e) {
        return null;  // 非法 URL 静默回退
    }
};

// 2. 注册到 SparkSession
spark.udf().register("extract_domain", extractDomainUdf, DataTypes.StringType);

// 3. 在 SQL 中使用
spark.sql("SELECT extract_domain(url) as domain, count(*) FROM sitemap_urls GROUP BY 1");

// 4. 或在 Dataset API 中使用
import static org.apache.spark.sql.functions.callUDF;
df.withColumn("domain", callUDF("extract_domain", col("url")));
```

**踩坑点**：UDF 中使用的第三方类必须在 Executor classpath 上，需要在 `spark-submit` 时通过 `--jars` 或 `--packages` 引入，或打入 fat jar。

### Q：离线任务中如何避免小文件问题（coalesce 在输出前合并分区）？

**参考回答**：

小文件问题来源：Spark 默认每个 partition 输出一个文件，若 `shuffle.partitions=200` 且数据量小，会产生大量几 KB 的小文件，严重影响 HDFS NameNode 和下游读取性能。

**解决方案**：

```java
// 方案1：输出前 coalesce 合并分区（推荐，无 shuffle）
// 适合：只是减少分区数，不需要重新分布数据
Dataset<Row> result = computeResult();
int outputPartitions = 10;  // 根据数据量估算：总数据量 / 目标文件大小（如 256MB）
result.coalesce(outputPartitions)
      .write()
      .mode(SaveMode.Overwrite)
      .parquet("hdfs:///output/frequency_scores/dt=2024-01-01");

// 方案2：repartition（有 shuffle，但分布更均匀）
// 适合：当前分区数据严重不均衡时
result.repartition(outputPartitions)
      .write()
      .parquet("hdfs:///output/...");

// 方案3：开启 AQE 自动合并（Spark 3.0+，全局生效）
spark.conf().set("spark.sql.adaptive.coalescePartitions.enabled", "true");
spark.conf().set("spark.sql.adaptive.advisoryPartitionSizeInBytes", "256mb");
```

**coalesce vs repartition 核心区别**：
- `coalesce`：窄依赖，无 shuffle，只能减少分区数，可能产生数据倾斜
- `repartition`：宽依赖，有 shuffle，可增减分区，数据均匀分布

**项目实践**：DI 日快照表输出时，先计算数据量（约 500MB），用 `coalesce(2)` 合并为 2 个 256MB 文件，写入 Hive 分区目录，避免 NameNode 元数据膨胀。


---

## 补充面试题 Q21–Q30

### Q21：Spark 如何实现容错？Lineage 和 Checkpoint 分别什么时候用？

**容错机制**：

Spark 容错基于两层机制：

1. **Lineage 重算**：RDD 记录完整的转换血统，分区丢失时沿血统重算，无需全量回滚。窄依赖只需重算丢失分区；宽依赖（shuffle）需重算上游所有分区。

2. **Checkpoint 持久化**：将 RDD 快照写入可靠存储（HDFS），截断血统，后续恢复直接从 checkpoint 读取。

**选择原则**：

| 场景 | 使用方式 |
|------|----------|
| 血统短（< 10 层），数据可快速重算 | 只用 Lineage，不需要 checkpoint |
| 迭代算法（PageRank、ML 训练），血统超过 20 层 | 必须 checkpoint，每隔若干轮 checkpoint 一次 |
| shuffle 后的关键中间结果，计算代价高 | checkpoint + cache 双重保障 |
| 跨应用复用中间结果 | checkpoint 到 HDFS |

---

### Q22：什么是谓词下推（Predicate Pushdown）？在 Spark 中如何工作？

**定义**：将过滤条件（WHERE/filter）尽早下推到数据源层执行，减少读入内存的数据量。

**工作原理**（Catalyst 优化器负责）：

```
原始逻辑计划：
  Filter(status = 200)
    └── Scan(parquet: logs)   ← 先全量读，再过滤

优化后（谓词下推）：
  Scan(parquet: logs, filter: status = 200)  ← 读文件时直接跳过不符合的 Row Group
```

**Parquet 文件的谓词下推**：Parquet 每个 Row Group 存储列的 min/max 统计信息。谓词下推后，Spark 在读文件时直接跳过不满足条件的 Row Group，IO 大幅减少。

**JDBC 数据源的谓词下推**：filter 条件被翻译为 SQL WHERE 子句推给数据库执行。

```java
// 验证谓词下推是否生效
df.filter(col("status").equalTo(200))
  .explain(true);  // 查看 Physical Plan 中是否有 PushedFilters
```

**注意**：UDF 中的过滤条件**无法被谓词下推**，因为 Catalyst 无法分析 UDF 内部逻辑。

---

### Q23：Spark 的内存溢出（OOM）常见原因及排查思路

**OOM 类型与原因**：

| OOM 类型 | 原因 | 定位方式 |
|----------|------|----------|
| Driver OOM | `collect()` 把大数据拉到 Driver；广播变量过大 | Spark UI Driver 日志，看 `java.lang.OutOfMemoryError` |
| Executor Heap OOM | 单分区数据过大（数据倾斜）；大对象缓存；Shuffle 溢写不及时 | Executor 日志，看 GC overhead / heap space |
| Executor Off-Heap OOM | `spark.memory.offHeap` 配置不足；Native 代码内存泄漏 | `Container killed by YARN for exceeding memory limits` |
| GC 过度 | Execution Memory 过小，频繁 Full GC | `spark.executor.extraJavaOptions=-verbose:gc` |

**排查步骤**：

```
1. Spark UI → Stages → 找到失败 Stage → 查看 Executor 日志
2. 关注 Shuffle Read Size：单 Task 超过 2GB 大概率是倾斜导致 OOM
3. 查看 Storage Memory 使用率：接近上限说明 cache 过多
4. jmap -heap <pid> 查看堆内存分布（Executor 进程还在时）
```

**常见解决方案**：

```properties
# 增加 Executor 内存
spark.executor.memory=8g
spark.executor.memoryOverhead=2g   # 堆外（NIO/shuffle buffer）

# 增加堆外内存（Arrow/ORC 列式处理）
spark.memory.offHeap.enabled=true
spark.memory.offHeap.size=4g

# 减小 collect 数据量，改用 take/limit
# 开启 AQE 自动拆分倾斜分区
spark.sql.adaptive.skewJoin.enabled=true
```

---

### Q24：collect() 的使用风险是什么？如何避免？

**风险**：`collect()` 将所有 Executor 上的数据全部传输到 Driver 内存，数据量超过 Driver 堆大小直接 OOM，且网络传输开销大。

**替代方案**：

```java
// 危险：collect 全量数据
List<Row> all = df.collect();  // 千万行数据直接 OOM

// 方案1：取样查看（调试用）
List<Row> sample = df.take(100);
df.show(20);

// 方案2：统计聚合后再 collect（结果集小）
List<Row> counts = df.groupBy("status").count().collect();

// 方案3：写出到存储，不拉到 Driver
df.write().parquet("hdfs:///output/result");

// 方案4：limit 限制后再 collect
List<Row> limited = df.limit(1000).collect();

// 方案5：需要完整数据时用 toLocalIterator（流式，避免一次性装载）
Iterator<Row> iter = df.toLocalIterator();
while (iter.hasNext()) {
    Row row = iter.next();
    // 逐行处理
}
```

---

### Q25：Spark 中如何处理小文件问题？

**小文件的危害**：NameNode 元数据压力大（每个文件约 150 字节元数据）；Spark 读取时每个小文件启动一个 Task，Task 调度开销远超计算开销；Parquet 小文件无法充分利用 Row Group 级别的统计信息。

**产生原因**：
- `shuffle.partitions` 设置过大，数据量小时每分区几 KB
- 流式写入（Structured Streaming）每个 batch 产生新文件
- 动态分区写入时，每个分区内数据量少

**解决方案**：

```java
// 1. 写出前合并分区（最常用）
df.coalesce(targetPartitions)
  .write().parquet(outputPath);

// 2. 开启 AQE 自动合并（Spark 3.0+）
spark.conf().set("spark.sql.adaptive.coalescePartitions.enabled", "true");
spark.conf().set("spark.sql.adaptive.advisoryPartitionSizeInBytes", "256mb");

// 3. 读取时合并小文件（Parquet 特有）
spark.conf().set("spark.sql.files.maxPartitionBytes", "268435456");  // 256MB
spark.conf().set("spark.sql.files.openCostInBytes", "4194304");      // 4MB，小文件合并阈值

// 4. Hive 表定期执行 COMPACT（对 ORC 格式）
spark.sql("ALTER TABLE my_table PARTITION(dt='2024-01-01') COMPACT 'MAJOR'");
```

**目标文件大小**：HDFS 块大小（128MB 或 256MB）是单个 Parquet/ORC 文件的合理目标大小。


---

### Q26：YARN Client 模式和 Cluster 模式的核心区别？

**核心区别**：Driver 运行在哪里。

| 项目 | Client 模式 | Cluster 模式 |
|------|-------------|--------------|
| Driver 位置 | 提交任务的客户端进程内 | 集群中某个 NodeManager 的 Container 内 |
| 客户端断开 | 任务失败（Driver 死亡） | 任务继续运行（Driver 在集群内） |
| 日志 | 直接打印到本地终端 | 需通过 YARN Web UI 或 `yarn logs` 查看 |
| 网络传输 | Driver 与所有 Executor 跨网络通信，若客户端不在机房则延迟高 | Driver 与 Executor 在机房内网通信，延迟低 |
| 适用场景 | 开发调试、spark-shell 交互 | 生产批处理、长时间运行任务 |

**ApplicationMaster 的角色差异**：
- Client 模式：AM 只负责申请 Executor Container，Driver 逻辑在客户端
- Cluster 模式：AM 就是 Driver，AM Container 同时承担调度和执行 Driver 代码

```bash
# 生产环境标准提交命令
spark-submit \
  --master yarn \
  --deploy-mode cluster \
  --num-executors 20 \
  --executor-memory 8g \
  --executor-cores 4 \
  --class com.sankuai.llm.UpdateFrequencyJob \
  /path/to/job.jar
```

---

### Q27：Spark 如何保证 Exactly-Once 语义（Structured Streaming）？

**背景**：Structured Streaming 相比 Spark Streaming 提供更强的一致性保证。

**三层保障机制**：

1. **Source 端（At-Least-Once 读取 + Offset 记录）**：
   - Kafka Source 每个 batch 结束后将消费的 offset 写入 checkpoint 目录
   - 失败重试时从 checkpoint 记录的 offset 重新消费（可能重复读，但不丢失）

2. **处理端（幂等转换）**：
   - Structured Streaming 的状态操作（聚合、deduplication）基于 WAL（Write-Ahead Log）保证状态一致性
   - 失败重启后从 checkpoint 恢复状态，重放同一批数据得到相同结果

3. **Sink 端（Exactly-Once 写出）**：
   - **幂等 Sink**（如写 Parquet 到 HDFS）：使用临时文件 + 原子 rename，同一 batch 重试不会产生重复文件
   - **事务 Sink**（如 `foreachBatch` + 数据库事务）：利用 batch ID 作为幂等键，重复提交时跳过

```java
// 使用 foreachBatch 实现 Exactly-Once 写入 MySQL
streamingDF.writeStream()
    .foreachBatch((batchDF, batchId) -> {
        // batchId 作为幂等键，重试时同一 batchId 不重复写入
        batchDF.write()
               .mode(SaveMode.Append)
               .jdbc(jdbcUrl, "result_table", props);
    })
    .option("checkpointLocation", "hdfs:///checkpoints/streaming_job")
    .start();
```

**注意**：Kafka Sink 不支持原生 Exactly-Once，需配合 Kafka 事务（`enable.idempotence=true`）或业务层幂等。

---

### Q28：什么是 Catalyst 优化器？有哪些优化规则？

**定义**：Spark SQL 的查询优化引擎，基于**规则（Rule-Based）和代价（Cost-Based）**两种优化策略，将逻辑计划转换为高效的物理执行计划。

**四阶段优化流程**：

```
SQL/DataFrame API
      │
      ▼
未解析逻辑计划（Unresolved Logical Plan）
      │ Analyzer（解析列名、类型绑定）
      ▼
已解析逻辑计划（Analyzed Logical Plan）
      │ Optimizer（规则优化）
      ▼
优化后逻辑计划（Optimized Logical Plan）
      │ Planner（选择物理执行策略）
      ▼
物理执行计划（Physical Plan）
      │ Code Generation（Tungsten 字节码生成）
      ▼
执行
```

**常见优化规则**：

| 优化规则 | 说明 |
|----------|------|
| 谓词下推（Predicate Pushdown） | filter 尽早执行，减少数据量 |
| 列裁剪（Column Pruning） | 只读取 SELECT 中涉及的列（Parquet 列式存储效果显著）|
| 常量折叠（Constant Folding） | `WHERE 1+1=2` 编译期直接计算为 `true` |
| Join 重排序（Join Reordering） | CBO 根据统计信息选择最优 Join 顺序（需 `ANALYZE TABLE`）|
| 子查询展开（Subquery Elimination） | 将相关子查询转换为 Join |
| 分区裁剪（Partition Pruning） | 结合 DPP 跳过不需要的分区 |

```java
// 查看 Catalyst 优化过程
df.filter(col("status").equalTo(200))
  .select("url", "status")
  .explain(ExplainMode.Extended());  // 输出 Analyzed/Optimized/Physical Plan
```

---

### Q29：mapPartitions 和 foreachPartition 的区别？

| 对比项 | `mapPartitions` | `foreachPartition` |
|--------|-----------------|---------------------|
| 类型 | Transformation（懒执行） | Action（立即触发） |
| 返回值 | 返回新 RDD/Dataset | 无返回值（void） |
| 典型用途 | 对每个分区做转换，返回结果 | 对每个分区做副作用操作（写 DB、发 HTTP 请求）|
| 资源管理 | 分区内共享连接等重量级资源 | 同左，避免每条记录创建连接 |

```java
// mapPartitions：批量转换，每分区共享一个 HTTP Client
JavaRDD<String> enriched = rdd.mapPartitions(iter -> {
    HttpClient client = HttpClient.newHttpClient();  // 分区内复用
    List<String> results = new ArrayList<>();
    while (iter.hasNext()) {
        results.add(enrich(client, iter.next()));
    }
    return results.iterator();
});

// foreachPartition：批量写入数据库，每分区共享一个连接
rdd.foreachPartition(iter -> {
    Connection conn = DriverManager.getConnection(jdbcUrl);  // 分区内复用
    PreparedStatement ps = conn.prepareStatement("INSERT INTO ...");
    while (iter.hasNext()) {
        Row row = iter.next();
        ps.setString(1, row.getString(0));
        ps.addBatch();
    }
    ps.executeBatch();
    conn.close();
});
```

**性能关键点**：`map`/`foreach` 每条记录创建一次连接（N 次），`mapPartitions`/`foreachPartition` 每分区创建一次（M 次，M << N），适合数据库写入、HTTP 调用等重量级操作。

---

### Q30：为什么 Spark SQL 比手写 RDD 更快？

**核心原因**：Spark SQL 有 Catalyst 优化器 + Tungsten 执行引擎两大加速器，手写 RDD 绕过了这两层优化。

**1. Catalyst 优化器**（逻辑层优化）：
- 谓词下推、列裁剪等规则优化，减少数据读取量
- CBO（代价优化）选择最优 Join 顺序
- AQE 运行时动态调整执行计划

**2. Tungsten 执行引擎**（物理层优化）：
- **堆外内存管理**：绕过 JVM GC，直接操作内存，减少 GC 停顿
- **缓存友好的数据结构**：列式存储（UnsafeRow），CPU Cache 命中率高
- **全阶段代码生成（Whole-Stage CodeGen）**：将多个算子融合成一个 JVM 字节码函数，消除虚函数调用开销和中间对象分配

```java
// 手写 RDD：每条记录经过多次虚函数调用，产生大量中间对象
rdd.map(row -> parse(row))
   .filter(parsed -> parsed.getStatus() == 200)
   .mapToPair(parsed -> new Tuple2<>(parsed.getDomain(), 1L))
   .reduceByKey(Long::sum);

// Spark SQL：Catalyst 优化 + Tungsten 代码生成，整个 pipeline 编译为单个函数
spark.sql("SELECT domain, count(*) FROM logs WHERE status=200 GROUP BY domain");
```

**直观类比**：手写 RDD 像写解释型脚本，Spark SQL 像写 C 代码——最终都实现同样逻辑，但后者经过深度编译优化，性能差异可达 2-10 倍。

**例外情况**：复杂自定义逻辑（如图算法、自定义迭代）RDD 更灵活；UDF 会破坏部分优化（无法谓词下推、无法代码生成）。

