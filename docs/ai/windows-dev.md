# Windows 开发调试指南

## 目的

本项目在 Windows 环境下开发。以下是与 Linux/Mac 环境的关键差异和调试建议。

## 路径差异

- 路径分隔符使用 `\`（但 Java 中建议使用 `/` 或 `File.separator` 保证跨平台）
- 在 PowerShell 中使用 `Get-ChildItem` 代替 `ls`，`Remove-Item` 代替 `rm`
- 环境变量引用用 `$env:VAR` 而不是 `$VAR`

## 后端启动（IntelliJ IDEA + Maven）

```powershell
# 编译
mvn clean install -DskipTests

# 启动（在 ruoyi-admin 目录下）
mvn spring-boot:run

# 或者在 IDEA 中直接运行 RuoYiApplication.java
```

## 前端启动（VS Code / WebStorm）

```powershell
cd ruoyi-ui
npm install
npm run dev
```

默认访问 http://localhost:80 。如需指定端口，可使用：

```powershell
npm run dev -- --port 1024
```

## 后端默认端口

- 后端：http://localhost:8080
- 前端代理到后端 URL 在 `ruoyi-ui/vue.config.js` 中配置

## 数据库

- MySQL 推荐使用本地或 Docker 运行
- Redis 推荐使用本地 Windows 版或 WSL2
- RuoYi 默认数据库初始化脚本在仓库根目录 `sql/` 下

## 常见问题

| 问题 | 解决 |
|------|------|
| 端口被占用 | `netstat -ano | findstr :8080` 查找 PID，`taskkill /PID <PID> /F` |
| Redis 连不上 | 检查 `application.yml` 中 Redis 配置，确认 Redis 服务已启动 |
| MySQL 连接失败 | 检查 `application-druid.yml` 中数据库连接参数 |
| 前端代理 404 | 检查 `ruoyi-ui/.env.development` 中 `VUE_APP_BASE_API` 是否正确 |
| Maven 依赖下载慢 | 检查 `settings.xml` 中是否配置了阿里云镜像 |

## 热更新

- IDEA：安装 JRebel 插件可实现后端热更新（可选）
- Vue 前端：`npm run dev` 默认支持热重载
