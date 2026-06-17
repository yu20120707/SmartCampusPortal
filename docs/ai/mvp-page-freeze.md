# MVP Page Freeze

## Purpose

This document freezes the Web MVP page scope for 全真项目 6 高校智慧校园门户.

The MVP has three user types:

- 学生
- 教师
- 领导

Mobile web, WeChat, and native app pages are not part of this MVP.

## Page Scope Rules

- The first screen after login should be role-specific.
- The first screen should summarize important information and expose quick entry points.
- Secondary pages should support the smallest complete demo flow.
- Pages outside this document are treated as deferred unless needed to fix an MVP blocker.
- Existing system management pages may remain for admin/development use, but they are not counted as campus MVP pages.

## 学生

### 首屏

| 页面 | 路由/组件 | MVP 内容 | 验收口径 |
|------|-----------|----------|----------|
| 校园门户首页 | `/campus/portal` / `views/campus/portal/index.vue` | 学生身份识别、课程/成绩/考试/缴费/一卡通/申请等聚合卡片、待关注事项、快捷入口 | 学生登录后能看到学生语境的数据聚合，而不是通用后台首页 |

### 二级页面

| 模块 | 页面 | 路由/组件 | MVP 能力 |
|------|------|-----------|----------|
| 教务 | 我的教务 | `/campus/academic/student` / `views/campus/academic/student.vue` | 学籍/个人教务信息、已选课程、课程表、成绩、考试安排、可选课程、选课/退课 demo |
| OA | 我的申请 | `/campus/office/my` / `views/campus/office/my.vue` | 查看本人申请、提交轻量申请 |
| 一卡通 | 一卡通 | `/campus/card/index` / `views/campus/card/index.vue` | 查看余额、最近交易、演示充值 |
| 缴费 | 缴费中心 | `/campus/payment/index` / `views/campus/payment/index.vue` | 待缴项目、缴费记录、演示支付 |
| 资产 | 资产申请 | `/campus/asset/index` / `views/campus/asset/index.vue` | 可借资产列表、本人借用记录、提交借用申请 |
| 学工 | 我的学工信息 | `/campus/student/my` / `views/campus/student/my.vue` | 学工档案、奖助勤贷/处分/荣誉等记录列表 |

### 学生 MVP 不包含

- 完整选课轮次、候补、容量锁定和先修课规则。
- 补考/重修完整报名闭环。
- 网上评教、等级考试报名、毕业设计完整流程。
- 心理咨询、在线心理测试、宿舍管理、勤工俭学岗位完整生命周期。

## 教师

### 首屏

| 页面 | 路由/组件 | MVP 内容 | 验收口径 |
|------|-----------|----------|----------|
| 校园门户首页 | `/campus/portal` / `views/campus/portal/index.vue` | 教师身份识别、任课/考试/申请/一卡通/资产等聚合卡片、待关注事项、快捷入口 | 教师登录后能看到任课和办公相关入口，而不是学生或领导首页 |

### 二级页面

| 模块 | 页面 | 路由/组件 | MVP 能力 |
|------|------|-----------|----------|
| 教务 | 任课视图 | `/campus/academic/teacher` / `views/campus/academic/teacher.vue` | 教师档案、任课列表、本周课程、监考/考试安排、课程学生成绩、成绩录入 demo |
| OA | 我的申请 | `/campus/office/my` / `views/campus/office/my.vue` | 查看本人申请、提交轻量申请 |
| 一卡通 | 一卡通 | `/campus/card/index` / `views/campus/card/index.vue` | 查看余额、最近交易、演示充值 |
| 资产 | 资产申请 | `/campus/asset/index` / `views/campus/asset/index.vue` | 可借资产列表、本人借用记录、提交借用申请 |

### 教师 MVP 不包含

- 科研项目和论文统计完整模块。
- 网上评教结果分析。
- 通讯录、学校论坛、课程申报完整流程。
- 多级公文流转、工作计划排期和部门级查询。
- 辅导员奖助贷/宿舍/贫困生完整管理闭环。

## 领导

### 首屏

| 页面 | 路由/组件 | MVP 内容 | 验收口径 |
|------|-----------|----------|----------|
| 校园门户首页 | `/campus/portal` / `views/campus/portal/index.vue` | 领导身份识别、全校概览、待办/统计类卡片、快捷入口 | 领导登录后能看到管理视角的聚合数据和待办入口 |

### 二级页面

| 模块 | 页面 | 路由/组件 | MVP 能力 |
|------|------|-----------|----------|
| 领导驾驶舱 | 领导驾驶舱 | `/campus/dashboard` / `views/campus/dashboard/index.vue` | 学生规模、课程类型、成绩趋势、审批、缴费、一卡通、资产统计 |
| OA | 审批待办 | `/campus/office/todo` / `views/campus/office/todo.vue` | 待审批申请列表、同意/驳回 demo |
| 资产 | 资产审批 | `/campus/asset/todo` / `views/campus/asset/todo.vue` | 待审批资产借用、同意/驳回 demo |
| 学工 | 学工概览 | `/campus/student/overview` / `views/campus/student/overview.vue` | 学生学工统计、学生记录概览 |

### 领导 MVP 不包含

- 招生计划、新生报到、生源地、入学率等招生专题。
- 教师信息库、教师分类统计、教学质量深度分析。
- 科研项目、论文发表统计。
- 全校固定资产完整台账、折旧、采购、报废分析。
- 会议通知、公文流转和跨部门工作计划管理。

## MVP Page Inventory

| 页面类型 | 数量 | 页面 |
|----------|------|------|
| 首屏 | 1 个角色化共用页面 | 校园门户首页 |
| 学生二级页 | 6 | 我的教务、我的申请、一卡通、缴费中心、资产申请、我的学工信息 |
| 教师二级页 | 4 | 任课视图、我的申请、一卡通、资产申请 |
| 领导二级页 | 4 | 领导驾驶舱、审批待办、资产审批、学工概览 |

## Change Control

Adding a new MVP page requires one of these reasons:

- It is required to complete a listed MVP flow.
- It replaces an existing page with a clearer role-specific page.
- It fixes an acceptance blocker.

Otherwise, the page should be added to `extension-capabilities.md` first.
