# SmartCampusPortal MVP Scope

## Purpose

This document freezes the current MVP boundary for team visibility.

For concrete first-screen and secondary-page scope by role, see `mvp-page-freeze.md`.
For deferred capabilities grouped by business domain, see `extension-capabilities.md`.

Current product target:

- 全真项目 6 高校智慧校园门户 is the active delivery target.
- 全真项目 5 智慧校园服务平台 is a reference capability pool because Project 6 has limited feature detail.
- Mobile web, WeChat, and native app surfaces are deferred.

MVP principle:

- 可演示
- 可运行
- 可验收
- 复杂系统先借鉴模型，不整包迁移

The MVP is now treated as a running baseline. New work should first improve setup, smoke checks, documentation, and demo stability unless a missing capability blocks MVP acceptance.

## Current MVP Includes

### Unified Portal

- Role-based campus portal entry.
- Aggregated cards for student, teacher, and leader users.
- Campus menu visibility through RuoYi role/menu permissions.

### Academic

- Student academic profile, courses, schedule, grades, and exams.
- Student-side elective enroll/drop flow.
- Teacher teaching view, schedule, exam/course context, and score entry flow.

### Leader Dashboard

- Baseline leader metrics.
- V2 operational metrics for approvals, payments, card transactions, and assets.

### Student Affairs

- Student profile view.
- Student record list.
- Leader-side student affairs overview and record statistics.

### OA Office

- Student application submission.
- Leader todo list.
- Approve/reject lightweight internal workflow.

### Campus Card

- Card account balance.
- Transaction ledger.
- Demo recharge flow.

### Payment Center

- Pending payment items.
- Payment records.
- Demo payment state transition.

### Asset

- Available asset list.
- Borrow application.
- Leader todo approval/reject flow.

## MVP Acceptance Gates

The MVP should be considered acceptable only when these gates pass for the touched slice:

- Backend build: `mvn clean install -pl ruoyi-admin -am -DskipTests`
- Frontend build: `NODE_OPTIONS=--openssl-legacy-provider npm run build:prod`
- MyBatis XML parse for changed mapper files.
- Authenticated API smoke for the changed role path.
- Route/menu visibility smoke for user-facing pages.

Existing approved runtime gates are recorded in `.ai/state.json` and detailed in `.ai/evaluation.md`.

## MVP Non-Goals

These are intentionally deferred and should not be pulled into MVP unless the team explicitly reopens scope:

- Flowable or multi-level approval orchestration.
- Real payment gateway integration, callback processing, reconciliation, settlement, and refund.
- Real campus card gateway/device integration.
- Full course scheduling, capacity control, prerequisites, selection rounds, waitlists, and admin scheduling screens.
- Score audit, publish/retract state, make-up exam handling, transcript locking, and score history.
- Full counselor workflows, evaluation workflows, dormitory management, and 奖助勤贷补 lifecycle.
- Full asset lifecycle including return, repair, audit, procurement, depreciation, and scrap.
- Production migration strategy for existing seed-style SQL.
- Full unit/integration test suite and production deployment automation.

## Known Constraints

- V2 SQL scripts are seed-style initialization scripts and include `drop table`; they are not production migrations.
- Runtime smoke used an isolated local MySQL instance on `127.0.0.1:3307`, not the default MySQL service on `3306`.
- Captcha was disabled through Redis for automated smoke testing.
- Browser smoke is strongest for the student path; teacher and leader paths have mostly been verified through authenticated APIs and route data.
- Current payment/card flows are demo internal state transitions, not external money or device integrations.

## Next MVP Hardening Priorities

1. Remove visible scaffold branding from README and the web UI.
2. Add a setup/runbook document for local database, Redis, backend, and frontend startup.
3. Maintain one-command smoke scripts for repeatable API checks through `scripts/campus_smoke.ps1`.
4. Normalize SQL initialization order and document destructive seed scripts clearly.
5. Add browser smoke coverage for the teacher, leader, OA, payment, card, asset, and student affairs pages.
6. Keep `docs/ai/README.md`, `.ai/state.json`, and `.ai/evaluation.md` synchronized after each accepted slice.

## Scope Decision

Feature work is frozen at the current MVP baseline unless the change is required for:

- MVP stability
- demo reliability
- acceptance verification
- documentation clarity

Complex production-grade capabilities should move into V2 planning and reference implementation analysis instead of being added ad hoc.
