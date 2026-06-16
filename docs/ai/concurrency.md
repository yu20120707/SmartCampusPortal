# Java 并发与事务

## 目的

本项目中业务 Service 层的并发和事务控制规范。

## 事务管理

使用 Spring `@Transactional` 注解管理声明式事务：

```java
@Service
public class GradeServiceImpl implements IGradeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertGrade(Grade grade) {
        // 多个 Mapper 操作在同一个事务中执行
        gradeMapper.insert(grade);
        return gradeMapper.updateStudentStatus(grade.getStudentId());
    }
}
```

## 事务注意事项

- 默认 RuntimeException 回滚，checked exception 不回滚
- 使用 `rollbackFor = Exception.class` 确保所有异常回滚
- `@Transactional` 在 private 方法上无效（需 public 方法）
- 同类中方法调用 `@Transactional` 不生效（AOP 代理限制）

## 并发控制

- RuoYi 项目为单体应用，默认不存在高并发场景
- 如需乐观锁，可在实体类使用 `@Version` 注解配合 MyBatis-Plus
- 热点数据（如公告）可通过 Redis 缓存减少数据库压力

## 异步操作

Spring `@Async` 可用于耗时操作的异步执行：

```java
@Service
public class PortalService {

    @Async
    public void refreshPortalCache() {
        // 刷新门户缓存
    }
}
```

需要在启动类或配置类添加 `@EnableAsync`。
