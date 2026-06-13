# Flink 从入门到面试

> 对标 Spark笔记，覆盖面试高频考点。重点：时间语义、Checkpoint、Exactly-Once、状态管理、背压。

---

## 一、Flink vs Spark Streaming（必考对比题）

| 维度 | Spark Streaming | Flink |
|------|----------------|-------|
| 处理模型 | **微批**（把流切成小批次跑） | **真流**（每条数据到来立刻处理） |
| 延迟 | 秒级（批次间隔决定） | **毫秒级** |
| 时间语义 | 以处理时间为主 | 原生支持 EventTime / ProcessingTime / IngestionTime |
| 状态管理 | 较弱 | **强大**（RocksDB/内存，支持大状态） |
| Checkpoint | 基于 RDD 血缘 | **精确一次**，基于 Chandy-Lamport 算法 |
| 吞吐量 | 高（批处理优化） | 高（流式但吞吐也很强） |
| 适用场景 | 离线/准实时，与Spark生态集成 | **实时性要求高**，有状态计算，复杂事件处理 |

**记忆口诀：** Spark = 微批（秒级）；Flink = 真流（毫秒级），有状态实时计算选 Flink。

---

## 二、Flink 架构（三个核心组件）

```
Client
  │── 提交 Job（JobGraph）
  ▼
JobManager（Master）
  │── JobMaster：调度 Task，协调 Checkpoint
  │── ResourceManager：管理 TaskManager 资源
  │── Dispatcher：接受 Job 提交，启动 JobMaster
  ▼
TaskManager（Worker）
  │── 执行实际计算（每个 TaskManager 有若干 TaskSlot）
  │── TaskSlot：资源隔离单元（CPU+内存的一份）
```

**TaskSlot 共享**：默认同一 Job 的不同 Task 可以共享 Slot，充分利用资源。

---

## 三、时间语义（面试高频）

Flink 三种时间：

| 时间类型 | 含义 | 使用场景 |
|---------|------|---------|
| **EventTime** | 事件实际发生时间（数据里的字段） | 精确计算，能处理乱序 |
| **IngestionTime** | 数据进入 Flink 的时间 | 折中方案 |
| **ProcessingTime** | 算子处理数据的时间 | 简单场景，不考虑乱序 |

**工业界几乎全用 EventTime**，原因：
- 网络延迟、消息积压都可能导致数据乱序到达
- 用处理时间统计，结果不准确

---

## 四、Watermark（水位线）—— EventTime 的核心

### 是什么

Watermark 是一个时间戳，表示"**这个时间点之前的数据已经全部到达**"。

```
Watermark(t) = max(EventTime seen so far) - 允许延迟时间
```

### 作用

- 触发窗口计算的信号
- 告诉 Flink：可以认为 t 之前的数据都到齐了，窗口可以关闭计算

### 乱序数据处理

```
事件时间：5, 3, 6, 2, 7, 4（乱序到达）
允许延迟：2秒
Watermark = max(EventTime) - 2

当 Watermark >= 窗口结束时间 → 触发窗口计算
Watermark 之前但还没到的数据 → 晚到数据（late data），默认丢弃
```

**晚到数据处理方案**：
```java
stream.allowedLateness(Time.seconds(5))  // 窗口关闭后再等5秒
      .sideOutputLateData(lateTag)        // 超过的放到旁路输出，不丢弃
```

---

## 五、窗口（Window）—— 四种类型

### 按时间划分

| 类型 | 说明 | 示例 |
|------|------|------|
| **Tumbling Window（滚动窗口）** | 固定大小，不重叠 | 每1分钟统计一次点击数 |
| **Sliding Window（滑动窗口）** | 固定大小，可重叠 | 最近5分钟每1分钟更新一次 |
| **Session Window（会话窗口）** | 按活跃间隔划分，无固定大小 | 用户30秒无操作则一次会话结束 |

### 按数量划分

| 类型 | 说明 |
|------|------|
| **Count Window** | 每N条数据触发一次计算 |

```java
// 滚动窗口示例
stream.keyBy(event -> event.userId)
      .window(TumblingEventTimeWindows.of(Time.minutes(1)))
      .sum("clickCount");
```

