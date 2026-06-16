# REST API 设计规范

## 目的

本项目后端对外暴露 RESTful API，前端通过 Axios 调用。

## URL 规范

- 系统管理 API：`/system/**`（RuoYi 原有）
- 校园业务 API：`/campus/**`

## 校园 API 路径设计

```
门户模块：
  GET    /campus/portal/current         当前用户门户聚合数据
  GET    /campus/portal/student         学生门户数据
  GET    /campus/portal/teacher         教师门户数据
  GET    /campus/portal/leader          领导门户数据

教务模块：
  GET    /campus/academic/schedule/my   我的课程表
  GET    /campus/academic/grades/my     我的成绩
  GET    /campus/academic/exams/my      我的考试安排
  POST   /campus/academic/grades        录入成绩（教师）

学工模块：
  GET    /campus/student/applications/list    申请列表（管理）
  POST   /campus/student/applications         提交申请（学生）
  GET    /campus/student/applications/my      我的申请（学生）

一卡通：
  GET    /campus/card/my                      我的一卡通信息
  GET    /campus/card/transactions/recent     最近消费记录

缴费模块：
  GET    /campus/payment/my                   我的缴费信息

OA 模块：
  GET    /campus/office/work-plans/my         我的工作计划
  GET    /campus/office/todo-documents/my     我的待办公文

领导大屏：
  GET    /campus/dashboard/leader             领导驾驶舱数据
```

## 统一响应格式

遵循 RuoYi 的 `AjaxResult` 格式：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

分页接口：

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 100,
  "rows": []
}
```

## HTTP 方法语义

| 方法 | 语义 |
|------|------|
| GET | 查询 |
| POST | 创建 |
| PUT | 更新 |
| DELETE | 删除 |

## 参数校验

使用 Spring `@Validated` + 注解校验（`@NotBlank`, `@NotNull`, `@Size` 等）。

## 接口文档

可选集成 Knife4j（Swagger 增强版），访问 `http://localhost:8080/doc.html`。
