# 构建指南

## 目的

描述本项目的实际构建方式。

## 后端构建（Maven）

RuoYi-Vue 是一个多模块 Maven 项目，根 `pom.xml` 管理所有子模块。

```powershell
# 完整编译（跳过测试）
mvn clean install -DskipTests

# 编译并运行测试
mvn clean install

# 仅编译指定模块及其依赖
mvn clean install -pl ruoyi-admin -am -DskipTests

# 打包可执行 Jar
mvn clean package -DskipTests
# Jar 生成路径：ruoyi-admin/target/ruoyi-admin.jar
```

## 前端构建（npm）

```powershell
cd ruoyi-ui
npm install
npm run dev      # 开发模式（热重载）
npm run build:prod    # 生产构建
npm run build:stage   # 测试/预发构建
npm run preview  # 预览生产构建
```

## 启动顺序

1. 启动 MySQL 和 Redis
2. 初始化数据库脚本（`sql/`）
3. `mvn spring-boot:run -pl ruoyi-admin` 启动后端
4. `cd ruoyi-ui && npm run dev` 启动前端

## MVP API Smoke

后端启动后，可以用脚本验证当前 MVP 的学生、教师、领导三类演示账号 API 闭环：

```powershell
scripts\campus_smoke.ps1

# 指定后端地址
scripts\campus_smoke.ps1 -BaseUrl http://127.0.0.1:8081

# bat 入口
scripts\campus_smoke.bat
```

前置条件：

- MySQL 和 Redis 已启动。
- `ry_20260417.sql`、`quartz.sql`、`campus_v1_init.sql`、`campus_v1_menu.sql` 以及相关 `campus_v2_*.sql` 已导入。
- 后端已启动。
- 自动化 smoke 环境下需要关闭验证码，或保证 `/login` 不要求验证码。

## 常见构建问题

- Maven 依赖下载慢 → 配置阿里云镜像到 `settings.xml`
- npm install 慢 → 设置 `npm config set registry https://registry.npmmirror.com`
- 编译失败 → 确认 JDK 版本与根 `pom.xml` 的 `java.version` 一致
