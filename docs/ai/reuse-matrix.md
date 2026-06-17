# SmartCampusPortal Reuse Matrix

## Purpose

This document translates the PRD into a reuse-first implementation plan.

Goal:

- implement 全真项目 6 高校智慧校园门户 on top of the current scaffold
- use 全真项目 5 智慧校园服务平台 as a reference capability pool, not as a mandatory MVP checklist
- maximize reuse of existing open-source implementations
- avoid broad rewrites of RuoYi core modules
- keep new campus business code inside campus-specific backend and frontend areas

## Current Baseline

- Backend scaffold: `RuoYi-Vue` style multi-module project
- Backend runtime: `Spring Boot 4.x` + `JDK 17`
- Frontend runtime: `Vue 2` + `Element UI`
- Existing core modules to preserve: `ruoyi-framework`, `ruoyi-common`, `ruoyi-system`
- Visible product surface should be branded as SmartCampusPortal / 高校智慧校园门户, not as the base scaffold.

Local landing zones for new business code:

- Backend controllers/services/mappers/entities: `ruoyi-admin/src/main/java/com/ruoyi/campus/**`
- Backend mapper XML files: `ruoyi-admin/src/main/resources/mapper/campus/**`
- Frontend pages: `ruoyi-ui/src/views/campus/**`
- Frontend API wrappers: `ruoyi-ui/src/api/campus/**`
- SQL scripts: `sql/campus_*.sql`

## Reuse Rules

1. Reuse official RuoYi capabilities first.
2. Reuse business module structure from campus/education projects second.
3. Reuse workflow and payment module design from mature projects by adaptation, not direct copy-in.
4. Only build from scratch when no suitable source exists or version mismatch is too costly.

Reuse levels:

- `Direct`: structure or code can likely be migrated with limited adaptation
- `Adapt`: borrow schema, API shape, page flow, or service logic and fit it to current stack
- `Reference`: use only for product scope, interaction model, or architecture ideas

## Source Catalog

### A. Base Scaffold

1. `yangzongzhuan/RuoYi-Vue`
   - Fit: exact base product family
   - Reuse: `Direct`
   - Use for: RBAC, menu model, dicts, notices, logs, code generator, monitor, cache, dashboard baseline
   - URL: `https://github.com/yangzongzhuan/RuoYi-Vue`

### B. Campus Domain Structure

1. `MUYIio/Smart-Campus-System`
   - Fit: broad campus feature map close to the PRD
   - Reuse: `Adapt`
   - Use for: module decomposition, page list, menu grouping, dashboard/workbench ideas, academic and office domain boundaries
   - URL: `https://github.com/MUYIio/Smart-Campus-System`

2. `richardgong1987/RuoYi-Cloud-Education`
   - Fit: education platform domain reference
   - Reuse: `Reference`
   - Use for: education-side capability coverage and naming ideas
   - URL: `https://github.com/richardgong1987/RuoYi-Cloud-Education`

### C. Academic / Course Selection

1. `Nevercomes/ruoyi-vue-elective`
   - Fit: RuoYi-based elective/course flow
   - Reuse: `Adapt`
   - Use for: student course selection, teacher-side course view, school dimension support, eligibility rules, announcement prompts
   - URL: `https://github.com/Nevercomes/ruoyi-vue-elective`

2. `Mason-zy/Online-Course-Scheduling_Online-Course-Selection_Academic-Affairs-System`
   - Fit: RuoYi-based scheduling and course selection
   - Reuse: `Adapt`
   - Use for: scheduling, course selection, academic affairs entities, teacher/student role split
   - URL: `https://github.com/Mason-zy/Online-Course-Scheduling_Online-Course-Selection_Academic-Affairs-System`

### D. OA / Approval

1. `KonBAI-Q/RuoYi-Flowable-Plus`
   - Fit: mature workflow extension in the RuoYi ecosystem
   - Reuse: `Adapt`
   - Use for: approval model, process status model, task inbox/outbox, form/process boundary design
   - Warning: based on `RuoYi-Vue-Plus`, not the current project structure
   - URL: `https://github.com/KonBAI-Q/RuoYi-Flowable-Plus`

2. `huangxing2010/ry-vue-flowable-xg`
   - Fit: OA/project workflow on top of RuoYi-Vue
   - Reuse: `Reference`
   - Use for: OA menu layout, approval entry flows, mobile-facing flow ideas
   - URL: `https://github.com/huangxing2010/ry-vue-flowable-xg`

### E. Payment / Card

1. `YunaiV/ruoyi-vue-pro`
   - Fit: mature pay module and broader business module design
   - Reuse: `Adapt`
   - Use for: pay order abstractions, payment status lifecycle, callback handling shape, reconciliation concepts
   - Warning: project stack is more advanced and modular; do not copy wholesale
   - URL: `https://github.com/yunaiv/ruoyi-vue-pro`

