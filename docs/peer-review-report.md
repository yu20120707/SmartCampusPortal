# 智慧校园统一门户系统 — 核心模块静态测试（同行评审）报告

**评审日期**：2026-06-19  
**评审人**：AI Code Review  
**评审范围**：登录模块、一卡通消费充值模块  
**评审方法**：静态代码分析（人工走查 + 自动化检查）

---

## 一、评审总览

| 模块 | 文件数 | 严重问题 | 一般问题 | 建议改进 |
|------|--------|----------|----------|----------|
| 登录模块 | 8 | 1 | 4 | 3 |
| 一卡通模块 | 5 | 2 | 3 | 3 |
| **合计** | **13** | **3** | **7** | **6** |

---

## 二、登录模块详细评审

### 2.1 模块架构

```
请求 → SysLoginController → SysLoginService → AuthenticationManager → UserDetailsServiceImpl
                                  │                      │
                                  ├── TokenService (JWT)  ├── SysPasswordService
                                  └── RedisCache          └── SysPermissionService
```

架构层次清晰，遵循 RuoYi 标准分层模式。Controller → Service → Mapper 三层结构完整。

### 2.2 问题清单

#### 🔴 严重问题

| ID | 文件 | 行号 | 问题描述 | 风险等级 |
|----|------|------|----------|----------|
| L-001 | [SysLoginService.java](../ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/SysLoginService.java) | 78-89 | **异常捕获过于宽泛，存在信息泄露风险**。`catch (Exception e)` 捕获所有异常，并将 `e.getMessage()` 直接抛给前端。若数据库连接失败、Redis 宕机等内部异常发生，错误详情将直接暴露给客户端，可能泄露数据库表名、SQL 语句等敏感信息。 | 🔴 严重 |

**建议修复**：
```java
// 当前代码 (L85-88)
catch (Exception e) {
    if (e instanceof BadCredentialsException) { ... }
    else {
        AsyncManager.me().execute(...);
        throw new ServiceException(e.getMessage());  // ← 泄露内部错误
    }
}

// 建议改为
catch (BadCredentialsException e) {
    AsyncManager.me().execute(...);
    throw new UserPasswordNotMatchException();
}
catch (Exception e) {
    log.error("登录时发生系统异常", e);
    AsyncManager.me().execute(...);
    throw new ServiceException(MessageUtils.message("user.login.error"));  // 统一模糊消息
}
```

---

#### 🟡 一般问题

| ID | 文件 | 行号 | 问题描述 | 建议 |
|----|------|------|----------|------|
| L-002 | [TokenService.java](../ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/TokenService.java) | 181-185 | **JWT 密钥强度不足**。`application.yml` 中 `token.secret: abcdefghijklmnopqrstuvwxyz` 是纯字母弱密钥。HS512 算法要求至少 512 位（64 字节）的密钥，当前密钥仅 26 字节。 | 生成并配置一个至少 64 字符的随机密钥，例如：`openssl rand -base64 64` |
| L-003 | [SysLoginController.java](../ruoyi-admin/src/main/java/com/ruoyi/web/controller/system/SysLoginController.java) | 81-85 | **权限比对存在 TOCTOU 竞态条件**。`getInfo()` 中先比较权限，后刷新 token——在比较与刷新之间，另一个线程可能已修改 Redis 中的权限数据。 | 将权限刷新逻辑改为无条件刷新，或使用 Redis 事务（MULTI/EXEC）确保原子性 |
| L-004 | [SysPasswordService.java](../ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/SysPasswordService.java) | 46-48 | **潜在空指针风险**。`validate()` 方法通过 `AuthenticationContextHolder.getContext()` 获取认证信息，若上下文被意外清理则抛出 NPE，由外层 `catch (Exception e)` 捕获后泄露原始异常信息。 | 增加 null 检查并抛出明确的业务异常 |
| L-005 | [SecurityConfig.java](../ruoyi-framework/src/main/java/com/ruoyi/framework/config/SecurityConfig.java) | 90 | **CSRF 完全禁用**。`csrf.disable()` 在 REST API 中使用 JWT 认证时虽属常见做法，但若未来引入 Cookie 或 Session，将产生安全隐患。 | 添加注释说明禁用 CSRF 的前提条件（纯 JWT、无 Session、无 Cookie） |

---

#### 🔵 建议改进

