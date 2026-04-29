# AGENTS.md

## 项目说明

这是一个前后端分离项目。

- `fronted`：已有前端项目，使用 Vue / Vite / JavaScript 实现。
- `backend`：准备开发的后端项目，计划使用 Java、Spring Boot、MyBatis、MySQL。
- 本项目后续重点是根据前端页面和新增需求，补齐后端接口、数据库表、业务逻辑和 MyBatis 映射。
- 前端项目既是后端接口设计的重要参考，也可能会随着新增需求进行调整。

## 工作规则

- 默认以后端开发为主，优先根据前端页面和业务需求设计后端接口、数据库表和业务逻辑。
- 如果前端新增需求导致页面、路由、组件、数据结构或接口调用方式需要调整，可以修改 `fronted` 目录下的代码。
- 修改代码前，必须先说明修改方案，等待用户确认后再执行。
- 修改前端代码前，必须先说明：
  - 需要修改哪些文件；
  - 为什么需要修改；
  - 修改后会影响哪些页面或功能；
  - 是否需要后端同步提供接口。
- 不要在未说明原因的情况下随意修改前端样式、页面结构或已有功能。
- 不要删除用户已有文件。
- 不要随意重命名目录或移动文件。
- 不要提交 Git、Push 到远程仓库，除非用户明确要求。
- 遇到不确定的业务逻辑时，先询问用户，不要自行假设。
- 如果用户要求“只读分析”，则只能读取项目结构和文件内容，不能修改任何文件。

## 忽略目录和文件

分析项目时请忽略以下目录或文件：

- `.idea/`
- `.git/`
- `node_modules/`
- `dist/`
- `target/`
- `*.log`
- `*.zip`
- `.env`
- `.env.local`
- `application-local.yml`
- `application-local.properties`

## 前端目录说明

`fronted` 是已有前端项目，主要用于分析页面功能、路由结构、数据字段和接口需求。

重点读取：

- `fronted/package.json`
- `fronted/src`
- `fronted/src/router`
- `fronted/src/views`
- `fronted/src/components`
- `fronted/src/data`
- `fronted/vite.config.js`
- `fronted/项目实现说明.md`

不要读取或分析：

- `fronted/node_modules`
- `fronted/dist`
- `fronted/.idea`

修改前端时要保持原有页面风格统一，不要破坏已有功能。

## 后端开发要求

后端计划采用：

- Java
- Spring Boot
- MyBatis
- MySQL
- Maven

建议后端结构：

- `controller`：接收前端请求
- `service`：处理业务逻辑
- `service.impl`：业务逻辑实现类
- `mapper`：操作数据库的接口
- `entity`：数据库实体类
- `dto`：接收前端请求数据
- `vo`：返回给前端的数据对象
- `config`：配置类
- `common`：通用返回结果、常量、工具类
- `resources/mapper`：MyBatis XML 映射文件

## 接口设计要求

- 后端接口要尽量根据前端页面实际需求设计，不要脱离前端随意增加复杂功能。
- Controller 返回结果尽量统一格式，例如：
  - `code`：状态码
  - `message`：提示信息
  - `data`：返回数据
- 接口命名要清晰，例如：
  - `/api/majors`
  - `/api/courses`
  - `/api/jobs`
  - `/api/resources`
- 如果前端已有静态数据文件，应优先分析这些数据字段，再设计数据库表和接口返回格式。
- 如果接口字段不确定，先给出建议方案，不要直接修改代码。

## 数据库设计要求

- 数据库使用 MySQL。
- 表名使用小写字母和下划线，例如 `major_info`、`course_info`。
- 字段名使用下划线风格，例如 `course_name`、`major_id`。
- 每张表建议包含主键 `id`。
- 如果适合，可以添加：
  - `create_time`
  - `update_time`
- 设计数据库表前，要先说明表的作用、主要字段和表之间的关系。

## 代码风格

- Java 类名使用大驼峰命名，例如 `UserController`、`CourseService`。
- 方法名、变量名使用小驼峰命名，例如 `getCourseList`、`majorId`。
- SQL 字段命名使用下划线风格，例如 `course_name`。
- 代码要适合初学者理解，必要时添加简短注释。
- 不要一次性生成过多复杂代码，优先保证结构清晰、能运行、方便学习。

## 和用户沟通方式

- 用户正在学习后端开发，解释时请尽量通俗。
- 不要一次性推进太多步骤，优先一步一步完成。
- 每次修改前说明：
  - 要改哪些文件；
  - 为什么要改；
  - 改完后有什么效果；
  - 用户需要如何运行或测试。
- 如果只是分析项目，请保持只读，不要修改任何文件。
- 如果发现项目结构、命名或配置存在问题，先说明问题和建议，不要直接改。