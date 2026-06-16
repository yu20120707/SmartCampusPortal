# AGENTS.md

## Project Type

基于 **RuoYi-Vue** 的高校智慧校园统一门户系统。
后端：Java / Spring Boot / MyBatis / Maven
前端：Vue 2 / Element UI
数据库：MySQL
缓存：Redis
构建：Maven + npm

## Required Reading

Read `docs/ai/README.md` first for the full document index.

For non-trivial tasks, read the relevant `docs/ai/*` files for the area you are changing.

Always read active `.ai/` task files when they exist.

## Workflow

- Classify non-trivial tasks as small, medium, or large before editing.
- `small` is for local bug fixes, config changes, or single-file edits.
- `medium` is for bounded multi-file tasks that need an explicit plan and verification.
- `large` is for complex work that needs spec → plan → diff → final gates.
- If a simple task fails twice or the impact expands, escalate the execution level.

## Project Structure

```
smart-campus-portal
├── ruoyi-admin/          # 后端启动模块（Spring Boot Application）
├── ruoyi-common/         # 公共工具模块
├── ruoyi-framework/      # 核心框架（Spring Security, JWT, Redis）
├── ruoyi-generator/      # 代码生成器
├── ruoyi-system/         # 系统管理模块
├── campus/               # [新增] 智慧校园业务模块
│   ├── portal/           # 门户聚合接口
│   ├── academic/         # 教务服务
│   ├── student/          # 学工服务
│   ├── card/             # 一卡通服务
│   ├── payment/          # 缴费服务
│   ├── office/           # OA 办公
│   ├── asset/            # 资产服务
│   └── dashboard/        # 领导驾驶舱
└── ruoyi-ui/             # 前端 Vue 项目
    └── src/views/campus/ # [新增] 校园门户页面
```

## Key Principles

1. **Do not modify RuoYi core modules** (`ruoyi-framework`, `ruoyi-common`, `ruoyi-system`) unless absolutely necessary.
2. **New business code goes into `campus/` package** under `ruoyi-admin/src/main/java/com/ruoyi/campus/`.
3. **Database tables use `campus_` prefix** (e.g., `campus_student`, `campus_course`).
4. **Follow RuoYi layered style**: Controller → Service/ServiceImpl → Mapper → MySQL.
5. **Use RuoYi's existing utilities** (`AjaxResult`, `TableDataInfo`, `PageDomain`, `BaseController`, `@PreAuthorize`).
6. **Keep the C++-related docs deleted or replaced** — this is a Java/Spring Boot project on Windows.

## Safety

- Do not overwrite existing RuoYi files unless explicitly allowed.
- Do not treat `.ai/` as long-lived architecture knowledge.
- Do not bypass review gates or safe-write rules.
- Do not refactor unrelated code opportunistically.
- Do not upgrade Vue/Vue-version or Spring Boot version without explicit instruction — keep RuoYi's current tech stack.