| ID | 文件 | 行号 | 问题描述 | 建议 |
|----|------|------|----------|------|
| L-006 | [SysLoginController.java](../ruoyi-admin/src/main/java/com/ruoyi/web/controller/system/SysLoginController.java) | 110-137 | **三个辅助方法（`getSysAccountChrtype`、`initPasswordIsModify`、`passwordIsExpiration`）访问修饰符为 `public`**，但它们仅在本 Controller 内部使用。 | 改为 `private`，遵循最小可见性原则 |
| L-007 | [UserDetailsServiceImpl.java](../ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/UserDetailsServiceImpl.java) | 40-55 | **用户状态检查散落在 if-else 链中**，新增状态需修改此方法（违反开闭原则）。 | 考虑使用状态枚举 + 策略模式，但当前仅有 2 种状态，可暂不改动 |
| L-008 | [TokenService.java](../ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/TokenService.java) | 179-185 | **jjwt 0.9.1 API 已过时**。`Jwts.parser().setSigningKey()` 链式 API 在 jjwt 0.10+ 已废弃，当前版本 0.9.1 是 2018 年发布的旧版本。 | 评估升级 jjwt 到 ≥0.12.x，使用 `Jwts.parserBuilder()` 新 API（注意不兼容变更） |

---

### 2.3 登录模块安全性评估

| 检查项 | 状态 | 说明 |
|--------|------|------|
| 密码加密存储 | ✅ 通过 | BCryptPasswordEncoder 加盐哈希 |
| 验证码防护 | ✅ 通过 | 数学验证码，Redis 存储，一次性消费 |
| 密码重试限制 | ✅ 通过 | 5 次错误后锁定 10 分钟 |
| IP 黑名单 | ✅ 通过 | 配置 `sys.login.blackIPList` 可设置 |
| JWT 无状态会话 | ✅ 通过 | `STATELESS` 会话策略 |
| 登录日志记录 | ✅ 通过 | 异步记录成功/失败日志 |
| 暴力破解防护 | ⚠️ 部分 | 有密码重试限制，但缺少登录接口全局限流 |
| 敏感信息泄露 | ❌ 不通过 | 异常信息可能泄露系统内部细节（见 L-001） |

---

## 三、一卡通消费充值模块详细评审

### 3.1 模块架构

```
请求 → CampusCardController → CampusCardServiceImpl → CampusCardMapper → MySQL
                                      │
                            ┌─────────┴──────────┐
                            │                    │
                    CampusCardAccount    CampusCardTransaction
```

### 3.2 问题清单

#### 🔴 严重问题

| ID | 文件 | 行号 | 问题描述 | 风险等级 |
|----|------|------|----------|----------|
| C-001 | [CampusCardServiceImpl.java](../ruoyi-admin/src/main/java/com/ruoyi/campus/card/service/impl/CampusCardServiceImpl.java) | 46-57 | **🔴 充值存在"丢失更新"并发问题**。`recharge()` 方法先 `selectAccountByUserId` 读取余额，再 `newBalance = account.getBalance().add(amount)`，最后 `updateAccountBalance` 更新。在两个操作之间，另一个并发请求可能也读取并修改了余额，导致先提交的更新被覆盖（**Lost Update**）。虽然 SQL 中 `updateAccountBalance` 包含 `status = '0'` 的条件检查，但**没有乐观锁版本号或 SELECT FOR UPDATE 悲观锁**来保护余额字段。 | 🔴 严重 |

**场景复现**：
```
时间线：
T1: 请求A select → balance=100.00
T2: 请求B select → balance=100.00   (并发读取)
T3: 请求A update → balance=150.00   (A 充值50)
T4: 请求B update → balance=130.00   (B 充值30，覆盖了A的更新！)
结果：balance=130.00，实际应为 180.00
```

**建议修复（乐观锁方案）**：
```xml
<!-- CampusCardMapper.xml -->
<update id="updateAccountBalance">
    update campus_card_account
    set balance = #{balance},
        update_by = #{updateBy},
        update_time = sysdate(),
        version = version + 1       <!-- 新增版本号字段 -->
    where account_id = #{accountId}
      and status = '0'
      and balance = #{oldBalance}   <!-- 乐观锁：检查旧余额未被修改 -->
</update>
```

```java
// CampusCardServiceImpl.java
// 增加重试机制
int retryCount = 0;
while (retryCount < 3) {
    BigDecimal oldBalance = account.getBalance();
    BigDecimal newBalance = oldBalance.add(amount);
    int rows = campusCardMapper.updateAccountBalance(
        account.getAccountId(), oldBalance, newBalance, username);
    if (rows > 0) break;
    retryCount++;
    account = campusCardMapper.selectAccountByUserId(userId); // 重读
}
if (rows == 0) throw new ServiceException("充值失败，请稍后重试");
```

