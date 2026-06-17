# Scope

## Allowed Write Scope

- `docs/ai/**`
- `.ai/spec.md`
- `.ai/scope.md`
- `.ai/epic.md`
- `.ai/implementation-plan.md`
- `.ai/affected-files.md`
- `.ai/evaluation.md`
- `.ai/state.json`
- `scripts/ai_build.*`
- `scripts/ai_check.*`
- `ruoyi-admin/src/main/resources/application.yml`

## Forbidden Scope

- `ruoyi-framework/**`
- `ruoyi-common/**`
- `ruoyi-system/**`
- runtime code, SQL schema, or UI implementation for campus modules

## Future Implementation Scope For V1

When implementation begins, expected primary code scope is:

- `ruoyi-admin/src/main/java/com/ruoyi/campus/portal/**`
- `ruoyi-admin/src/main/java/com/ruoyi/campus/academic/**`
- `ruoyi-admin/src/main/java/com/ruoyi/campus/dashboard/**`
- `ruoyi-admin/src/main/resources/mapper/campus/**`
- `ruoyi-ui/src/api/campus/**`
- `ruoyi-ui/src/views/campus/**`
- `sql/campus_*.sql`