---

## 六、Checkpoint —— 最重要的容错机制

### 是什么

Checkpoint 是 Flink 定期把**所有算子的状态**快照持久化到存储（HDFS/OSS）的机制。

任务崩溃后从最近一次成功的 Checkpoint 恢复，保证数据不丢。

### Chandy-Lamport 算法（面试爱问原理）

```
JobManager 向所有 Source 注入 Barrier（屏障，一个特殊标记）
Barrier 随数据流向下游传播
每个算子收到所有输入通道的 Barrier 后：
  1. 把当前 state 快照写入存储
  2. 把 Barrier 继续传给下游
所有算子都完成快照 → 一次 Checkpoint 完成
```

**Barrier 对齐 vs 非对齐**：
- **对齐**（默认）：等所有输入通道的 Barrier 都到了才处理，可能增加延迟
- **非对齐**（Flink 1.11+）：Barrier 到了立刻处理，延迟更低但 Checkpoint 数据更多

### 配置示例

```java
env.enableCheckpointing(60000);  // 每60秒做一次Checkpoint
env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
env.getCheckpointConfig().setMinPauseBetweenCheckpoints(30000); // 两次Checkpoint最小间隔
env.getCheckpointConfig().setCheckpointTimeout(120000); // 超时时间
```

---

## 七、Exactly-Once 精确一次语义

### 三种语义

| 语义 | 含义 | 实现 |
|------|------|------|
| At Most Once | 最多处理一次，可能丢数据 | 不做任何保障 |
| At Least Once | 至少处理一次，可能重复 | Checkpoint 但不做幂等 |
| **Exactly Once** | 精确处理一次，不丢不重 | **Checkpoint + 幂等写入 or 两阶段提交** |

### Flink + Kafka Exactly-Once 实现

```
两阶段提交（2PC）：
① pre-commit：Checkpoint 时把数据写入 Kafka 事务（未提交）
② commit：所有算子 Checkpoint 成功后，提交 Kafka 事务
   任务失败：事务回滚，从上一个 Checkpoint 重新消费

关键配置：
- Kafka Source：Checkpoint 托管 offset（不能用自动提交）
- Kafka Sink：FlinkKafkaProducer 设置 EXACTLY_ONCE 语义
```

**注意**：Exactly-Once 是端到端的，Sink 也要支持事务或幂等写入。Redis INCR 是幂等的（天然支持），数据库写入需要用 upsert（主键冲突则更新）。

---

## 八、State 状态管理

### 两种状态类型

| 类型 | 说明 | 示例 |
|------|------|------|
| **Keyed State** | 每个 key 独立的状态 | 每个 userId 的点赞数 |
| **Operator State** | 算子级别的状态，不按 key 区分 | Kafka 消费的 offset |

### 常用状态类型

```java
// ValueState：单值
ValueState<Long> countState = getRuntimeContext()
    .getState(new ValueStateDescriptor<>("count", Long.class));

// ListState：列表
// MapState：Map，key-value
// ReducingState：自动聚合
```

### State Backend（状态存储在哪）

| Backend | 存储位置 | 适用场景 |
|---------|---------|---------|
| **MemoryStateBackend** | JVM 堆内存 | 开发/测试，状态量小 |
| **FsStateBackend** | 本地内存 + Checkpoint 到 HDFS | 中等状态量 |
| **RocksDBStateBackend** | 本地 RocksDB（磁盘）+ Checkpoint 到 HDFS | **生产推荐**，大状态量 |

---

## 九、背压（Backpressure）

### 是什么

下游算子处理速度 < 上游产生速度 → 下游缓冲区满 → 反向通知上游减速 → **背压**

### 如何检测

Flink Web UI 的 Backpressure 面板：
- `OK`：无背压
- `LOW`：轻微背压
- `HIGH`：严重背压，需要处理

### 常见原因和解法

| 原因 | 解法 |
|------|------|
| 下游算子并发度太低 | 提高该算子的并行度 |
| 某个算子处理慢（含复杂计算/IO等待） | 异步IO（AsyncDataStream）替代同步调用 |
| State 过大，RocksDB 读写慢 | 优化 State 结构，增加 RocksDB 内存 |
| 数据倾斜，部分 Task 过热 | 打散热点 key |

