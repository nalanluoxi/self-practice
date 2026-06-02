# StatMonitorController 接口测试文档

> Base URL：`http://localhost:8080`
> 所有接口均为 GET 请求，响应格式统一为 `ResponseBean<T>`。

---

## 一、枚举索引类接口

### 1. 查询所有 monitorKey

**接口：** `GET /stat-monitor/monitor-keys`

**参数：** 无

**示例：**
```
GET /stat-monitor/monitor-keys
```

**预期响应：**
```json
{
  "code": 0,
  "data": ["screenshot", "sitemap"]
}
```

---

### 2. 查询某 monitorKey 下所有 module

**接口：** `GET /stat-monitor/modules`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| monitorKey | String | 是 | 业务标识，如 `screenshot` |

**正常示例：**
```
GET /stat-monitor/modules?monitorKey=screenshot
```
**预期响应：**
```json
{
  "code": 0,
  "data": ["screenshot"]
}
```

**异常示例（monitorKey 为空）：**
```
GET /stat-monitor/modules?monitorKey=
```
**预期响应：**
```json
{
  "code": -1,
  "msg": "monitorKey 不能为空"
}
```

---

### 3. 查询某 monitorKey+module 下所有 type

**接口：** `GET /stat-monitor/types`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| monitorKey | String | 是 | 业务标识 |
| module | String | 是 | 模块名 |

**正常示例：**
```
GET /stat-monitor/types?monitorKey=screenshot&module=screenshot
```
**预期响应：**
```json
{
  "code": 0,
  "data": ["download"]
}
```

**异常示例（缺少 module）：**
```
GET /stat-monitor/types?monitorKey=screenshot
```
**预期响应：**
```json
{
  "code": -1,
  "msg": "monitorKey 和 module 不能为空"
}
```

---

### 4. 查询 name 列表

**接口：** `GET /stat-monitor/names`

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| monitorKey | String | 是 | - | 业务标识 |
| module | String | 是 | - | 模块名 |
| type | String | 是 | - | 分类 |
| parsed | boolean | 否 | false | false：返回原始 `name#tag`；true：返回解析后 `[{name, tag}]` |

**示例1 — 原始格式：**
```
GET /stat-monitor/names?monitorKey=screenshot&module=screenshot&type=download
```
**预期响应：**
```json
{
  "code": 0,
  "data": ["baidu.com#succ", "baidu.com#fail_proxy_is_null", "google.com#succ"]
}
```

**示例2 — 解析格式：**
```
GET /stat-monitor/names?monitorKey=screenshot&module=screenshot&type=download&parsed=true
```
**预期响应：**
```json
{
  "code": 0,
  "data": [
    {"name": "baidu.com", "tag": "succ"},
    {"name": "baidu.com", "tag": "fail_proxy_is_null"},
    {"name": "google.com", "tag": "succ"}
  ]
}
```

---

## 二、时间维度查询接口

### 5. 按天查询（all 汇总 + 24 小时明细）

**接口：** `GET /stat-monitor/query/day`

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| monitorKey | String | 是 | - | 业务标识 |
| module | String | 是 | - | 模块名 |
| type | String | 是 | - | 分类 |
| day | String | 否 | 今天 | 格式 `yyyyMMdd`，如 `20260518` |

**示例1 — 缺省今天：**
```
GET /stat-monitor/query/day?monitorKey=screenshot&module=screenshot&type=download
```

**示例2 — 指定日期：**
```
GET /stat-monitor/query/day?monitorKey=screenshot&module=screenshot&type=download&day=20260518
```

**预期响应：**
```json
{
  "code": 0,
  "data": {
    "all": {
      "baidu.com#succ": 120,
      "baidu.com#fail_proxy_is_null": 5,
      "google.com#succ": 80
    },
    "2026051800": {"baidu.com#succ": 10},
    "2026051801": {"baidu.com#succ": 15, "google.com#succ": 8},
    "...": {}
  }
}
```

---

### 6. 按天查询（按 name 聚合，忽略 tag）

**接口：** `GET /stat-monitor/query/day/by-name`

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| monitorKey | String | 是 | - | 业务标识 |
| module | String | 是 | - | 模块名 |
| type | String | 是 | - | 分类 |
| day | String | 否 | 今天 | 格式 `yyyyMMdd` |

