# SmartCampusPortal Product Scope

## Purpose

This document normalizes the relationship between the two source briefs currently guiding the project.

## Source Relationship

### Current Target: 全真项目 6 高校智慧校园门户

Project 6 is the delivery target.

The product we are building is a university smart campus portal that integrates existing campus business systems from four angles:

- data integration
- workflow integration
- function integration
- interface integration

The current delivery should focus on a web portal for three user groups:

- student
- teacher
- leader

Mobile web, WeChat, and native app surfaces are explicitly deferred.

### Reference Pool: 全真项目 5 智慧校园服务平台

Project 5 is not the delivery target. It is a reference capability pool used to infer a fuller campus portal feature map because Project 6 has limited description.

Project 5 helps us reason about:

- student portal widgets
- teacher portal widgets
- leader portal widgets
- future integration directions
- extension modules that should not be forced into the MVP

Project 5 should guide naming, grouping, and future expansion, but not create an obligation to implement every listed capability now.

## MVP Product Principle

The MVP should first make the system feel like a real campus portal after login.

For each role, the first screen should answer:

- What should I pay attention to now?
- What campus services can I enter quickly?
- What data belongs to my role?

The MVP is successful when student, teacher, and leader users see meaningfully different portal content and can complete a small set of end-to-end demo flows.

## Role Scope

### Student MVP

- campus portal home
- announcements/news entry
- personal academic profile
- course list and timetable
- grades and exam arrangements
- elective enroll/drop demo
- campus card balance and recent transactions
- pending payment items and payment records
- lightweight application submission
- student affairs profile and records
- available asset list and borrow application

### Teacher MVP

- campus portal home
- personal teaching profile
- teaching courses and timetable
- exam context
- class score entry demo
- application submission
- campus card balance and recent transactions
- available asset list and borrow application

### Leader MVP

- campus portal home
- leader dashboard
- approval todo list
- asset approval todo list
- student affairs overview
- operational metrics for approval, payment, card transaction, and asset status

## Deferred Extension Pool

These capabilities are valuable but should be documented before implementation and only pulled into active scope through a separate planning gate.

### Integration

- real single sign-on to academic affairs, enrollment, student affairs, OA, and asset systems
- external system adapters and synchronization jobs
- unified campus resource access with external authorization boundaries

### Mobile

- mobile web
- WeChat integration
- native app

### Academic

- full scheduling administration
- capacity control
- prerequisites
- selection rounds
- waitlists
- make-up and retake registration
- teaching evaluation
- graduation design workflow
- level exam registration
- score audit, publish/retract, transcript locking, and score history

### Student Affairs

- counselor workflow
- scholarship and grant lifecycle
- student loan lifecycle
- work-study jobs and application results
- dormitory management
- psychological counseling and testing
- discipline competition registration and team management

### Office And Workflow

- Flowable or equivalent workflow engine
- multi-level approval
- official document circulation
- meeting notifications
- work plan scheduling

### Payment And Card

- real payment gateway
- payment callback
- reconciliation
- refund and settlement
- real campus card gateway/device integration
- consumption device scenes

### Asset

- full fixed-asset account
- return
- repair
- inventory audit
- procurement
- depreciation
- scrap

### Leader Analytics

- research project statistics
- teacher paper statistics
- enrollment plan
- freshman registration statistics
- student source distribution
- enrollment rate by major
- teaching quality statistics
- teacher statistics by employment type

## Productization Tasks

Immediate productization work should prioritize:

1. removing visible scaffold branding from the web UI and README
2. strengthening role-specific portal home content
3. adding richer seed data for credible demos
4. documenting extension capability pools before implementation
5. expanding browser smoke coverage for teacher and leader paths
