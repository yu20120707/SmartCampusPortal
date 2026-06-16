# 测试指南

## 目的

本项目的测试策略基于 JUnit 5 + Spring Boot Test。

## 测试层级

| 层级 | 框架 | 用途 |
|------|------|------|
| 单元测试 | JUnit 5 + Mockito | Service 层逻辑测试 |
| 集成测试 | Spring Boot Test + @SpringBootTest | Controller/Repository 集成 |
| API 测试 | Postman / Knife4j | 手动接口验证 |

## 后端测试命令

```powershell
# 运行全部测试
mvn test

# 运行特定模块测试
mvn test -pl ruoyi-admin

# 运行特定测试类
mvn test -pl ruoyi-admin -Dtest=MyControllerTest

# 跳过测试编译
mvn clean install -DskipTests
```

## 前端测试

```powershell
cd ruoyi-ui
npm run test:unit   # Vue 单元测试（如果有）
```

## 测试注意事项

- 数据库测试建议使用 H2 内存数据库或单独的测试库
- RuoYi 原有的测试不要由于新增业务而破坏
- 新增校园业务的 Service 层建议编写单元测试（MVP 阶段可选）
