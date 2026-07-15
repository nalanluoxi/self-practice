# screenshot 结果消息新增 s3FileKeys 字段执行计划

## 一、背景与目的

### 问题现状

下游消费截图结果消息（`ScreenshotResultMessage`）时，消息中只有 `s3DirKey`（S3 目录前缀，格式为 `screenshot/{yyyyMMdd}/{dirName}/`）。下游批量处理时必须通过 S3 `ListObjects` 接口扫描整个目录才能得知上传了哪些文件，频繁触发 S3 限流。

### 解决方案

在 `ScreenshotResultMessage` 中新增 `List<String> s3FileKeys` 字段，记录每次抓取成功上传的所有 S3 文件路径（包括 CSS/JS 等资源文件）。下游直接遍历该列表逐一调用 `GetObject`，完全消除 `ListObjects` 调用。

---

## 二、S3 文件结构说明

每次截图抓取的上传结构：

```
screenshot/{date}/{dirName}/
  ├── index.html                          ← 固定，Phase1 HTML 内容
  ├── resources.tsv                       ← 有资源时上传，记录资源URL→localName映射
  ├── assets/{localName}                  ← 动态，每个 JS/CSS/图片资源一个文件
  └── viewport_{idx}/                     ← 每个视口一个目录（idx 从 0 开始）
        ├── screenshot.png                ← 固定
        ├── annotated_screenshot.png      ← annotatedBytes 非空时上传
        └── clickable_elements.json       ← elementsJson 非空时上传
```

**各文件上传条件：**

| 文件 | 上传条件 |
|------|----------|
| `index.html` | `html != null` |
| `assets/{localName}` | `body != null && body.length > 0` |
| `resources.tsv` | `StringUtils.isNotBlank(resourcesTsv)` |
| `viewport_{idx}/screenshot.png` | screenshotBytes 非空（正常截图均会上传） |
| `viewport_{idx}/annotated_screenshot.png` | `annotatedBytes != null` |
| `viewport_{idx}/clickable_elements.json` | `StringUtils.isNotBlank(elementsJson)` |

---

## 三、消息体大小评估

| 项目 | 估算 |
|------|------|
| 单条路径长度 | 约 80 字符 |
| 典型资源总数 | 1 + 1 + 50~100 + 3×N视口 ≈ 100 条 |
| 预估增量大小 | 100 × 80 = 8KB |
| Mafka 消息限制 | 1MB |
| 结论 | 安全，远低于限制 |

---

## 四、涉及文件与改动详情

### 4.1 ScreenshotCrawlResult.java

**路径：** `llm-crawler-server/src/main/java/com/sankuai/llm/spider/crawler/screenshot/model/ScreenshotCrawlResult.java`

**改动：** 新增 `s3FileKeys` 字段，需加 `@Builder.Default` 否则 builder 模式下字段为 null，后续 `.add()` 会 NPE。

```java
// 新增字段
/**
 * 本次抓取成功上传的所有 S3 文件路径列表，按上传顺序追加。
 */
@Builder.Default
private List<String> s3FileKeys = new ArrayList<>();
```

同时顶部补充 `import java.util.ArrayList;`（`List` 已有 import）。

---

### 4.2 ScreenshotResultMessage.java

**路径：** `llm-crawler-server/src/main/java/com/sankuai/llm/spider/crawler/screenshot/model/ScreenshotResultMessage.java`

**改动：** 新增 `s3FileKeys` 字段，加在 `startTime` 字段之后。

```java
// 顶部补充
import java.util.List;

// 新增字段
/**
 * 本次抓取上传的所有 S3 文件路径列表，下游直接 GetObject 访问，无需 ListObjects 扫描目录。
 * 抓取完全失败时为 null 或空列表，下游消费需判空。
 */
private List<String> s3FileKeys;
```

**注意：** `s3DirKey` 字段保留不删除，维持向后兼容。

---

### 4.3 S3Service.java

**路径：** `llm-crawler-server/src/main/java/com/sankuai/llm/spider/crawler/service/S3Service.java`

**改动：** `uploadScreenshotBytes` 返回值从 `void` 改为 `boolean`，成功返回 `true`，data 为空或上传异常返回 `false`。

