# 登录模块黑盒测试计划

**版本**：v1.0  
**日期**：2026-06-19  
**测试对象**：SmartCampusPortal 登录模块  
**测试类型**：黑盒测试（API 级别功能测试）  
**测试范围**：`POST /login`, `GET /captchaImage`, `GET /getInfo`, `GET /getRouters`, `POST /logout`

---

## 1. 测试目标

验证登录模块的 API 端点功能是否符合需求规格，包括：
- 用户认证流程的正确性
- 验证码机制的完整性
- 异常场景的错误处理
- 会话令牌的生命周期管理
- 安全策略（密码重试限制、IP 黑名单）的有效性

## 2. 测试环境

| 项目 | 配置 |
|------|------|
| 后端地址 | `http://127.0.0.1:8080` |
| 数据库 | MySQL 8.0+ (已完成种子数据导入) |
| 缓存 | Redis 7.0+ |
| 测试账号 | `student` / `admin123`（学生角色） |
| 测试账号 | `teacher` / `admin123`（教师角色） |
| 测试账号 | `leader` / `admin123`（领导角色） |
| 测试工具 | curl + bash 脚本 |

### 2.1 前置条件

1. MySQL 服务运行中，已导入 `sql/ry_20260417.sql` 及所有 campus SQL 脚本
2. Redis 服务运行中
3. Spring Boot 后端应用启动在 8080 端口
4. 验证码功能开启（`captchaType: math`）

## 3. 测试范围

### 3.1 包含的功能点

| 编号 | 功能 | API 端点 | 方法 |
|------|------|----------|------|
| F1 | 获取验证码 | `/captchaImage` | GET |
| F2 | 用户登录 | `/login` | POST |
| F3 | 获取用户信息 | `/getInfo` | GET |
| F4 | 获取路由菜单 | `/getRouters` | GET |
| F5 | 用户登出 | `/logout` | POST |

### 3.2 不包含的范围

- 注册功能（`/register`）
- 前端 UI 测试
- 压力/性能测试
- 第三方 OAuth 登录

## 4. 测试用例设计

### 4.1 正常流程测试

| 用例ID | 用例名称 | 前置条件 | 测试步骤 | 预期结果 |
|--------|----------|----------|----------|----------|
| TC-LOGIN-001 | 正确凭据登录 | 验证码开启 | 1. GET /captchaImage 获取 uuid 和验证码<br>2. POST /login 提交正确用户名/密码/验证码 | 返回 code=200, 包含有效 token |
| TC-LOGIN-002 | 获取用户信息 | 已有有效 token | 1. GET /getInfo 携带 token | 返回用户信息(user/roles/permissions) |
| TC-LOGIN-003 | 获取路由菜单 | 已有有效 token | 1. GET /getRouters 携带 token | 返回菜单树结构 |
| TC-LOGIN-004 | 用户登出 | 已有有效 token | 1. POST /logout 携带 token | 返回成功，token 失效 |
| TC-LOGIN-005 | 多角色登录 | 三种角色账号存在 | 分别用 student/teacher/leader 登录 | 各自获取对应角色权限 |

### 4.2 异常流程测试

| 用例ID | 用例名称 | 测试步骤 | 预期结果 |
|--------|----------|----------|----------|
| TC-LOGIN-101 | 用户名为空 | POST /login username="" | code≠200, 提示用户不存在 |
| TC-LOGIN-102 | 密码为空 | POST /login password="" | code≠200, 提示用户不存在 |
| TC-LOGIN-103 | 用户名不存在 | POST /login username="nonexist" | code≠200, 提示用户不存在 |
| TC-LOGIN-104 | 密码错误 | POST /login password="wrongpass" | code≠200, 提示密码错误 |
| TC-LOGIN-105 | 无验证码提交 | 不获取验证码，直接 POST /login | code≠200, 提示验证码过期 |
| TC-LOGIN-106 | 错误验证码 | POST /login code="wrong" | code≠200, 提示验证码错误 |
| TC-LOGIN-107 | 不带 token 访问受保护接口 | 无 Authorization header GET /getInfo | code=401, 未授权 |
| TC-LOGIN-108 | 伪造 token 访问 | Authorization: Bearer fake_token | code=401, 未授权 |
| TC-LOGIN-109 | 过期 token 访问 | token 过期后 GET /getInfo | code=401, 未授权 |
| TC-LOGIN-110 | 登出后 token 重用 | POST /logout 后再次 GET /getInfo | code=401, 未授权 |

### 4.3 安全策略测试

| 用例ID | 用例名称 | 测试步骤 | 预期结果 |
|--------|----------|----------|----------|
| TC-LOGIN-201 | 密码连续错误锁定 | 对同一账号连续 POST /login 错误密码 6 次 | 第 6 次提示密码重试次数超限 |
| TC-LOGIN-202 | 锁定后正确密码也无法登录 | 密码锁定后 POST /login 正确密码 | 仍然提示重试次数超限 |
| TC-LOGIN-203 | 锁定时间后恢复 | 等待 lockTime 分钟后用正确密码登录 | 可正常登录 |
| TC-LOGIN-204 | XSS 注入防护 | POST /login username="<script>alert(1)</script>" | 返回业务错误，不执行脚本 |
| TC-LOGIN-205 | SQL 注入防护 | POST /login username="' OR '1'='1" | 返回业务错误，不绕过认证 |

### 4.4 边界值测试

| 用例ID | 用例名称 | 测试步骤 | 预期结果 |
|--------|----------|----------|----------|
| TC-LOGIN-301 | 用户名最小长度 | POST /login username="ab"（2字符） | code≠200 (最小3字符) |
| TC-LOGIN-302 | 用户名最大长度 | POST /login username="a"×51 | code≠200 (最大50字符) |
| TC-LOGIN-303 | 密码最小长度 | POST /login password="1234"（4字符） | code≠200 (最小5字符) |
| TC-LOGIN-304 | 密码最大长度 | POST /login password="a"×21 | code≠200 (最大20字符) |
| TC-LOGIN-305 | 特殊字符用户名 | POST /login username="test@#$%user" | 根据配置返回结果 |
| TC-LOGIN-306 | Unicode 密码 | POST /login password="密码测试123" | 正常处理（UTF-8） |

## 5. 测试执行计划

| 阶段 | 任务 | 预计时间 | 优先级 |
|------|------|----------|--------|
| Phase 1 | 正常流程测试 (TC-001~005) | 10 min | P0 |
| Phase 2 | 异常流程测试 (TC-101~110) | 15 min | P0 |
| Phase 3 | 安全策略测试 (TC-201~205) | 15 min | P1 |
| Phase 4 | 边界值测试 (TC-301~306) | 10 min | P1 |
| Phase 5 | 测试报告生成 | 10 min | P2 |

**总预计时间**：约 60 分钟

## 6. 通过标准

- P0 用例**100% 通过**
- P1 用例**≥90% 通过**
- 无安全相关的阻塞问题

## 7. 风险与依赖

| 风险 | 影响 | 缓解措施 |
|------|------|----------|
| Redis 服务不可用 | 无法测试验证码/会话 | 确保 Redis 已启动 |
| 数据库种子数据不一致 | 测试账号不可用 | 检查 `sys_user` 表 |
| 验证码识别困难 | 无法自动化测试 | 测试脚本使用 UUID 方式复用验证码键 |
| 密码锁定时间等待 | 测试时间延长 | 锁定测试安排在最后，或手动清理 Redis |

---

*计划完毕*
