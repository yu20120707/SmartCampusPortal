# SmartCampusPortal

高校智慧校园统一门户系统，面向学生、教师、领导三类用户提供差异化门户首页、校园业务入口和聚合信息展示。

本项目当前交付目标是 **全真项目 6 高校智慧校园门户**。由于项目 6 原始说明较少，产品范围参考 **全真项目 5 智慧校园服务平台** 的能力池进行补全，但不把项目 5 的所有能力一次性塞进 MVP。当前版本优先交付 Web 端可演示、可运行、可验收的智慧校园门户，移动端、微信端、APP 和真实外部系统集成暂缓。

## 项目定位

SmartCampusPortal 不是普通校园官网，也不是单一学生管理系统，而是一个统一门户：

- 面向学生聚合课程、课表、成绩、考试、学工、一卡通、缴费、申请和资产借用。
- 面向教师聚合任课、课表、考试、成绩录入、申请、一卡通和资产借用。
- 面向领导聚合驾驶舱指标、审批待办、资产审批、学工概览和运行统计。
- 复用底层认证、角色、菜单、权限、字典、日志等通用能力。
- 新增校园业务统一放在 `com.ruoyi.campus.*` 和 `ruoyi-ui/src/views/campus/*` 下。

## 当前 MVP

### 统一门户

- 登录后根据账号角色进入校园门户首页。
- 门户首页展示角色标签、聚合指标卡、快捷入口。
- 首屏补齐公告、校园新闻、待办、最近消费、待缴费、近期课表和考试安排。

### 教务

- 学生：个人教务信息、已选课程、课程表、成绩、考试安排、选课/退课演示。
- 教师：教师档案、任课列表、本周课程、考试安排、课程学生成绩、成绩录入演示。

### OA 与审批

- 学生/教师可提交和查看个人申请。
- 领导可查看审批待办，并进行通过/驳回演示。

### 一卡通

- 查看一卡通余额。
- 查看交易流水。
- 支持演示充值，不对接真实设备或支付渠道。

### 缴费

- 查看待缴费项目。
- 查看缴费记录。
- 支持演示支付状态流转，不对接真实支付网关。

### 资产

- 查看可借资产。
- 学生/教师可提交资产借用申请。
- 领导可审批资产借用。

### 学工

- 学生查看学工档案和学工记录。
- 领导查看学工概览和统计。

### 领导驾驶舱

- 学生规模、课程类型、成绩趋势。
- 审批、缴费、一卡通、资产等运行指标。

## 演示账号

三类门户账号密码均为 `admin123`。

| 用户类型 | 用户名 | 登录后视图 |
| --- | --- | --- |
| 学生 | `student` | 学生门户、我的教务、我的申请、一卡通、缴费中心、资产申请、我的学工 |
| 教师 | `teacher` | 教师门户、任课视图、我的申请、一卡通、资产申请 |
| 领导 | `leader` | 领导门户、领导驾驶舱、审批待办、资产审批、学工概览 |

默认管理员账号可继续用于系统管理能力。

| 用户类型 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `admin123` |

## 技术栈

| 层级 | 技术 |
| --- | --- |
| 后端 | Java, Spring Boot, Spring Security, JWT, MyBatis |
| 前端 | Vue 2, Element UI, Vuex, Vue Router, Axios |
| 数据库 | MySQL |
| 缓存 | Redis |
| 构建 | Maven, npm |

## 仓库结构

```text
SmartCampusPortal
├── ruoyi-admin/          # Spring Boot 启动模块，校园业务后端位于 com.ruoyi.campus
├── ruoyi-common/         # 公共工具能力
├── ruoyi-framework/      # 安全、JWT、Redis、Web 配置等框架能力
├── ruoyi-generator/      # 代码生成器
├── ruoyi-quartz/         # 定时任务模块
├── ruoyi-system/         # 系统管理、用户、角色、菜单、通知公告
├── ruoyi-ui/             # Vue 2 + Element UI 前端
├── sql/                  # 基础库脚本和 campus_* 演示数据脚本
├── docs/ai/              # PRD、MVP 范围、扩展能力和协作文档
└── scripts/              # 本地检查和 smoke 脚本
```

## 校园业务模块

