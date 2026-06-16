# SmartCampusPortal V1 Delivery Plan

## Goal

Deliver the first usable campus portal slice described by the PRD:

- role-based portal home
- academic core for students
- teaching view for teachers
- baseline dashboard for leaders

This is the first implementation slice that should make the system feel like a campus portal instead of a generic RuoYi admin.

## V1 Functional Scope

### Student

- portal home with shortcut cards and announcements
- current term course list
- timetable
- score query
- exam arrangement query

### Teacher

- portal home with pending items and shortcuts
- my teaching courses
- teaching timetable
- exam-related teaching view if data is present

### Leader

- leader portal home
- dashboard cards and charts for high-level academic metrics
- trend and distribution panels based on available data

### Admin

- maintain portal configuration and menu visibility through existing RuoYi capabilities
- use campus business pages through configured role/menu permissions

## V1 Non-Goals

- no full student affairs workflow suite
- no campus card account lifecycle
- no payment channel integration
- no OA process engine rollout
- no asset management breadth beyond future placeholders
- no migration away from current RuoYi-Vue family stack

## Reuse Strategy

### Direct Reuse

- `RuoYi-Vue`
  - auth
  - role and menu control
  - dicts and notices
  - dashboard shell
  - ECharts foundation already used by the frontend

### Adapted Reuse

- `ruoyi-vue-elective`
  - academic entity ideas
  - student/teacher role split
  - course-facing interaction patterns

- `Online-Course-Scheduling_Online-Course-Selection_Academic-Affairs-System`
  - scheduling and academic affairs modeling ideas

- `Smart-Campus-System`
  - feature grouping
  - workbench and campus page inventory ideas

## Planned Local Landing Zones

### Backend

- `ruoyi-admin/src/main/java/com/ruoyi/campus/portal/**`
- `ruoyi-admin/src/main/java/com/ruoyi/campus/academic/**`
- `ruoyi-admin/src/main/java/com/ruoyi/campus/dashboard/**`
- `ruoyi-admin/src/main/resources/mapper/campus/**`

Expected first-layer package pattern:

- `controller`
- `domain`
- `mapper`
- `service`
- `service/impl`

### Frontend

- `ruoyi-ui/src/api/campus/portal.js`
- `ruoyi-ui/src/api/campus/academic.js`
- `ruoyi-ui/src/api/campus/dashboard.js`
- `ruoyi-ui/src/views/campus/portal/**`
- `ruoyi-ui/src/views/campus/academic/**`
- `ruoyi-ui/src/views/campus/dashboard/**`

### SQL

- `sql/campus_v1_init.sql`
- `sql/campus_v1_menu.sql`
- optional incremental follow-up scripts if schema iteration becomes necessary

## Suggested V1 Data Scope

Core tables should stay narrow and support the first visible experience:

- `campus_student`
- `campus_teacher`
- `campus_term`
- `campus_course`
- `campus_course_section`
- `campus_student_course`
- `campus_score`
- `campus_exam`
- `campus_portal_notice` or direct reuse of existing RuoYi notice data when enough

This is a planning baseline, not a final schema contract.

V1 seed data must also include the RuoYi menu and role binding records needed to expose campus pages:

- `sys_menu`
- `sys_role_menu`
- role/user setup notes for `student`, `teacher`, and `leader`

## First Build Slice

Build in this order:

1. campus package skeleton and empty route shell
2. portal aggregate API contract
3. academic read-only queries
4. student portal page
5. teacher teaching view
6. leader dashboard metrics endpoints and charts

The first slice should bias toward read-heavy pages because they verify the information architecture fast and keep rollback cost low.

## Acceptance Criteria

V1 is considered implementation-complete when all of the following are true:

1. login can route different roles to meaningful campus pages
2. portal home shows role-specific cards or sections
3. students can view courses, timetable, scores, and exams
4. teachers can view their teaching-related information
5. leaders can view a dashboard with at least baseline academic metrics
6. backend and frontend additions stay outside core RuoYi modules by default
7. a fresh database can expose V1 campus menus after applying documented SQL/setup steps

## Verification Baseline

Implementation work should verify at minimum:

- backend project compiles
- frontend project builds with `npm run build:prod`
- new campus APIs return expected JSON structures
- role-specific routing and menu visibility behave correctly
- at least one manual smoke path each for student, teacher, and leader

## Escalation Triggers

Escalate the task beyond the V1 boundary if any of these become true:

- academic scope expands into full selection/scheduling administration
- leader dashboard requires cross-domain metrics not available from V1 tables
- external card or payment systems become mandatory dependencies for V1
- approval workflows are required before portal launch

## Deferred To V2+

- campus card
- payment
- OA and approval flows
- broad student affairs workflows
- asset workflows
- richer operational analytics
