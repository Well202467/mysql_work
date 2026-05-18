# smart_learning 项目交付说明

## 1. 项目概述

`smart_learning` 是一个面向软件学院学生的职业导航与学习推荐网站。项目采用前后端分离结构，前端负责专业、课程、职业方向和后台管理页面展示，后端负责通过 Spring Boot + MyBatis 从 MySQL 数据库读取和维护业务数据。

项目当前已完成最终验收，前端构建和后端测试均已通过。交付版本重点围绕“专业 -> 课程 -> 知识点 -> 职业方向”的学习路径展开，同时提供后台管理页面用于维护基础数据和关联数据。

## 2. 项目结构

| 路径 | 说明 |
| --- | --- |
| `fronted/` | 前端项目，使用 Vue 3、Vue Router、Vite、Axios 实现 |
| `fronted/src/views/` | 前台页面和后台管理页面 |
| `fronted/src/api/` | 前端接口请求封装 |
| `fronted/src/router/index.js` | 前端路由配置，包含展示页和后台页 |
| `backend/` | Spring Boot 后端项目 |
| `backend/src/main/java/com/example/smartlearning/` | 后端 Java 源码 |
| `backend/src/main/resources/mapper/` | MyBatis XML 映射文件 |
| `backend/src/main/resources/application.yml` | 后端端口、数据库和 MyBatis 配置 |
| `docs/` | 本次生成的项目交付文档 |

## 3. 技术栈

| 模块 | 技术 |
| --- | --- |
| 前端框架 | Vue 3 |
| 前端构建 | Vite 5 |
| 前端路由 | Vue Router 4 |
| HTTP 请求 | Axios |
| 后端框架 | Spring Boot 2.7.18 |
| 后端语言 | Java 21 |
| 持久层 | MyBatis |
| 数据库 | MySQL |
| 构建工具 | Maven、npm |
| 接口文档 | springdoc-openapi |

## 4. 已完成功能

### 4.1 前台展示

| 功能 | 对应页面 | 数据来源 |
| --- | --- | --- |
| 首页展示 | `/` | 前端页面内静态展示内容 |
| 专业列表 | `/majors` | `GET /api/majors` |
| 专业详情 | `/major/:id` | `GET /api/majors/{id}` |
| 专业课程列表 | `/major/:id` | `GET /api/majors/{id}/courses` |
| 课程知识点 | 课程弹窗、专业详情 | `GET /api/courses/{id}/knowledge-points` |
| 课程关联职业 | 课程弹窗、专业详情 | `GET /api/courses/{courseId}/careers` |
| 职业列表 | `/careers` | `GET /api/careers` |
| 职业详情 | `/careers/:id` | `GET /api/careers/{id}` |
| 职业相关课程 | `/careers/:id` | `GET /api/careers/{id}/courses` |
| 职业能力 | `/careers/:id` | `GET /api/careers/{id}/abilities` |
| 职业知识点 | `/careers/:id` | `GET /api/careers/{id}/knowledge-points` |

### 4.2 后台管理

后台入口为 `/admin/login`，登录后进入 `/admin/dashboard`。后台已完成 9 类数据管理：

| 管理模块 | 路由 | 后端接口前缀 |
| --- | --- | --- |
| 专业管理 | `/admin/majors` | `/api/admin/majors` |
| 课程管理 | `/admin/courses` | `/api/admin/courses` |
| 专业课程关系管理 | `/admin/major-courses` | `/api/admin/major-courses` |
| 知识点管理 | `/admin/knowledge-points` | `/api/admin/knowledge-points` |
| 职业管理 | `/admin/careers` | `/api/admin/careers` |
| 职业画像管理 | `/admin/career-profiles` | `/api/admin/career-profiles` |
| 职业能力管理 | `/admin/career-abilities` | `/api/admin/career-abilities` |
| 职业课程关系 | `/admin/career-courses` | `/api/admin/career-courses` |
| 职业知识点关系 | `/admin/career-knowledge-points` | `/api/admin/career-knowledge-points` |

## 5. 后端分层结构

| 包名 | 作用 |
| --- | --- |
| `controller` | 接收 HTTP 请求并返回统一响应 |
| `service` | 定义业务接口 |
| `service.impl` | 实现业务逻辑和校验逻辑 |
| `mapper` | MyBatis Mapper 接口 |
| `entity` | 数据库实体对象 |
| `dto` | 接收前端提交的数据 |
| `vo` | 返回给前端的视图对象 |
| `config` | 跨域、拦截器、OpenAPI 配置 |
| `common` | 通用响应结果类 |

## 6. 交付说明

本交付文档根据当前源码中的路由、接口、实体类和 Mapper SQL 整理，不包含数据库密码、管理员口令或个人隐私路径。部分历史说明文件保留了早期开发阶段内容，项目交付时请以 `docs/` 下文档和当前源码为准。
