# Implementation Plan

## Phase A: V1 Contract Setup

1. lock V1 scope to portal, academic, teacher view, and leader dashboard
2. define acceptance criteria by role: student, teacher, leader, admin
3. confirm deferred scope: payment, card, OA, asset breadth

## Phase B: Implementation Landing Zones

1. define backend package boundaries for `portal`, `academic`, and `dashboard`
2. define frontend API and page boundaries under `campus`
3. define MyBatis XML resource boundaries under `mapper/campus`
4. define the first SQL slice needed for read-first academic and portal pages
5. define RuoYi menu and role-menu seed data for V1 pages

## Phase C: First Coding Slice Plan

1. create campus package and route skeletons
2. build portal aggregate API contract
3. implement academic read APIs for courses, timetable, scores, and exams
4. add teacher teaching view APIs and pages
5. add leader dashboard metric APIs and charts
6. add V1 menu/permission seed records

## Phase D: Verification Baseline

1. backend compile check
2. frontend build check with `npm run build:prod`
3. campus API smoke checks
4. role-based route and menu verification
5. manual student/teacher/leader smoke paths

## Phase E: V2 Incremental Runtime Slices

1. add lightweight OA application workflow for submit, todo, approve, and reject
2. add lightweight campus card account workflow for balance, ledger, and demo recharge
3. add lightweight payment center workflow for pending items, payment records, and demo payment
4. add lightweight asset borrow workflow for available assets, borrow application, and leader approval
5. aggregate V2 runtime signals into the leader dashboard
6. add read-first student affairs profile and record views
7. add lightweight student-side elective enroll/drop flow
8. add lightweight teacher-side score entry flow
9. keep all slices inside `campus/**` backend/frontend boundaries
10. defer real Flowable orchestration, real payment/card gateway integration, full student affairs workflows, full academic scheduling, score audit/publishing, and full asset lifecycle management until external integration decisions exist
11. verify every slice with backend compile, frontend build, XML parse, and at least one authenticated API smoke path

## Escalation Rules

- escalate if V1 starts requiring workflow, payment, or external card dependencies
- escalate if academic scope expands into full scheduling or selection administration
- escalate if leader metrics require data not available from the agreed V1 tables
- escalate if campus card or payment moves from internal demo ledger to real money or third-party callbacks
- escalate if OA moves from single-step approval to multi-level process modeling or Flowable integration
- escalate if asset management moves from borrow approval into procurement, depreciation, audit, repair, or return lifecycle management
- escalate if student affairs moves from read-first records into counselor workflow, dormitory operations, evaluations, or奖助勤贷补 lifecycle management
- escalate if elective management requires capacity control, prerequisites, selection rounds, waitlists, or admin scheduling screens
- escalate if score management requires audit workflow, publish/retract state, make-up exams, transcript locking, or historical versioning

## Phase F: MVP Freeze And Hardening

1. freeze the current MVP feature scope as the running acceptance baseline
2. document current included capabilities, non-goals, known constraints, and acceptance gates
3. document the reference-project strategy for post-MVP complex capabilities
4. prioritize setup/runbook documentation, repeatable smoke scripts, SQL initialization notes, and browser smoke expansion
5. defer Flowable orchestration, real payment/card integration, full scheduling, score audit, full student affairs workflows, and full asset lifecycle until a separate V2 design gate
