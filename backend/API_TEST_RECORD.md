# 第一阶段后端接口测试记录

## 一、测试环境

- 测试时间：2026-05-16 21:16:11
- 测试地址：`http://localhost:8086`
- 测试工具：PowerShell `Invoke-WebRequest`（等价于 curl 接口访问）
- 后端项目：Spring Boot + MyBatis + MySQL
- 统一返回格式：`code`、`message`、`data`

## 二、当前第一阶段查询接口识别

根据 `controller` 目录读取结果，当前第一阶段查询接口如下：

| Controller | 接口 | 说明 |
| --- | --- | --- |
| `MajorController` | `GET /api/majors` | 查询专业列表 |
| `MajorController` | `GET /api/majors/{id}` | 根据专业 id 查询专业详情 |
| `MajorController` | `GET /api/majors/{id}/courses` | 查询某个专业相关课程 |
| `CourseController` | `GET /api/courses/{id}/knowledge-points` | 查询某门课程知识点，`id` 为课程 id |
| `CareerController` | `GET /api/careers` | 查询职业列表 |
| `CareerController` | `GET /api/careers/{id}` | 根据职业 id 查询职业详情 |
| `CareerController` | `GET /api/careers/{id}/courses` | 查询某个职业相关课程 |
| `CareerController` | `GET /api/careers/{id}/abilities` | 查询某个职业需要的职业能力 |
| `CareerController` | `GET /api/careers/{id}/knowledge-points` | 查询某个职业相关知识点 |

## 三、接口测试总览

| 序号 | 请求方式 | 接口地址 | HTTP 状态码 | 业务状态码 | 数据概况 | 是否通过 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | GET | `/api/majors` | 200 | 200 | 返回 3 条专业数据 | 通过 |
| 2 | GET | `/api/majors/software-engineering` | 200 | 200 | 返回 1 条专业详情 | 通过 |
| 3 | GET | `/api/majors/software-engineering/courses` | 200 | 200 | 返回 29 条课程数据 | 通过 |
| 4 | GET | `/api/courses/ds-16/knowledge-points` | 200 | 200 | 返回 3 条知识点数据 | 通过 |
| 5 | GET | `/api/careers` | 200 | 200 | 返回 17 条职业数据 | 通过 |
| 6 | GET | `/api/careers/java-backend-engineer` | 200 | 200 | 返回 1 条职业详情 | 通过 |
| 7 | GET | `/api/careers/java-backend-engineer/courses` | 200 | 200 | 返回 14 条职业课程数据 | 通过 |
| 8 | GET | `/api/careers/java-backend-engineer/abilities` | 200 | 200 | 返回 9 条职业能力数据 | 通过 |
| 9 | GET | `/api/careers/java-backend-engineer/knowledge-points` | 200 | 200 | 返回 6 条职业知识点数据 | 通过 |

说明：课程知识点接口需要课程 id。本次从 `GET /api/majors/software-engineering/courses` 返回结果中选择真实课程 id：`ds-16`。当前专业课程接口返回字段名为 `id`，该字段代表课程 id。

## 四、接口详细记录

### 1. 查询专业列表

- 请求方式：GET
- 接口地址：`http://localhost:8086/api/majors`
- 测试参数：无
- HTTP 状态码：200
- 返回结果概况：`code=200`，`message=success`，`data` 为数组，共 3 条
- 字段示例：`id`、`name`、`intro`、`createdAt`、`updatedAt`
- 是否通过：通过

### 2. 查询专业详情

- 请求方式：GET
- 接口地址：`http://localhost:8086/api/majors/software-engineering`
- 测试参数：`id=software-engineering`
- HTTP 状态码：200
- 返回结果概况：`code=200`，`message=success`，`data` 为对象
- 字段示例：`id`、`name`、`intro`、`createdAt`、`updatedAt`
- 是否通过：通过

### 3. 查询专业相关课程

- 请求方式：GET
- 接口地址：`http://localhost:8086/api/majors/software-engineering/courses`
- 测试参数：`id=software-engineering`
- HTTP 状态码：200
- 返回结果概况：`code=200`，`message=success`，`data` 为数组，共 29 条
- 字段示例：`id`、`name`、`description`、`type`、`credits`
- 是否通过：通过

### 4. 查询课程知识点

