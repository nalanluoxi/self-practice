# 浩浩的持久化记忆

## 当前主要任务
- 用户（老大）正在进行 Java 后端面试备考模拟练习
- 面试记录保存在：`/Users/nalan/IdeaProjects/self-practice/LanQiao/docs/复习记录/`
- 提问提示词（含所有规则）：`/Users/nalan/IdeaProjects/self-practice/LanQiao/docs/复习记录/面试官提问提示词.md`
- 八股参考资料：`/Users/nalan/IdeaProjects/self-practice/LanQiao/docs/A学习笔记/java八股记录.md`

## 面试模拟规则（每次启动必须遵守）
1. 每次只问一个问题，等回答后再出下一题
2. 每次回答后给出「更完善的参考回答」
3. 每次回答后同步追加到当天记录文档（YYYY-MM-DD-模拟面试记录.md）
4. 禁止纯背诵八股提问，必须结合业务场景
5. 提问维度：功能描述、技术选型、底层原理、大流量思考、场景题
6. 【项目深挖专项规则】优先出项目深挖题（Q96风格），回答不上来时必须给出包含以下内容的完整参考答案：
   - 当时设计的原始方案及选型理由
   - 其他可行的替代方案（至少2种）及各自优缺点
   - 当时可能是为了快速上线的妥协方案，现在有哪些更好的替代手段
   - 每种方案详细介绍，不能只列名字
7. 【STAR法则专项训练规则（2026-06-17起）】候选人介绍项目/工程亮点时必须评估STAR四要素：
   - S（Situation背景）✅/❌ | T（Task任务职责）✅/❌ | A（Action行动+选型理由）✅/❌ | R（Result量化结果+业务收益）✅/❌
   - T或R缺失时必须追问，参考回答必须给出完整STAR话术模板
   - A部分重点：必须包含"为什么这样做而不是那样做"的选型理由

## 候选人简历关键信息
- 姓名：冀子彦，太原理工大学软件工程（211），2023-2027
- 实习：美团 Friday数据组，Sitemap 新链发现环路后端
- 自研：智析RAG（Spring AI + RAG 对话系统）
- 项目路径：`/Users/nalan/IdeaProjects/`

## Playwright 实战项目（llm-spider-crawler）
- 路径：`/Users/nalan/IdeaProjects/llm-spider-crawler/`
- `PlaywrightDownLoader.java`：Playwright/Browser 实例封装，支持 Chrome（含反检测参数）/ Firefox，UA 绑定
- `ScreenshotPlaywrightService.java`：截图服务，实例池 + 下载线程池 + 5分钟 TTL 清理 + JS DOM 提取 + S3 存储 + 多代理线路
- 面试考点：Playwright vs Selenium、BrowserContext 隔离、反检测原理、实例池 OOM 防护、JS 注入时机、UA/时区指纹绑定

## 已考察的知识点（截至 2026-06-08）
详见提示词文档的"已考察内容"部分

## 核心薄弱点（每次启动重点追问，发现新薄弱点立即追加）
1. JDK 动态代理=接口，CGLIB=继承子类【二次答错，重点强化】
2. Spring Bean 生命周期8步：实例化→属性填充→Aware→前置→初始化→后置(AOP)→使用→销毁
3. Spring 三级缓存：一级=完整Bean，二级=早期引用，三级=ObjectFactory
4. 301=永久重定向，304=缓存未变更（混淆过）
5. GZIPInputStream.read() 返回 int，-1 表示结束（说成null）
6. Pattern 未重写 equals/hashCode，HashSet 无法去重
7. Kafka ack：1=leader收到即响应，-1=等ISR全部同步（已补考通过）✅
8. MVCC 可见性四步规则（已补考通过）✅
9. CPU 飙高排查三板斧：top -H → jstack → jstat
10. 并发丢失更新：MySQL 原子更新 `count=count+1` 是最简单解法
11. Canal 监听 MySQL binlog，不是 Redis（Redis 没有 binlog）
12. TIME_WAIT 在主动关闭方（客户端），不在服务端
13. JMM 指令重排序：volatile 禁止重排，happens-before 原则
14. 缺页中断：JVM 堆首次写入才触发，-XX:+AlwaysPreTouch 预热
15. synchronized 线程通信用 Object.wait()/notify()，不是 await/join
16. 网络带宽是IO并发的共享瓶颈：多线程爬虫并发下载共享同一网卡，带宽满后加线程不再线性提速【2026-06-22新增】
17. Semaphore vs synchronized 限并发：Semaphore.acquire() 是阻塞等待无延迟，while+sleep 是忙等有延迟；Semaphore 需 try-finally 保证 release【2026-06-22新增】
18. IO多路复用 ET/LT边缘触发 vs 水平触发 完全不清楚，EAGAIN编程模式不掌握【2026-06-30新增】

## 薄弱点更新规则
每次发现新薄弱点立即追加到提示词文档「候选人历史薄弱点」列表和本记忆文件，确保下次会话自动强化复考。

## 目标 JD 列表（2026-06-08 更新）

### JD1：京东大数据平台 + AI 融合岗
额外考察：Spark/Hive/HDFS/Flink/YARN（见提示词文档）

### JD2：字节跳动飞书 ByteIntern（2027届，Golang 后端）
- 岗位：飞书服务端研发，Go语言为主
- 重点：Web后端（协议/架构/存储/缓存/安全）、服务稳定性、开发效率工具
- **字节偏好基础知识深度**：TCP/HTTP/HTTPS、OS（进程/线程/内存管理）、JVM GC、并发（AQS/线程池）
- 字节高频考点：TCP粘包/TIME_WAIT、JMM内存模型、G1调优、线程池拒绝策略
- 提问风格：场景题切入基础知识，不问纯背诵

### JD3：Web3 钱包后端（Go语言，区块链方向）
- 岗位：Web3钱包核心模块（密钥生成/管理/签名/恢复）、账户抽象(AA)、多链集成
- 要求：3年以上Go经验、MongoDB/Redis/MySQL、Ethereum/Bitcoin/Solana了解
- 提问策略：
  - 候选人无区块链经验，此岗位匹配度低，**不建议重点备考**
  - 如需了解：非对称加密（公私钥/签名）、哈希函数是基础
  - 可从候选人已有知识迁移：Redis分布式锁 → 钱包防重放攻击原理类比

## 用户偏好
- 语言：中文
- 每道题回答后必须同步到当天记录文档
- 面试记录按天存档，文件名格式：YYYY-MM-DD-模拟面试记录.md
- 文档统一保存在 docs/ 下，不提交 git
