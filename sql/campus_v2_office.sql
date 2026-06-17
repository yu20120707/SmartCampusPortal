-- ----------------------------
-- SmartCampusPortal V2 office application slice
-- Apply after sql/campus_v1_init.sql and sql/campus_v1_menu.sql.
-- ----------------------------

drop table if exists campus_application;

create table campus_application (
  application_id    bigint(20)    not null auto_increment comment '申请ID',
  application_no    varchar(40)   not null comment '申请编号',
  application_type  varchar(30)   not null comment '申请类型',
  title             varchar(100)  not null comment '申请标题',
  content           varchar(1000) not null comment '申请说明',
  applicant_user_id bigint(20)    not null comment '申请人用户ID',
  applicant_name    varchar(50)   not null comment '申请人名称',
  applicant_role    varchar(30)   default '' comment '申请人角色',
  status            char(1)       default '1' comment '状态（0草稿 1待审批 2已通过 3已驳回）',
  approver_user_id  bigint(20)    default null comment '审批人用户ID',
  approver_name     varchar(50)   default '' comment '审批人名称',
  review_comment    varchar(500)  default '' comment '审批意见',
  submit_time       datetime      default null comment '提交时间',
  review_time       datetime      default null comment '审批时间',
  create_by         varchar(64)   default '' comment '创建者',
  create_time       datetime      default null comment '创建时间',
  update_by         varchar(64)   default '' comment '更新者',
  update_time       datetime      default null comment '更新时间',
  remark            varchar(500)  default null comment '备注',
  primary key (application_id),
  unique key uk_campus_application_no (application_no),
  key idx_campus_application_applicant (applicant_user_id),
  key idx_campus_application_status (status)
) engine=innodb auto_increment=100 comment='校园申请表';

insert into campus_application values
(100, 'APP202606170001', 'leave', '外出参加竞赛请假', '参加省级软件设计竞赛，需要请假一天。', 201, 'student', 'student', '1', null, '', '', sysdate(), null, 'student', sysdate(), '', null, 'V2样例待审批申请'),
(101, 'APP202606170002', 'repair', '实验室设备报修', '明德楼A201投影设备无法正常开机。', 202, 'teacher', 'teacher', '2', 203, 'leader', '已安排信息中心处理。', sysdate(), sysdate(), 'teacher', sysdate(), 'leader', sysdate(), 'V2样例已审批申请');

insert into sys_menu values(2020, '校园办公', 2000, 5, 'office', null, '', '', 1, 0, 'M', '0', '0', '', 'form', 'admin', sysdate(), '', null, '校园办公目录');
insert into sys_menu values(2021, '我的申请', 2020, 1, 'my', 'campus/office/my', '', '', 1, 0, 'C', '0', '0', 'campus:office:apply', 'edit', 'admin', sysdate(), '', null, '我的校园申请');
insert into sys_menu values(2022, '审批待办', 2020, 2, 'todo', 'campus/office/todo', '', '', 1, 0, 'C', '0', '0', 'campus:office:approve', 'validCode', 'admin', sysdate(), '', null, '校园审批待办');

insert into sys_menu values(2023, '申请提交', 2021, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:office:apply', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2024, '审批处理', 2022, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:office:approve', '#', 'admin', sysdate(), '', null, '');

insert into sys_role_menu values(110, 2020);
insert into sys_role_menu values(110, 2021);
insert into sys_role_menu values(110, 2023);

insert into sys_role_menu values(111, 2020);
insert into sys_role_menu values(111, 2021);
insert into sys_role_menu values(111, 2023);

insert into sys_role_menu values(112, 2020);
insert into sys_role_menu values(112, 2022);
insert into sys_role_menu values(112, 2024);