```text
com.ruoyi.campus
├── portal/      # 门户聚合接口
├── academic/    # 教务：课程、课表、成绩、考试、任课
├── office/      # OA 申请与审批
├── card/        # 一卡通账户与交易流水
├── payment/     # 缴费项目与缴费记录
├── asset/       # 资产借用与审批
├── student/     # 学工档案与学工记录
└── dashboard/   # 领导驾驶舱统计
```

## 本地运行

### 推荐演示端口

我们当前本地演示环境按下面端口运行：

| 服务 | 地址 | 说明 |
| --- | --- | --- |
| MySQL | `127.0.0.1:3307` | 本地演示库，数据库名 `ry-vue` |
| Redis | `127.0.0.1:6379` | 登录 token、验证码、缓存等 |
| 后端 | `http://127.0.0.1:8081` | Spring Boot 服务 |
| 前端 | `http://127.0.0.1:81` | Vue 开发服务，代理 `/dev-api` 到后端 |

如果本机已经有 MySQL 或后端服务占用默认端口，可以继续使用上面的演示端口，避免和系统自带服务冲突。

### 1. 准备数据库

创建数据库后，按顺序导入 SQL：

1. `sql/ry_20260417.sql`
2. `sql/quartz.sql`
3. `sql/campus_v1_init.sql`
4. `sql/campus_v1_menu.sql`
5. `sql/campus_v2_office.sql`
6. `sql/campus_v2_card.sql`
7. `sql/campus_v2_payment.sql`
8. `sql/campus_v2_asset.sql`
9. `sql/campus_v2_student.sql`

注意：当前 `campus_*` SQL 是演示 seed 脚本，包含 `drop table`，适合本地演示环境，不是生产迁移脚本。

如果使用本项目当前演示端口 `3307`，可以在仓库根目录执行下面命令重建本地演示库。

下面示例默认 `mysql` 命令已经加入 `PATH`。如果没有加入，请先将 MySQL `bin` 目录加入系统环境变量，或把 `mysql` 替换为本机实际的 MySQL 客户端可执行文件路径。

```powershell
@"
drop database if exists ``ry-vue``;
create database ``ry-vue`` default character set utf8mb4 collate utf8mb4_general_ci;
use ``ry-vue``;
source ./sql/ry_20260417.sql;
source ./sql/quartz.sql;
source ./sql/campus_v1_init.sql;
source ./sql/campus_v1_menu.sql;
source ./sql/campus_v2_office.sql;
source ./sql/campus_v2_card.sql;
source ./sql/campus_v2_payment.sql;
source ./sql/campus_v2_asset.sql;
source ./sql/campus_v2_student.sql;
update sys_config set config_value='false' where config_key='sys.account.captchaEnabled';
"@ | mysql --host=127.0.0.1 --port=3307 --user=root
```

### 2. 启动 Redis

使用本地 Redis 服务即可，默认配置连接 `127.0.0.1:6379`。

### 3. 启动后端

```powershell
mvn -pl ruoyi-admin spring-boot:run
```

如果使用当前演示库 `127.0.0.1:3307/ry-vue`，建议显式传入后端参数：

```powershell
mvn -pl ruoyi-admin spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081 --spring.datasource.druid.master.url=jdbc:mysql://127.0.0.1:3307/ry-vue --spring.datasource.druid.master.username=root --spring.datasource.druid.master.password= --spring.data.redis.host=127.0.0.1 --spring.data.redis.port=6379"
```

如需完整构建：

```powershell
mvn clean install -pl ruoyi-admin -am -DskipTests
```

### 4. 启动前端

```powershell
cd ruoyi-ui
npm install
$env:NODE_OPTIONS="--openssl-legacy-provider"
$env:port="81"
$env:VUE_APP_PROXY_TARGET="http://127.0.0.1:8081"
npm run dev
```

启动成功后访问：

```text
http://127.0.0.1:81/
```

如需构建生产包：

```powershell
cd ruoyi-ui
$env:NODE_OPTIONS="--openssl-legacy-provider"
npm run build:prod
```

### 5. 当前联调链路

开发环境前端使用 `ruoyi-ui/.env.development` 中的 `VUE_APP_BASE_API=/dev-api`。`vue.config.js` 会把 `/dev-api` 代理到 `VUE_APP_PROXY_TARGET`，因此本地联调推荐：