---

## 十、Flink SQL（面试加分项）

Flink 1.13+ Table API 和 SQL 已经非常成熟：

```sql
-- 注册 Kafka Source 表
CREATE TABLE user_actions (
  user_id BIGINT,
  action  STRING,
  ts      TIMESTAMP(3),
  WATERMARK FOR ts AS ts - INTERVAL '5' SECOND
) WITH (
  'connector' = 'kafka',
  'topic' = 'user_action',
  'properties.bootstrap.servers' = 'localhost:9092',
  'format' = 'json'
);

-- 统计每分钟每用户点赞数
SELECT
  user_id,
  TUMBLE_START(ts, INTERVAL '1' MINUTE) AS window_start,
  COUNT(*) AS like_count
FROM user_actions
WHERE action = 'like'
GROUP BY user_id, TUMBLE(ts, INTERVAL '1' MINUTE);
```

---

## 十一、常见面试题汇总

### Q1：Flink 怎么保证数据不丢失？

**答**：Checkpoint 机制。定期把所有算子 state 快照到 HDFS，任务崩溃后从最近 Checkpoint 恢复。结合 Kafka offset 由 Flink 托管（不自动提交），实现端到端 Exactly-Once。

### Q2：Watermark 是什么？为什么需要它？

**答**：Watermark 是一个时间戳，表示这个时间之前的数据已经到齐。因为实时流数据会乱序到达（网络抖动/重试），用 EventTime 窗口时需要 Watermark 告诉 Flink 什么时候可以关闭窗口触发计算。

### Q3：Flink 任务突然变慢怎么排查？

**答**：
1. 看 Web UI 背压面板，找哪个算子有 HIGH 背压
2. 有背压的算子往往是瓶颈：看 CPU 使用率、GC 情况（jstat）、是否有外部 IO 等待
3. 看 Checkpoint 是否超时：超时说明 state 过大或 IO 慢
4. 看数据倾斜：某个 Task 的 Records Processed 远超其他 Task

### Q4：Flink 和 Kafka 怎么配合实现 Exactly-Once？

**答**：
- Source 端：Flink Kafka Connector 把 offset 保存在 Checkpoint state 里，不用 Kafka 自动提交
- Sink 端：FlinkKafkaProducer 使用 Kafka 事务，两阶段提交（Checkpoint 时 pre-commit，Checkpoint 成功后 commit）
- 任何一步失败：从上一个 Checkpoint 重启，事务回滚，重新消费

### Q5：滚动窗口和滑动窗口区别？

**答**：
- 滚动窗口：固定大小，不重叠，每条数据只属于一个窗口。如"每1分钟统计一次"
- 滑动窗口：固定大小，可以重叠，每条数据可以属于多个窗口。如"最近5分钟每1分钟更新"，一条数据会被5个窗口统计到

### Q6：RocksDB State Backend 和 Memory State Backend 怎么选？

**答**：
- 开发测试、state 量小（几十MB内）：MemoryStateBackend，速度快
- 生产环境、state 量大（GB级，如长期统计每个用户行为）：**RocksDBStateBackend**，数据存磁盘，不受 JVM 堆大小限制，Checkpoint 增量保存

### Q7：Flink 数据倾斜怎么处理？

**答**：
1. 热点 key 打散：给 key 加随机前缀，并行处理后聚合
2. 提高并行度：增加该算子的并发 Task 数
3. 用 LocalKeyBy：先在本地预聚合再全局聚合，减少网络传输
4. 异步 IO：外部查询慢的场景改用 AsyncDataStream

---

## 十二、Flink vs Spark 选型总结（一张图）

```
需要实时处理（毫秒级）？
    ├── 是 → Flink
    └── 否 → 准实时可以接受秒级？
              ├── 是 → Spark Streaming（与Spark生态集成更好）
              └── 离线批处理 → Spark / Hive

有复杂状态计算（用户画像/实时风控）？→ Flink（状态管理更强）
已有大量 Spark 代码，改动成本高？→ 继续用 Spark
```

---

> 更新时间：2026-06-13
> 关联文档：Spark从入门到面试.md、java八股记录.md
