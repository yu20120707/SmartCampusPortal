-- ----------------------------
-- SmartCampusPortal V2 student affairs seed data
-- Apply after sql/campus_v1_init.sql and sql/campus_v1_menu.sql.
-- This is a read-first student affairs slice, not a full counselor workflow suite.
-- ----------------------------

drop table if exists campus_student_affairs_record;
drop table if exists campus_student_affairs_profile;

create table campus_student_affairs_profile (
    profile_id bigint(20) not null auto_increment comment '学工档案ID',
    student_id bigint(20) not null comment '学生ID',
    counselor_name varchar(64) not null comment '辅导员',
    political_status varchar(30) default '' comment '政治面貌',
    dormitory varchar(100) default '' comment '宿舍',
    emergency_contact varchar(64) default '' comment '紧急联系人',
    emergency_phone varchar(30) default '' comment '紧急联系电话',
    status_summary varchar(500) default '' comment '在校状态摘要',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (profile_id),
    unique key uk_student_affairs_profile_student (student_id)
) engine=innodb auto_increment=100 comment='学工扩展档案';

create table campus_student_affairs_record (
    record_id bigint(20) not null auto_increment comment '学工记录ID',
    student_id bigint(20) not null comment '学生ID',
    record_type varchar(30) not null comment '记录类型',
    title varchar(100) not null comment '记录标题',
    level_name varchar(50) default '' comment '级别',
    record_date date not null comment '记录日期',
    status varchar(20) not null default 'active' comment '状态',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (record_id),
    key idx_student_affairs_record_student (student_id, record_date),
    key idx_student_affairs_record_type (record_type)
) engine=innodb auto_increment=100 comment='学工记录';

insert into campus_student_affairs_profile values
(100, 100, '刘辅导员', '共青团员', '梅园3栋 402', '陈家长', '13900000001', '在校，学业状态稳定，近期参与软件设计竞赛备赛。', 'admin', sysdate(), '', null, 'V2学工样例档案'),
(101, 101, '刘辅导员', '共青团员', '梅园3栋 405', '李家长', '13900000002', '在校，需关注高数课程学习进展。', 'admin', sysdate(), '', null, 'V2学工样例档案'),
(102, 102, '赵辅导员', '群众', '兰园2栋 318', '王家长', '13900000003', '在校，积极参与学生社团活动。', 'admin', sysdate(), '', null, 'V2学工样例档案'),
(103, 103, '刘辅导员', '共青团员', '梅园4栋 216', '赵家长', '13900000004', '在校，担任班级学习委员，正在准备创新创业训练项目。', 'admin', sysdate(), '', null, 'MVP学工样例档案'),
(104, 104, '刘辅导员', '群众', '竹园1栋 508', '孙家长', '13900000005', '在校，近期申请开放实验室资源用于课程实践。', 'admin', sysdate(), '', null, 'MVP学工样例档案'),
(105, 105, '赵辅导员', '中共预备党员', '兰园2栋 401', '周家长', '13900000006', '在校，参与学院志愿服务和数据分析竞赛。', 'admin', sysdate(), '', null, 'MVP学工样例档案'),
(106, 106, '王辅导员', '共青团员', '菊园5栋 302', '吴家长', '13900000007', '在校，英语竞赛备赛中，综合表现稳定。', 'admin', sysdate(), '', null, 'MVP学工样例档案'),
(107, 107, '陈辅导员', '群众', '松园6栋 118', '郑家长', '13900000008', '在校，实训课程出勤良好，需关注职业规划。', 'admin', sysdate(), '', null, 'MVP学工样例档案'),
(108, 108, '黄辅导员', '共青团员', '枫园2栋 609', '钱家长', '13900000009', '在校，作品集建设进展良好。', 'admin', sysdate(), '', null, 'MVP学工样例档案');

insert into campus_student_affairs_record values
(100, 100, 'honor', '校级优秀学生干部', '校级', '2026-05-20', 'active', 'admin', sysdate(), '', null, ''),
(101, 100, 'practice', '暑期社会实践先进个人', '院级', '2025-09-15', 'active', 'admin', sysdate(), '', null, ''),
(102, 101, 'aid', '国家助学金资格确认', '二等', '2026-04-10', 'active', 'admin', sysdate(), '', null, ''),
(103, 102, 'honor', '社团活动积极分子', '院级', '2026-03-18', 'active', 'admin', sysdate(), '', null, ''),
(104, 103, 'competition', '创新创业训练项目立项', '校级', '2026-04-22', 'active', 'admin', sysdate(), '', null, ''),
(105, 104, 'practice', '开放实验室实践预约通过', '学院', '2026-05-08', 'active', 'admin', sysdate(), '', null, ''),
(106, 105, 'volunteer', '校园迎新志愿服务', '院级', '2025-09-01', 'active', 'admin', sysdate(), '', null, ''),
(107, 106, 'honor', '英语演讲比赛三等奖', '校级', '2026-05-12', 'active', 'admin', sysdate(), '', null, ''),
(108, 107, 'warning', '职业规划访谈提醒', '辅导员', '2026-03-28', 'active', 'admin', sysdate(), '', null, ''),
(109, 108, 'portfolio', '数字媒体作品展入围', '院级', '2026-06-05', 'active', 'admin', sysdate(), '', null, ''),
(110, 100, 'aid', '勤工俭学岗位申请通过', '校级', '2026-06-01', 'active', 'admin', sysdate(), '', null, '');

insert into sys_menu values(2060, '我的学工', 2000, 11, 'student/my', 'campus/student/my', '', '', 1, 0, 'C', '0', '0', 'campus:student:view', 'user', 'admin', sysdate(), '', null, '学生学工档案');
insert into sys_menu values(2061, '学工概览', 2000, 12, 'student/overview', 'campus/student/overview', '', '', 1, 0, 'C', '0', '0', 'campus:student:manage', 'peoples', 'admin', sysdate(), '', null, '领导学工概览');
insert into sys_menu values(2062, '学工查看', 2060, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:student:view', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2063, '学工管理', 2061, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:student:manage', '#', 'admin', sysdate(), '', null, '');

insert into sys_role_menu values(110, 2060);
insert into sys_role_menu values(110, 2062);
insert into sys_role_menu values(112, 2061);
insert into sys_role_menu values(112, 2063);