**改前：**
```java
public void uploadScreenshotBytes(String s3Key, byte[] data, String contentType, String domain) {
    if (data == null || data.length == 0) {
        return;
    }
    ...
    try {
        // 上传逻辑
        tx.setSuccessStatus();
    } catch (Exception e) {
        log.error("screenshot s3 upload error, key={}", s3Key, e);
        Cat.logEvent("screenshot.s3.upload.error", domain);
        tx.setStatus(e);
    } finally {
        tx.complete();
    }
}
```

**改后：**
```java
public boolean uploadScreenshotBytes(String s3Key, byte[] data, String contentType, String domain) {
    if (data == null || data.length == 0) {
        return false;
    }
    ...
    try {
        // 上传逻辑（不变）
        tx.setSuccessStatus();
        return true;      // ← 新增
    } catch (Exception e) {
        log.error("screenshot s3 upload error, key={}", s3Key, e);
        Cat.logEvent("screenshot.s3.upload.error", domain);
        tx.setStatus(e);
        return false;     // ← 新增
    } finally {
        tx.complete();
    }
}
```

---

### 4.4 ScreenshotPlaywrightService.java

**路径：** `llm-crawler-server/src/main/java/com/sankuai/llm/spider/crawler/service/ScreenshotPlaywrightService.java`

**改动概述：**
1. `crawlScreenshot` 方法开头新增 `List<String> s3FileKeys = new ArrayList<>();`
2. 6 处上传点：接收 `boolean` 返回值，成功时 `s3FileKeys.add(key)`
3. 方法末尾 builder 补充 `.s3FileKeys(s3FileKeys)`

#### 步骤 1：方法开头新增局部变量

在局部变量声明区新增：
```java
List<String> s3FileKeys = new ArrayList<>();
```

#### 步骤 2-A：上传 index.html

**改前：**
```java
if (html != null) {
    s3Service.uploadScreenshotBytes(s3DirKey + "index.html",
            html.getBytes(java.nio.charset.StandardCharsets.UTF_8), "text/html", domain);
}
```

**改后：**
```java
if (html != null) {
    String indexHtmlKey = s3DirKey + "index.html";
    if (s3Service.uploadScreenshotBytes(indexHtmlKey,
            html.getBytes(java.nio.charset.StandardCharsets.UTF_8), "text/html", domain)) {
        s3FileKeys.add(indexHtmlKey);
    }
}
```

#### 步骤 2-B：上传 assets 资源

**改前：**
```java
s3Service.uploadScreenshotBytes(s3DirKey + "assets/" + localName,
        body, "application/octet-stream", domain);
tsvBuilder.append(...);
```

**改后：**
```java
String assetKey = s3DirKey + "assets/" + localName;
if (s3Service.uploadScreenshotBytes(assetKey, body, "application/octet-stream", domain)) {
    s3FileKeys.add(assetKey);
}
tsvBuilder.append(...);  // tsvBuilder 追加与上传结果无关，不受影响
```

#### 步骤 2-C：上传 resources.tsv

**改前：**
```java
if (StringUtils.isNotBlank(resourcesTsv)) {
    s3Service.uploadScreenshotBytes(s3DirKey + "resources.tsv",
            resourcesTsv.getBytes(java.nio.charset.StandardCharsets.UTF_8), "text/plain", domain);
    resourcesTsv = null;
}
```

**改后：**
```java
if (StringUtils.isNotBlank(resourcesTsv)) {
    String tsvKey = s3DirKey + "resources.tsv";
    if (s3Service.uploadScreenshotBytes(tsvKey,
            resourcesTsv.getBytes(java.nio.charset.StandardCharsets.UTF_8), "text/plain", domain)) {
        s3FileKeys.add(tsvKey);
    }
    resourcesTsv = null;
}
```

#### 步骤 2-D：上传 screenshot.png

**改前：**
```java
s3Service.uploadScreenshotBytes(vpPrefix + "screenshot.png", screenshotBytes, "image/png", domain);
screenshotBytes = null;
```

**改后：**
```java
if (s3Service.uploadScreenshotBytes(vpPrefix + "screenshot.png", screenshotBytes, "image/png", domain)) {
    s3FileKeys.add(vpPrefix + "screenshot.png");
}
screenshotBytes = null;
```