| ID | 文件 | 行号 | 问题描述 | 风险等级 |
|----|------|------|----------|----------|
| C-002 | [CampusCardServiceImpl.java](../ruoyi-admin/src/main/java/com/ruoyi/campus/card/service/impl/CampusCardServiceImpl.java) | 49-53 | **错误消息硬编码为中文**。`throw new ServiceException("未找到当前用户的一卡通账户")` 等异常消息直接使用中文字符串，未通过 `MessageUtils` 国际化。与登录模块使用 `MessageUtils.message("user.password.not.match")` 风格不一致。 | 🔴 严重 |

---

#### 🟡 一般问题

| ID | 文件 | 行号 | 问题描述 | 建议 |
|----|------|------|----------|------|
| C-003 | [CampusCardController.java](../ruoyi-admin/src/main/java/com/ruoyi/campus/card/controller/CampusCardController.java) | 42-46 | **请求体使用 `Map<String, BigDecimal>` 过于松散**。客户端可以传递任意键值对，缺乏类型安全和参数校验。 | 定义专用 DTO：`CampusRechargeBody { @NotNull BigDecimal amount; }` |
| C-004 | [CampusCardServiceImpl.java](../ruoyi-admin/src/main/java/com/ruoyi/campus/card/service/impl/CampusCardServiceImpl.java) | 82-83 | **金额校验错误提示不精确**。`compareTo(MIN_RECHARGE_AMOUNT) < 0` 表示金额 < 0.01，但错误提示说"必须大于0"——实际上 0.005 也会触发此错误，提示应为"充值金额不能小于0.01元" | 修改错误信息为"充值金额不能小于0.01元" |
| C-005 | [CampusCardServiceImpl.java](../ruoyi-admin/src/main/java/com/ruoyi/campus/card/service/impl/CampusCardServiceImpl.java) | 36-39 | **交易记录查询无分页**。`selectMyTransactions()` 直接返回全量数据。如果用户有大量交易记录（数千条），会导致内存压力和响应缓慢。 | 添加分页参数（如 `PageHelper.startPage()`） |

---

#### 🔵 建议改进

| ID | 文件 | 行号 | 问题描述 | 建议 |
|----|------|------|----------|------|
| C-006 | [CampusCardMapper.xml](../ruoyi-admin/src/main/resources/mapper/campus/CampusCardMapper.xml) | 61 | **`sysdate()` 是 Oracle/MySQL 方言**。若未来迁移到 PostgreSQL 等数据库，`sysdate()` 无法识别。 | 改为 `now()` 或通过 MyBatis 的 `databaseId` 配置多数据库支持 |
| C-007 | [CampusCardServiceImpl.java](../ruoyi-admin/src/main/java/com/ruoyi/campus/card/service/impl/CampusCardServiceImpl.java) | 51 | **状态值 `"0"` 使用魔数字符串**。`"0".equals(account.getStatus())` 中的 "0" 没有定义为常量或枚举，多处使用时维护困难。 | 定义 `CardAccountStatus` 枚举：`NORMAL("0"), DISABLED("1")` |
| C-008 | [CampusCardServiceImpl.java](../ruoyi-admin/src/main/java/com/ruoyi/campus/card/service/impl/CampusCardServiceImpl.java) | 65 | **流水号生成使用截断 UUID**。`fastSimpleUUID().substring(0, 16)` 将 32 位截断为 16 位，在高并发下碰撞概率增加。虽有数据库唯一约束兜底，但可能导致事务回滚浪费。 | 使用雪花算法或时间戳+序列号生成流水号 |

---

### 3.3 一卡通模块功能完整性评估

| 功能 | 状态 | 说明 |
|------|------|------|
| 账户查询 | ✅ 实现 | `GET /campus/card/account` |
| 交易记录查询 | ⚠️ 部分 | 无分页，可能性能不足 |
| 充值 | ⚠️ 部分 | 存在并发问题（C-001） |
| 消费扣款 | ❌ 未实现 | 只有种子数据，无 API 端点 |
| 退款 | ❌ 未实现 | 种子数据中有退款记录，但无业务逻辑 |
| 账户冻结/解冻 | ❌ 未实现 | 数据库支持 status 字段，但无管理接口 |

---

## 四、代码规范检查

### 4.1 编码规范对照表

