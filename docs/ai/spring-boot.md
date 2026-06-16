# Spring Boot 项目架构

## 目的

本系统后端基于 Spring Boot，以 RuoYi-Vue 为基础框架。
新增校园业务模块遵循 RuoYi 的分层风格和包结构。

## 后端分层

```
Controller            → 接收请求、参数校验、返回 AjaxResult
    ↓
Service / ServiceImpl → 业务规则、角色判断、数据聚合、事务控制
    ↓
Mapper                → 单表/关联/分页/统计 SQL（MyBatis XML）
    ↓
MySQL                 → 数据库
```

## 核心注解

| 层次 | 注解 |
|------|------|
| Controller | `@RestController`, `@RequestMapping`, `@PreAuthorize` |
| Service | `@Service`, `@Transactional`, `@Async` |
| Mapper | `@Mapper`（或 MyBatis-Plus 注解） |
| 实体 | `@Data`, `@TableName("campus_xxx")` |

## 核心基类

RuoYi 提供的可直接复用的基类：

```java
com.ruoyi.common.core.controller.BaseController
  // 提供 getPage(), getDataTable(), success(), error(), AjaxResult 等

com.ruoyi.common.core.domain.AjaxResult
  // 统一 JSON 响应：code / msg / data

com.ruoyi.common.core.page.TableDataInfo
  // 分页响应：total / rows / code / msg

com.ruoyi.common.annotation.Log
  // 操作日志注解

com.ruoyi.common.enums.BusinessType
  // 操作类型：INSERT / UPDATE / DELETE / OTHER
```

## 权限控制

使用 RuoYi 的权限注解体系：

```java
@PreAuthorize("@ss.hasPermi('campus:portal:view')")
@PreAuthorize("@ss.hasPermi('campus:academic:grade:list')")
@PreAuthorize("@ss.hasPermi('campus:academic:grade:edit')")
```

## 获取当前用户

```java
com.ruoyi.common.utils.SecurityUtils.getUserId()
com.ruoyi.common.utils.SecurityUtils.getUsername()
com.ruoyi.common.utils.SecurityUtils.getLoginUser()
```

## 校园业务包路径

新增业务放在 `ruoyi-admin/src/main/java/com/ruoyi/campus/` 下：

```
campus
├── portal/       门户聚合接口
├── academic/     教务服务
├── student/      学工服务
├── card/         一卡通服务
├── payment/      缴费服务
├── office/       OA 办公
├── asset/        资产服务
└── dashboard/   领导驾驶舱
```

每个子包保持 domain / mapper / service / service.impl / controller 结构。
