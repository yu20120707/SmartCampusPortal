# Reference Implementation Strategy

## Purpose

This document defines how the team should use GitHub reference projects after the MVP baseline.

The goal is maximum reuse with minimum scaffold risk:

- reuse RuoYi core capabilities directly
- adapt external project domain models and flows
- avoid copying another scaffold wholesale
- preserve the current Java/Spring Boot/MyBatis/Vue 2/Element UI stack

## Reuse Rules

1. Reuse the current RuoYi-Vue scaffold first.
2. Put campus business code under `com.ruoyi.campus.*`, `mapper/campus/**`, `views/campus/**`, and `src/api/campus/**`.
3. Use external projects for entity models, workflow states, menu grouping, page interaction ideas, and API contracts.
4. Do not import another project's framework, auth model, build system, or frontend stack.
5. Adapt one bounded capability at a time and verify it independently.
6. Keep schema changes under the `campus_` table prefix.
7. Do not change RuoYi core modules unless a specific blocker requires it.

## Current Reference Catalog

| Reference project | Reuse level | Recommended use |
|---|---|---|
| `yangzongzhuan/RuoYi-Vue` | Direct | RBAC, menus, dicts, logs, monitor, cache, codegen, scaffold conventions |
| `MUYIio/Smart-Campus-System` | Adapt | Campus feature grouping, dashboard/workbench ideas, module inventory |
| `Nevercomes/ruoyi-vue-elective` | Adapt | Elective/course selection flow, eligibility rules, teacher/student course context |
| `Mason-zy/Online-Course-Scheduling_Online-Course-Selection_Academic-Affairs-System` | Adapt | Scheduling, academic affairs entities, course selection administration |
| `KonBAI-Q/RuoYi-Flowable-Plus` | Adapt | Process model, task inbox/outbox, approval status, form/process boundary |
| `huangxing2010/ry-vue-flowable-xg` | Reference | OA menu layout, approval entry flow, mobile-facing workflow ideas |
| `YunaiV/ruoyi-vue-pro` | Adapt | Payment order abstraction, callback lifecycle, reconciliation and refund concepts |
| `zylu/card_consumption` | Adapt | Card account, ledger, consumption/recharge records, device and scene concepts |

More detail is tracked in `docs/ai/reuse-matrix.md`.

## When To Reference Versus Implement Locally

| Situation | Default decision |
|---|---|
| MVP demo gap | Implement locally first with the smallest safe flow |
| Production complex domain | Study a mature reference model before coding |
| External integration | Build an adapter and mirror table boundary |
| Framework or stack mismatch | Adapt contracts and concepts, do not copy files wholesale |
| Shared schema or public API change | Plan as a higher-risk task with migration and rollback notes |

## Module Strategy After MVP

### Academic V2

Reference `ruoyi-vue-elective` and the scheduling project for:

- capacity control
- prerequisites
- selection rounds
- waitlists
- schedule administration
- score audit and publishing rules

### OA V2

Reference Flowable-based RuoYi projects for:

- process definition
- task inbox/outbox
- process status tracking
- approval history
- form/process separation

Do not replace the current lightweight MVP workflow until Flowable scope and migration path are explicit.

### Payment And Card V2

Reference `ruoyi-vue-pro` and `card_consumption` for:

- payment order lifecycle
- callback processing
- reconciliation
- refund and settlement state
- campus card account and ledger modeling
- external card system synchronization

No real-money flow should be added without security, idempotency, reconciliation, and audit design.

### Student Affairs V2

Reference campus-domain projects for:

- counselor workflows
- student evaluations
- dormitory management
- awards, grants, work-study, loans, and subsidies lifecycle

Keep read-first MVP screens stable while adding workflows in separate slices.

### Asset V2

Reference campus/admin systems for:

- return flow
- repair flow
- inventory audit
- procurement
- depreciation
- scrap/disposal

Do not turn the MVP borrow flow into a full ERP module without a separate design gate.

## Guardrails

- Do not rewrite RuoYi auth, role, menu, or permission systems.
- Do not migrate the frontend to Vue 3 to match a reference project.
- Do not split into microservices for the MVP or near-term V2.
- Do not copy external SQL directly without table-prefix, permission, and seed-data review.
- Do not add real payment/card integrations without a security review.
- Do not treat seed-style SQL as production migration scripts.

## Practical Workflow

For each post-MVP capability:

1. Identify the smallest user-visible capability.
2. Pick one primary reference project.
3. Extract the data model, state machine, and page flow.
4. Adapt it into the current campus package boundaries.
5. Add SQL, API, menu, and page changes as one bounded slice.
6. Run backend build, frontend build, XML parse, and authenticated API smoke.
7. Record the result in `.ai/evaluation.md` and `.ai/run-trace.md`.