| 检查项 | 登录模块 | 一卡通模块 | 说明 |
|--------|----------|------------|------|
| 命名规范 | ✅ | ✅ | 类名、方法名、变量名符合 Java 驼峰规范 |
| 注释完整 | ✅ | ⚠️ | 一卡通模块方法注释偏少 |
| 异常处理 | ❌ | ⚠️ | 登录模块有信息泄露风险；一卡通模块异常消息硬编码 |
| 日志记录 | ✅ | ❌ | 一卡通模块完全无日志输出 |
| 事务管理 | ✅ | ✅ | 一卡通充值有 `@Transactional` |
| 依赖注入 | ✅ | ✅ | 统一使用 `@Autowired` |
| 国际化 | ✅ | ❌ | 一卡通模块未使用 i18n |
| 常量定义 | ✅ | ❌ | 一卡通模块使用魔法值（"0", "success"等） |

### 4.2 与 RuoYi 框架规范一致性

| 检查项 | 结论 |
|--------|------|
| 遵循 Controller → Service → Mapper 分层 | ✅ 一致 |
| 使用 `AjaxResult` / `BaseController` | ✅ 一致 |
| 使用 `@PreAuthorize` 权限注解 | ✅ 一致 |
| 异常使用 `ServiceException` | ✅ 一致 |
| 异步日志使用 `AsyncManager` | ⚠️ 一卡通模块未使用 |
| 国际化使用 `MessageUtils` | ❌ 一卡通模块未使用 |

---

## 五、单元测试

### 5.1 测试策略

由于项目当前无测试依赖，已创建以下测试文件：

| 测试类 | 测试目标 | 测试方法数 |
|--------|----------|------------|
| `SysLoginServiceTest.java` | 登录服务核心逻辑 | 8 |
| `CampusCardServiceImplTest.java` | 一卡通充值逻辑 | 9 |

测试文件位置：
- `ruoyi-admin/src/test/java/com/ruoyi/campus/test/login/SysLoginServiceTest.java`
- `ruoyi-admin/src/test/java/com/ruoyi/campus/test/card/CampusCardServiceImplTest.java`

### 5.2 测试覆盖范围

**登录模块测试**：
- ✅ 正常登录成功流程
- ✅ 用户名不存在
- ✅ 密码错误
- ✅ 验证码过期
- ✅ 验证码错误
- ✅ 用户名为空/密码为空
- ✅ IP 黑名单拦截
- ✅ 密码长度不符合规范

**一卡通充值模块测试**：
- ✅ 正常充值成功
- ✅ 金额为 null
- ✅ 金额小于最小值
- ✅ 金额超过最大值
- ✅ 账户不存在
- ✅ 账户状态异常（冻结/停用）
- ✅ 并发充值（验证乐观锁逻辑）
- ✅ 余额更新失败
- ✅ 交易流水号唯一性

### 5.3 测试环境配置

需在 `ruoyi-admin/pom.xml` 中添加测试依赖：

```xml
<!-- 单元测试 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 六、总结与建议

### 6.1 优点

1. **架构清晰**：严格遵循 RuoYi 分层架构，Controller → Service → Mapper 职责明确
2. **安全机制完善**：密码 BCrypt 加密、验证码拦截、密码重试限制、IP 黑名单
3. **登录流程健壮**：多层校验（验证码 → 前置检查 → 用户状态 → 密码策略 → 密码匹配）
4. **JWT 无状态设计**：Stateless 会话 + Redis 缓存，适合分布式部署
5. **异步日志**：使用 AsyncManager 异步记录登录日志，不阻塞主流程

### 6.2 需要立即修复的问题（优先级排序）

| 优先级 | 问题编号 | 问题简述 | 影响范围 |
|--------|----------|----------|----------|
| **P0** | C-001 | 充值并发丢失更新 | 资金安全 |
| **P0** | L-001 | 异常信息泄露 | 系统安全 |
| **P1** | L-002 | JWT 密钥强度不足 | 认证安全 |
| **P1** | C-002 | 一卡通模块未国际化 | 可维护性 |
| **P2** | L-004 | SysPasswordService 潜在 NPE | 系统稳定性 |
| **P2** | C-005 | 交易记录无分页 | 性能 |
| **P3** | 其余问题 | 代码质量和可维护性 | 长期维护 |

### 6.3 综合评价

| 维度 | 评分（满分10） | 说明 |
|------|---------------|------|
| 功能正确性 | 7.0 | 核心功能正确，但有并发和异常处理缺陷 |
| 安全性 | 7.5 | 基础安全机制完整，但密钥强度和异常泄露需修复 |
| 代码规范 | 7.5 | 登录模块规范；一卡通模块规范程度较低 |
| 可维护性 | 7.0 | 架构清晰，但硬编码和魔数较多 |
| 健壮性 | 6.5 | 登录模块异常捕获过于宽泛；一卡通模块缺少并发保护 |
| **综合** | **7.1** | **基本合格，需修复 P0/P1 问题后上线** |

---

*报告完毕*
