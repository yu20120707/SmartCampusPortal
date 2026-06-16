# 测试指南

## 目的

本项目的测试策略以 Maven 编译、后端测试、前端构建和手动 API 冒烟为基线。
当前脚手架尚未默认引入完整的 Spring Boot Test 依赖；新增测试前先确认对应模块的测试依赖。

## 测试层级

| 层级 | 框架 | 用途 |
|------|------|------|
| 编译检查 | Maven | 确认后端模块可编译 |
| 单元测试 | JUnit / Mockito（按需添加） | Service 层逻辑测试 |
| 集成测试 | Spring Boot Test（按需添加） | Controller/Repository 集成 |
| API 测试 | Postman / Swagger UI | 手动接口验证 |
| 前端检查 | Vue CLI build | 确认 Vue 页面可生产构建 |

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
npm run build:prod
```

当前 `ruoyi-ui/package.json` 未配置 `test:unit`。如果后续添加 Vue 单元测试，需要先补齐测试依赖和 npm script。

## 测试注意事项

- 数据库测试建议使用 H2 内存数据库或单独的测试库
- RuoYi 原有的测试不要由于新增业务而破坏
- 新增校园业务的 Service 层建议编写单元测试（MVP 阶段可选）
