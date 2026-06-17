-- ----------------------------
-- SmartCampusPortal V2 asset borrow seed data
-- Apply after sql/campus_v1_menu.sql.
-- This is a lightweight borrow/approval demo, not full asset lifecycle management.
-- ----------------------------

drop table if exists campus_asset_borrow;
drop table if exists campus_asset;

create table campus_asset (
    asset_id bigint(20) not null auto_increment comment '资产ID',
    asset_no varchar(64) not null comment '资产编号',
    asset_name varchar(100) not null comment '资产名称',
    asset_type varchar(20) not null comment '资产类型',
    location varchar(100) not null comment '存放位置',
    total_quantity int not null default 1 comment '总数量',
    available_quantity int not null default 1 comment '可借数量',
    status char(1) not null default '0' comment '状态（0正常 1停用）',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (asset_id),
    unique key uk_asset_no (asset_no)
) engine=innodb auto_increment=100 comment='校园资产';

create table campus_asset_borrow (
    borrow_id bigint(20) not null auto_increment comment '借用ID',
    asset_id bigint(20) not null comment '资产ID',
    applicant_user_id bigint(20) not null comment '申请人用户ID',
    applicant_name varchar(64) not null comment '申请人',
    purpose varchar(500) not null comment '借用用途',
    status char(1) not null default '1' comment '状态（1待审批 2已通过 3已驳回）',
    approver_user_id bigint(20) default null comment '审批人用户ID',
    approver_name varchar(64) default '' comment '审批人',
    review_comment varchar(500) default null comment '审批意见',
    apply_time datetime not null comment '申请时间',
    review_time datetime default null comment '审批时间',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (borrow_id),
    key idx_asset_borrow_status (status, apply_time),
    key idx_asset_borrow_user (applicant_user_id, apply_time)
) engine=innodb auto_increment=100 comment='校园资产借用';

insert into campus_asset values(100, 'ASSET20260001', '移动投影仪', 'device', '公共教学楼 A101', 3, 2, '0', 'admin', sysdate(), '', null, '演示资产');
insert into campus_asset values(101, 'ASSET20260002', '会议室 B203', 'room', '行政楼 B203', 1, 1, '0', 'admin', sysdate(), '', null, '演示资产');
insert into campus_asset values(102, 'ASSET20260003', '创新创业指导图书套装', 'book', '图书馆三层服务台', 5, 4, '0', 'admin', sysdate(), '', null, '演示资产');
insert into campus_asset values(103, 'ASSET20260004', '无人机航拍套件', 'device', '创新创业中心 205', 2, 1, '0', 'admin', sysdate(), '', null, 'MVP演示资产');
insert into campus_asset values(104, 'ASSET20260005', '开放实验室工位', 'room', '实验楼 L501', 8, 6, '0', 'admin', sysdate(), '', null, 'MVP演示资产');
insert into campus_asset values(105, 'ASSET20260006', '便携录播设备', 'device', '现代教育技术中心', 4, 3, '0', 'admin', sysdate(), '', null, 'MVP演示资产');

insert into campus_asset_borrow values(100, 100, 201, 'student', '课程展示需要借用投影仪', '1', null, '', null, date_sub(sysdate(), interval 1 day), null, 'student', sysdate(), '', null, '');
insert into campus_asset_borrow values(101, 102, 202, 'teacher', '教学备课参考资料', '2', 203, 'leader', '同意借用', date_sub(sysdate(), interval 3 day), date_sub(sysdate(), interval 2 day), 'teacher', sysdate(), 'leader', sysdate(), '');
insert into campus_asset_borrow values(102, 103, 201, 'student', '学科竞赛作品拍摄需要借用无人机套件', '1', null, '', null, date_sub(sysdate(), interval 6 hour), null, 'student', sysdate(), '', null, '');
insert into campus_asset_borrow values(103, 104, 202, 'teacher', '课程项目路演需要预约开放实验室工位', '2', 203, 'leader', '同意使用，请遵守实验室开放时间。', date_sub(sysdate(), interval 5 day), date_sub(sysdate(), interval 4 day), 'teacher', sysdate(), 'leader', sysdate(), '');
insert into campus_asset_borrow values(104, 105, 201, 'student', '毕业设计访谈录制需要便携录播设备', '3', 203, 'leader', '当前设备排期冲突，请调整借用时间。', date_sub(sysdate(), interval 10 day), date_sub(sysdate(), interval 9 day), 'student', sysdate(), 'leader', sysdate(), '');

insert into sys_menu values(2050, '资产借用', 2000, 8, 'asset', null, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', sysdate(), '', null, '校园资产借用');
insert into sys_menu values(2051, '资产申请', 2050, 1, 'index', 'campus/asset/index', '', '', 1, 0, 'C', '0', '0', 'campus:asset:borrow', 'list', 'admin', sysdate(), '', null, '资产借用申请');
insert into sys_menu values(2052, '资产审批', 2050, 2, 'todo', 'campus/asset/todo', '', '', 1, 0, 'C', '0', '0', 'campus:asset:approve', 'validCode', 'admin', sysdate(), '', null, '资产借用审批');
insert into sys_menu values(2053, '资产查看', 2051, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:asset:view', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2054, '资产借用', 2051, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:asset:borrow', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2055, '资产审批处理', 2052, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:asset:approve', '#', 'admin', sysdate(), '', null, '');

insert into sys_role_menu values(110, 2050);
insert into sys_role_menu values(110, 2051);
insert into sys_role_menu values(110, 2053);
insert into sys_role_menu values(110, 2054);
insert into sys_role_menu values(111, 2050);
insert into sys_role_menu values(111, 2051);
insert into sys_role_menu values(111, 2053);
insert into sys_role_menu values(111, 2054);
insert into sys_role_menu values(112, 2050);
insert into sys_role_menu values(112, 2052);
insert into sys_role_menu values(112, 2055);
