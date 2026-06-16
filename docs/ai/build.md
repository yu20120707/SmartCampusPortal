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

## 常见构建问题

- Maven 依赖下载慢 → 配置阿里云镜像到 `settings.xml`
- npm install 慢 → 设置 `npm config set registry https://registry.npmmirror.com`
- 编译失败 → 确认 JDK 版本与根 `pom.xml` 的 `java.version` 一致
