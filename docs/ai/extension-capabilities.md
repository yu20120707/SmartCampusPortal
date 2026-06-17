# Extension Capabilities

## Purpose

This document records deferred capabilities for SmartCampusPortal after the Web MVP.

These items are useful for later planning, but they are not part of the frozen MVP unless the team explicitly reopens scope.

## Extension Intake Rules

- New requirements should first be assigned to one domain below.
- Each extension should define target users, data source, integration boundary, and verification method before implementation.
- External integrations should not be mocked as production-ready features.
- Mobile web, WeChat, and native app work should be planned separately and is not included in the Web MVP.

## 教务

Deferred capabilities:

- Full course scheduling administration.
- Course capacity control with concurrency-safe seat locking.
- Prerequisite rules and course conflict detection.
- Selection rounds, waitlists, add/drop windows, and admin override.
- Make-up and retake registration.
- Exam arrangement publishing and change notification.
- Online teaching evaluation and teacher evaluation result query.
- Graduation design workflow, including teacher announcements, review comments, and progress tracking.
- Level exam information browsing and registration.
- Score audit, publish/retract workflow, transcript locking, and score change history.
- School-wide timetable query.

Future planning notes:

- This domain needs stricter data correctness than the MVP demo flow.
- Selection and score flows should not be expanded without transaction and audit design.

## 学工

Deferred capabilities:

- Counselor workbench.
- Student status verification and periodic profile confirmation.
- Scholarship, grant, subsidy, and student loan lifecycle.
- Work-study job publishing, application, review, and result query.
- Dormitory distribution and dormitory management.
- Psychological counseling appointment and appointment query.
- Online psychological testing.
- Discipline competition registration and team management.
- Practice project appointment.
- Poverty student identification and assistance records.
- Student award, punishment, honor, and comprehensive development archive.

Future planning notes:

- Student affairs data often has privacy and permission boundaries.
- Counselor, student, department leader, and school leader views should be separated before implementation.

## OA

Deferred capabilities:

- Flowable or equivalent workflow engine integration.
- Multi-level approval routing.
- Official document circulation.
- Submitted document tracking and leader processing status.
- Meeting notifications.
- Personal work plan scheduling.
- Department staff work plan query for leaders.
- School-wide faculty address book.
- Forum or internal discussion area.

Future planning notes:

- The current MVP uses a lightweight application/todo state machine only.
- Do not replace it with a workflow engine until process ownership and migration path are explicit.

## 一卡通

Deferred capabilities:

- Real campus card gateway integration.
- Consumption device integration.
- Card recharge gateway and reconciliation.
- Loss reporting, card replacement, and card status management.
- Merchant/category-level transaction statistics.
- Abnormal transaction detection.
- More than recent demo transactions, including paginated historical statements.

Future planning notes:

- Real card integration touches money-like balances and device-side correctness.
- Demo recharge should remain clearly marked as demo before gateway integration exists.

## 缴费

Deferred capabilities:

- Real payment gateway integration.
- Payment callback processing.
- Reconciliation and settlement.
- Refund workflow.
- Invoice or receipt management.
- Tuition, book fee, exam fee, registration fee, thesis defense fee, graduation application fee, and other fee category lifecycle.
- Exemption and waiver workflow.
- Arrears reminder and overdue rules.

Future planning notes:

- Payment work requires stronger audit, idempotency, and failure recovery design.
- Do not treat the MVP demo payment state transition as a real payment flow.

## 资产

Deferred capabilities:

- Full fixed-asset ledger.
- Return, renewal, repair, and loss workflows.
- Inventory audit.
- Procurement request and acceptance.
- Depreciation and scrap.
- Department-level and school-level asset statistics.
- Personal fixed asset query.
- Asset permission rules by department, role, and asset category.

Future planning notes:

- Asset lifecycle should be modeled separately from the current borrow demo.
- Approval, stock, and return flows need clear concurrency and audit behavior.

## 领导驾驶舱

Deferred capabilities:

- Annual enrollment plan distribution.
- Freshman registration count, rate, and source-area statistics.
- Student registration and payment overview by college or major.
- Student status change analysis.
- Enrollment rate by major.
- Current student source-area statistics.
- Current student course-selection statistics.
- Total student count by college and major.
- College and major information directory.
- Teaching quality statistics.
- Teacher information query.
- Teacher statistics by full-time, part-time, and external categories.
- Research project statistics.
- Teacher paper publication statistics.
- Cross-domain drill-down from dashboard cards to detailed lists.

Future planning notes:

- Dashboard indicators should not be added as isolated SQL snippets.
- Each indicator needs a business definition, refresh cadence, permission boundary, and source table or integration source.

## Parking Lot

The following are cross-cutting extensions and should be planned after the domain scope is clear:

- Real single sign-on to academic affairs, enrollment, student affairs, OA, and asset systems.
- External system adapters and synchronization jobs.
- Unified campus resource access and authorization boundary.
- Mobile web, WeChat, and native app surfaces.
- Production migration strategy for seed-style SQL.
- Full automated unit, integration, and browser smoke coverage.