```text
浏览器 -> http://127.0.0.1:81
前端代理 -> /dev-api
后端服务 -> http://127.0.0.1:8081
数据库 -> 127.0.0.1:3307/ry-vue
Redis -> 127.0.0.1:6379
```

如果本机设置了 `http_proxy` / `https_proxy`，命令行验证本地接口时建议绕过代理：

```powershell
curl.exe --noproxy "*" http://127.0.0.1:8081/captchaImage
curl.exe --noproxy "*" http://127.0.0.1:81/
```

## 构建方式

### 后端构建

```powershell
mvn clean install -pl ruoyi-admin -am -DskipTests
```

说明：

- `-pl ruoyi-admin` 指定启动模块。
- `-am` 会同时构建 `ruoyi-admin` 依赖的模块。
- `-DskipTests` 适合本地快速打包；需要测试时可去掉。

### 前端构建

```powershell
cd ruoyi-ui
npm install
$env:NODE_OPTIONS="--openssl-legacy-provider"
npm run build:prod
```

构建产物输出到：

```text
ruoyi-ui/dist/
```

如果只需要测试环境包：

```powershell
cd ruoyi-ui
$env:NODE_OPTIONS="--openssl-legacy-provider"
npm run build:stage
```

## 关键页面

| 页面 | 路径 | 说明 |
| --- | --- | --- |
| 校园门户 | `/campus/portal` | 三类用户共用的角色化门户首屏 |
| 我的教务 | `/campus/academic/student` | 学生教务视图 |
| 任课视图 | `/campus/academic/teacher` | 教师任课与成绩录入视图 |
| 领导驾驶舱 | `/campus/dashboard` | 领导统计看板 |
| 我的申请 | `/campus/office/my` | 学生/教师申请提交与查询 |
| 审批待办 | `/campus/office/todo` | 领导 OA 审批 |
| 一卡通 | `/campus/card` | 余额、交易、演示充值 |
| 缴费中心 | `/campus/payment` | 待缴项目、缴费记录、演示支付 |
| 资产申请 | `/campus/asset/index` | 资产列表与借用申请 |
| 资产审批 | `/campus/asset/todo` | 领导资产审批 |
| 我的学工 | `/campus/student/my` | 学生学工档案 |
| 学工概览 | `/campus/student/overview` | 领导学工概览 |

## 验证

后端启动后可运行 smoke 脚本验证三类账号的主要 API 闭环：

```powershell
powershell -ExecutionPolicy Bypass -File scripts\campus_smoke.ps1 -BaseUrl http://127.0.0.1:8081 -Password admin123
```

常用检查：

```powershell
git diff --check
mvn clean install -pl ruoyi-admin -am -DskipTests
cd ruoyi-ui
$env:NODE_OPTIONS="--openssl-legacy-provider"
npm run build:prod
```

## MVP 边界

当前明确暂缓：

- 移动端、微信端、APP。
- 真实单点登录和外部系统适配器。
- Flowable 或复杂多级审批。
- 真实支付网关、回调、退款、对账、结算。
- 真实一卡通设备或网关接入。
- 完整补考重修、评教、毕业设计、等级考试报名。
- 完整奖助贷、心理咨询、宿舍、勤工俭学、辅导员工作台。
- 完整固定资产采购、维修、归还、盘点、折旧、报废。
- 招生、新生报到、科研、教师画像、教学质量等深度驾驶舱专题。

更详细的冻结范围见：

- `docs/ai/mvp-scope.md`
- `docs/ai/mvp-page-freeze.md`
- `docs/ai/extension-capabilities.md`
- `docs/ai/product-scope.md`

## 文档

- `docs/ai/README.md`：文档索引。
- `docs/ai/product-scope.md`：项目 5/6 关系与产品范围。
- `docs/ai/mvp-scope.md`：MVP 范围与验收边界。
- `docs/ai/mvp-page-freeze.md`：三类用户首屏和二级页面冻结。
- `docs/ai/extension-capabilities.md`：MVP 后扩展能力池。
- `docs/ai/reuse-matrix.md`：外部能力参考与复用策略。

## License

本项目用于高校智慧校园门户课程/实训场景演示。若用于真实生产环境，需要补齐生产迁移、外部系统集成、安全审计、测试覆盖和部署运维方案。