2. `zylu/card_consumption`
   - Fit: card consumption system on a RuoYi base
   - Reuse: `Adapt`
   - Use for: campus card account, transaction records, recharge/consume ledger, device/scene concepts
   - Warning: older Spring Boot generation and domain is canteen-consumption-centric
   - URL: `https://github.com/zylu/card_consumption`

## PRD-to-Reuse Matrix

| PRD area | Target capabilities | Primary references | Reuse level | Local backend landing zone | Local frontend landing zone | Notes |
|---|---|---|---|---|---|---|
| Portal home | role-based home, aggregated cards, shortcuts, notices, pending items | `RuoYi-Vue`, `Smart-Campus-System` | Direct + Adapt | `com.ruoyi.campus.portal` | `views/campus/portal` | Build the core portal aggregator here |
| Academic | courses, grades, exams, timetable, teacher teaching view | `ruoyi-vue-elective`, `Online-Course-Scheduling...`, `Smart-Campus-System` | Adapt | `com.ruoyi.campus.academic` | `views/campus/academic` | Highest-value V1 business module |
| Student affairs | student profile, counselor-facing data, evaluation, applications | `Smart-Campus-System` | Adapt | `com.ruoyi.campus.student` | `views/campus/student` | Start with data views before complex workflows |
| OA / approvals | leave/apply/approve, inbox/outbox, process status | `RuoYi-Flowable-Plus`, `ry-vue-flowable-xg` | Adapt | `com.ruoyi.campus.office` | `views/campus/office` | Prefer workflow engine integration over custom state machines |
| Campus card | balance, transactions, recharge/consume history | `card_consumption` | Adapt | `com.ruoyi.campus.card` | `views/campus/card` | If external system exists, build adapter + mirror tables first |
| Payment | payable items, payment records, status callbacks, reconciliation view | `ruoyi-vue-pro` | Adapt | `com.ruoyi.campus.payment` | `views/campus/payment` | Treat payment as a bounded domain, not a generic platform rebuild |
| Asset / admin office | asset list, apply/use/borrow, basic office data | `Smart-Campus-System`, `ruoyi-office` | Reference | `com.ruoyi.campus.asset` | `views/campus/asset` | Start narrow; avoid importing enterprise ERP complexity |
| Leader dashboard | role-specific metrics, charts, trend panels | `RuoYi-Vue`, `Smart-Campus-System` | Direct + Adapt | `com.ruoyi.campus.dashboard` | `views/campus/dashboard` | Reuse existing ECharts foundation |

## Phase Plan

### Phase V1

Must ship first:

- portal aggregator
- academic core: courses, grades, exams, timetable
- teacher-side teaching view
- leader dashboard baseline
- role-specific menus and portal cards

Priority reuse plan:

- use `RuoYi-Vue` for auth, roles, menu, dict, announcements, dashboard shell
- use `ruoyi-vue-elective` and scheduling/elective examples for academic entities and flows
- use `Smart-Campus-System` for feature grouping and page inventory

### Phase V2

Add next:

- campus card
- payable items and payment records
- student applications
- OA approvals and circulation

Priority reuse plan:

- use `card_consumption` for account/ledger ideas
- use `ruoyi-vue-pro` for payment abstractions and status lifecycle
- use `RuoYi-Flowable-Plus` for process modeling instead of hand-rolled approval tables where practical

### Phase V3

Add last:

- richer analytics
- cache optimization
- logs and operational tooling
- documentation completion

Priority reuse plan:

- reuse existing `RuoYi-Vue` monitor, log, cache, scheduled task capabilities
- only add campus-specific observability where the base scaffold is insufficient

## Recommended Build Strategy

1. Do not split into microservices now.
2. Keep business code under `ruoyi-admin` with campus package boundaries.
3. Put MyBatis XML mappings under `ruoyi-admin/src/main/resources/mapper/campus`.
4. Add campus SQL in incremental scripts using the `campus_` prefix.
5. Include RuoYi `sys_menu` and `sys_role_menu` seed data for every user-facing campus page.
6. Keep frontend route/menu additions under `campus` namespaces.
7. Prefer adapter layers for card/payment/OA external systems instead of copying foreign infrastructure models.

## Anti-Goals

- do not rewrite RuoYi auth or menu systems
- do not import another scaffold wholesale
- do not move to Vue 3 just to match a reference project
- do not pull in full ERP/CRM/HRM modules that exceed the PRD
- do not modify `ruoyi-framework`, `ruoyi-common`, or `ruoyi-system` unless blocked

## Execution Order For Actual Implementation

1. Create campus package and route skeletons.
2. Implement portal aggregator contract.
3. Implement academic core entities and APIs.
4. Add role-based home pages for student/teacher/leader.
5. Add dashboard metrics endpoints and charts.
6. Introduce workflow, card, and payment modules in bounded phases.

## Open Risks

1. Some reference projects use different generations of Spring Boot, MyBatis, or RuoYi derivatives.
2. Card and payment may need external system integration rather than pure internal CRUD.
3. OA can sprawl unless approval scope is defined tightly before implementation.
4. The current PRD is embedded in a conversation transcript and should eventually be normalized into a cleaner specification.
