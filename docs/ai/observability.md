# 日志与监控

## 目的

本项目的日志记录和可观测性策略。

## 日志框架

RuoYi 使用 **Logback** + **SLF4J**。

配置在 `ruoyi-admin/src/main/resources/logback.xml`。

默认日志级别：`INFO`，开发时可调整为 `DEBUG`。

## 日志使用规范

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger log = LoggerFactory.getLogger(XxxService.class);

log.info("成绩录入：学生 {}，课程 {}，分数 {}", studentId, courseId, score);
log.error("成绩录入失败", e);
```

## 操作日志（重要）

RuoYi 提供 `@Log` 注解自动记录操作日志到 `sys_oper_log` 表：

```java
@Log(title = "成绩管理", businessType = BusinessType.UPDATE)
@PreAuthorize("@ss.hasPermi('campus:academic:grade:edit')")
@PutMapping("/grades/{id}")
public AjaxResult updateGrade(@PathVariable Long id, @RequestBody Grade grade) {
    // ...
}
```

新增校园业务时，重要操作（增删改）务必添加 `@Log` 注解。

## 可观测性（MVP 阶段可选）

- Spring Boot Actuator 可提供健康检查端点（RuoYi 默认开启 `/actuator/health`）
- 领导驾驶舱的统计数据可以视为业务层面的可观测性

## 调试技巧

- IDEA 断点调试
- 查看 `logs/` 目录下的日志文件
- 前端 F12 → Network 查看 API 请求和响应