**示例：**
```
GET /stat-monitor/query/day/by-name?monitorKey=screenshot&module=screenshot&type=download&day=20260518
```

**预期响应：**（succ 和 fail 都合并到 domain 维度）
```json
{
  "code": 0,
  "data": {
    "baidu.com": 125,
    "google.com": 80
  }
}
```

---

### 7. 单小时查询

**接口：** `GET /stat-monitor/query/hour`

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| monitorKey | String | 是 | - | 业务标识 |
| module | String | 是 | - | 模块名 |
| type | String | 是 | - | 分类 |
| hour | String | 否 | 当前小时 | 格式 `yyyyMMddHH`，如 `2026051814` |

**示例1 — 缺省当前小时：**
```
GET /stat-monitor/query/hour?monitorKey=screenshot&module=screenshot&type=download
```

**示例2 — 指定小时：**
```
GET /stat-monitor/query/hour?monitorKey=screenshot&module=screenshot&type=download&hour=2026051814
```

**预期响应：**
```json
{
  "code": 0,
  "data": {
    "baidu.com#succ": 15,
    "baidu.com#fail_proxy_is_null": 2,
    "google.com#succ": 8
  }
}
```

---

### 8. 多天趋势查询

**接口：** `GET /stat-monitor/query/trend`

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| monitorKey | String | 是 | - | 业务标识 |
| module | String | 是 | - | 模块名 |
| type | String | 是 | - | 分类 |
| days | String | 否 | 最近 7 天 | 逗号分隔，如 `20260512,20260513,20260514` |

**示例1 — 缺省最近 7 天：**
```
GET /stat-monitor/query/trend?monitorKey=screenshot&module=screenshot&type=download
```

**示例2 — 指定日期范围：**
```
GET /stat-monitor/query/trend?monitorKey=screenshot&module=screenshot&type=download&days=20260512,20260513,20260514
```

**预期响应：**
```json
{
  "code": 0,
  "data": {
    "20260512": 1520,
    "20260513": 1803,
    "20260514": 1650
  }
}
```

---

## 三、截图监控专项测试用例

针对 `ScreenshotService.download()` 打点，`monitorKey=screenshot`、`module=screenshot`、`type=download`。

| 场景 | 预期 name | 预期 tag |
|------|-----------|----------|
| 两阶段都成功 | domain | `succ` |
| phase1 失败，phase2 成功 | domain | `fail_<phase1ErrorReason>` |
| phase1 成功，phase2 失败 | domain | `fail_<phase2ErrorReason>` |
| 两阶段都失败 | domain | `fail_<reason1>\|<reason2>` |
| result 为 null（极端异常） | domain | `fail_null_result` |
| 代理为 null 提前返回 | domain | `fail_proxy_is_null\|proxy_is_null` |

**验证用接口：**
```
# 查看今天某 domain 的所有打点
GET /stat-monitor/query/day?monitorKey=screenshot&module=screenshot&type=download

# 查看当前小时实时数据
GET /stat-monitor/query/hour?monitorKey=screenshot&module=screenshot&type=download

# 查看所有 name#tag 组合（用于确认打点 key 是否正确）
GET /stat-monitor/names?monitorKey=screenshot&module=screenshot&type=download&parsed=true
```

---

## 四、异常参数边界测试

| 场景 | 接口 | 参数 | 预期 |
|------|------|------|------|
| monitorKey 为空 | `/modules` | `monitorKey=` | `code=-1`，提示不能为空 |
| module 为空 | `/types` | `monitorKey=screenshot&module=` | `code=-1`，提示不能为空 |
| type 为空 | `/names` | `monitorKey=screenshot&module=screenshot&type=` | `code=-1`，提示不能为空 |
| day 格式错误 | `/query/day` | `day=2026-05-18` | 查不到数据，`data` 为空 Map（底层 key 不匹配） |
| hour 格式错误 | `/query/hour` | `hour=20260518` | 查不到数据，`data` 为空 Map |
| days 含空项 | `/query/trend` | `days=20260518,` | 空项被传入，对应天返回 0 |