#### 步骤 2-E：上传 annotated_screenshot.png

**改前：**
```java
if (annotatedBytes != null) {
    s3Service.uploadScreenshotBytes(vpPrefix + "annotated_screenshot.png", annotatedBytes, "image/png", domain);
    annotatedBytes = null;
}
```

**改后：**
```java
if (annotatedBytes != null) {
    if (s3Service.uploadScreenshotBytes(vpPrefix + "annotated_screenshot.png", annotatedBytes, "image/png", domain)) {
        s3FileKeys.add(vpPrefix + "annotated_screenshot.png");
    }
    annotatedBytes = null;
}
```

#### 步骤 2-F：上传 clickable_elements.json

**改前：**
```java
if (StringUtils.isNotBlank(elementsJson)) {
    s3Service.uploadScreenshotBytes(vpPrefix + "clickable_elements.json",
            elementsJson.getBytes(java.nio.charset.StandardCharsets.UTF_8), "application/json", domain);
}
```

**改后：**
```java
if (StringUtils.isNotBlank(elementsJson)) {
    String elementsKey = vpPrefix + "clickable_elements.json";
    if (s3Service.uploadScreenshotBytes(elementsKey,
            elementsJson.getBytes(java.nio.charset.StandardCharsets.UTF_8), "application/json", domain)) {
        s3FileKeys.add(elementsKey);
    }
}
```

#### 步骤 3：builder 补充 s3FileKeys

```java
return ScreenshotCrawlResult.builder()
        ...
        .startTime(startTime)
        .s3FileKeys(s3FileKeys)  // ← 新增
        .build();
```

---

### 4.5 ScreenshotService.java

**路径：** `llm-crawler-server/src/main/java/com/sankuai/llm/spider/crawler/screenshot/ScreenshotService.java`

**改动：** `buildResultMessage` 末尾透传 `s3FileKeys`。

**改前：**
```java
message.setStartTime(crawlResult.getStartTime());
return message;
```

**改后：**
```java
message.setStartTime(crawlResult.getStartTime());
message.setS3FileKeys(crawlResult.getS3FileKeys());
return message;
```

---

## 五、改动总览

| 文件 | 改动类型 | 核心内容 |
|------|----------|----------|
| `ScreenshotCrawlResult.java` | 新增字段 | `@Builder.Default private List<String> s3FileKeys = new ArrayList<>()` |
| `ScreenshotResultMessage.java` | 新增字段 | `private List<String> s3FileKeys` |
| `S3Service.java` | 修改签名 | `void` → `boolean`，成功 true，失败 false |
| `ScreenshotPlaywrightService.java` | 修改逻辑 | 6 处上传点收集路径，builder 补充字段 |
| `ScreenshotService.java` | 修改逻辑 | `buildResultMessage` 末尾透传字段 |

---

## 六、注意事项

1. **`@Builder.Default`**：`ScreenshotCrawlResult` 使用了 `@Builder`，字段初始化器在 builder 模式下不生效，必须加 `@Builder.Default`，否则 NPE。

2. **向后兼容**：`s3DirKey` 保留不变。新增字段对旧版下游无感知（Jackson 反序列化时忽略未知字段）。

3. **失败场景**：两阶段均失败时走 catch 分支，`@Builder.Default` 保证 `s3FileKeys` 为空列表而非 null，下游消费仍需判断列表是否为空（兼容历史消息中该字段为 null 的情况）。

4. **线程安全**：`s3FileKeys` 为 `crawlScreenshot` 方法的局部变量，单线程操作，无并发问题。

5. **其他调用方**：`uploadScreenshotBytes` 签名改变后，忽略返回值的调用方编译不报错、行为不变，无需修改。改动前需全局搜索确认。

---

## 七、实施顺序

1. 改 `S3Service.java`（uploadScreenshotBytes 返回 boolean）
2. 改 `ScreenshotCrawlResult.java`（新增字段 + @Builder.Default）
3. 改 `ScreenshotResultMessage.java`（新增字段）
4. 改 `ScreenshotPlaywrightService.java`（6 处收集 + builder 传递）
5. 改 `ScreenshotService.java`（buildResultMessage 透传）
6. 编译验证，检查 `uploadScreenshotBytes` 所有调用方无编译错误