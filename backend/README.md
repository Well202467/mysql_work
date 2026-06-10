# smart_learning backend

这是 `smart_learning` 项目的 Spring Boot 后端基础项目。当前只完成成员 A 的任务：后端基础框架和专业课程模块。

## 1. 修改数据库连接信息

打开 `src/main/resources/application.yml`，修改下面两项：

```yaml
spring:
  datasource:
    username: your_username
    password: your_password
```

数据库名已经配置为 `smart_learning`：

```yaml
url: jdbc:mysql://localhost:3306/smart_learning?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
```

如果你的 MySQL 不在本机，或者端口不是 `3306`，也需要同步修改 `url`。

## 2. 启动项目

进入 `backend` 目录：

```bash
cd backend
```

启动项目：

```bash
mvn spring-boot:run
```

项目默认端口是：

```text
8080
```

## 3. 测试接口

可以使用浏览器、Postman、IDEA HTTP Client 或 curl 测试。

```text
GET http://localhost:8080/api/majors
GET http://localhost:8080/api/majors/{id}
GET http://localhost:8080/api/majors/{id}/courses
GET http://localhost:8080/api/courses/{id}/knowledge-points
```

示例：

```text
GET http://localhost:8080/api/majors
GET http://localhost:8080/api/majors/1
GET http://localhost:8080/api/majors/1/courses
GET http://localhost:8080/api/courses/1/knowledge-points
```

## 4. 当前已完成接口

| 方法 | 地址 | 说明 |
|---|---|---|
| GET | `/api/majors` | 查询专业列表 |
| GET | `/api/majors/{id}` | 根据 id 查询专业详情 |
| GET | `/api/majors/{id}/courses` | 查询某个专业的课程 |
| GET | `/api/courses/{id}/knowledge-points` | 查询某门课程的知识点 |

统一返回格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

列表查询不到数据时，`data` 返回空数组 `[]`，不会因为数据为空返回 500。
