# Maven 构建与依赖管理

## 目的

本项目使用 Maven 作为后端构建工具。RuoYi-Vue 采用多模块 Maven 结构。

## 模块结构

```
ruoyi-admin         → Spring Boot 启动模块（含新增的 campus 业务包）
ruoyi-common        → 公共工具模块
ruoyi-framework     → 核心框架模块
ruoyi-generator     → 代码生成器模块
ruoyi-system        → 系统管理模块
ruoyi-ui            → 前端（Vue 项目，独立 npm 构建）
```

## 常用命令

```bash
# 编译整个后端项目
mvn clean install -DskipTests

# 仅编译某个模块
mvn clean install -pl ruoyi-admin -am -DskipTests

# 运行后端测试
mvn test -pl ruoyi-admin

# 打包可执行 Jar
mvn clean package -DskipTests

# Spring Boot 启动
mvn spring-boot:run -pl ruoyi-admin
```

## 新增依赖

新增校园业务模块时，如果引入新依赖（如 Apache POI、ECharts Java 库等），需要：
1. 在 `ruoyi-admin/pom.xml` 添加依赖
2. 或统一在 `ruoyi-common/pom.xml` 添加公共依赖
3. 保持与 RuoYi 原有 Spring Boot / MyBatis 版本一致

## 注意事项

- 不要随意升级 RuoYi 锁定的 Spring Boot / MyBatis / Vue 版本
- 新增的 `campus/` 包放在 `ruoyi-admin` 模块下，不需要额外创建 Maven 子模块
- 前端通过 `npm install` + `npm run dev` 独立构建，不依赖 Maven