- 请求方式：GET
- 接口地址：`http://localhost:8086/api/courses/ds-16/knowledge-points`
- 测试参数：`courseId=ds-16`
- HTTP 状态码：200
- 返回结果概况：`code=200`，`message=success`，`data` 为数组，共 3 条
- 字段示例：`id`、`courseId`、`name`、`description`、`difficulty`、`ability`、`source`
- 是否通过：通过

### 5. 查询职业列表

- 请求方式：GET
- 接口地址：`http://localhost:8086/api/careers`
- 测试参数：无
- HTTP 状态码：200
- 返回结果概况：`code=200`，`message=success`，`data` 为数组，共 17 条
- 字段示例：`id`、`name`、`category`、`sortOrder`、`createdAt`、`updatedAt`
- 是否通过：通过

### 6. 查询职业详情

- 请求方式：GET
- 接口地址：`http://localhost:8086/api/careers/java-backend-engineer`
- 测试参数：`id=java-backend-engineer`
- HTTP 状态码：200
- 返回结果概况：`code=200`，`message=success`，`data` 为对象
- 字段示例：`id`、`name`、`category`、`sortOrder`、`createdAt`、`updatedAt`
- 是否通过：通过

### 7. 查询职业相关课程

- 请求方式：GET
- 接口地址：`http://localhost:8086/api/careers/java-backend-engineer/courses`
- 测试参数：`id=java-backend-engineer`
- HTTP 状态码：200
- 返回结果概况：`code=200`，`message=success`，`data` 为数组，共 14 条
- 字段示例：`courseId`、`courseName`、`courseDescription`、`courseRole`、`relatedAbility`、`relatedPoints`、`importance`、`isCore`、`learningStage`
- 是否通过：通过

### 8. 查询职业能力

- 请求方式：GET
- 接口地址：`http://localhost:8086/api/careers/java-backend-engineer/abilities`
- 测试参数：`id=java-backend-engineer`
- HTTP 状态码：200
- 返回结果概况：`code=200`，`message=success`，`data` 为数组，共 9 条
- 字段示例：`id`、`careerId`、`name`、`description`、`abilityType`、`importance`、`learningAdvice`
- 是否通过：通过

### 9. 查询职业相关知识点

- 请求方式：GET
- 接口地址：`http://localhost:8086/api/careers/java-backend-engineer/knowledge-points`
- 测试参数：`id=java-backend-engineer`
- HTTP 状态码：200
- 返回结果概况：`code=200`，`message=success`，`data` 为数组，共 6 条
- 字段示例：`id`、`careerId`、`courseId`、`courseName`、`knowledgePointId`、`knowledgePointName`、`knowledgePointDescription`、`useScene`、`importance`、`learningAdvice`、`source`
- 是否通过：通过

## 五、返回字段概况

### 统一返回字段

所有接口均使用统一返回格式：

```json
{
  "code": 200,
  "message": "success",
  "data": "..."
}
```

### 主要业务字段

| 模块 | 字段概况 |
| --- | --- |
| 专业 | `id`、`name`、`intro`、`createdAt`、`updatedAt` |
| 专业课程 | `id`、`name`、`description`、`type`、`credits` |
| 课程知识点 | `id`、`courseId`、`name`、`description`、`difficulty`、`ability`、`source` |
| 职业 | `id`、`name`、`category`、`sortOrder`、`createdAt`、`updatedAt` |
| 职业课程 | `courseId`、`courseName`、`courseDescription`、`courseRole`、`relatedAbility`、`relatedPoints`、`importance`、`isCore`、`learningStage` |
| 职业能力 | `id`、`careerId`、`name`、`description`、`abilityType`、`importance`、`learningAdvice` |
| 职业知识点 | `id`、`careerId`、`courseId`、`courseName`、`knowledgePointId`、`knowledgePointName`、`knowledgePointDescription`、`useScene`、`importance`、`learningAdvice`、`source` |

## 六、测试结论

本次测试的第一阶段查询接口均可正常访问，HTTP 状态码均为 200，业务状态码均为 200，返回格式统一为 `code`、`message`、`data`。

本次指定测试数据均返回了有效数据。后续如果某个职业暂时没有能力、知识点或课程关联数据，接口返回 `data: []` 属于正常情况，不应视为错误。

第一阶段后端查询接口测试通过，后续可以开始前端接口联调。
