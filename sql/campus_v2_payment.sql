-- ----------------------------
-- SmartCampusPortal V2 payment center seed data
-- Apply after sql/campus_v1_menu.sql.
-- This is an internal demo payment flow, not a real payment-channel integration.
-- ----------------------------

drop table if exists campus_payment_record;
drop table if exists campus_payment_item;

create table campus_payment_item (
    item_id bigint(20) not null auto_increment comment '缴费项目ID',
    user_id bigint(20) not null comment '用户ID',
    item_no varchar(64) not null comment '项目编号',
    item_name varchar(100) not null comment '项目名称',
    item_type varchar(20) not null comment '项目类型',
    amount decimal(10,2) not null comment '应缴金额',
    status varchar(20) not null default 'unpaid' comment '状态',
    due_date date default null comment '截止日期',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (item_id),
    unique key uk_payment_item_no (item_no),
    key idx_payment_item_user_status (user_id, status)
) engine=innodb auto_increment=100 comment='校园缴费项目';

create table campus_payment_record (
    record_id bigint(20) not null auto_increment comment '缴费记录ID',
    item_id bigint(20) not null comment '缴费项目ID',
    user_id bigint(20) not null comment '用户ID',
    payment_no varchar(64) not null comment '支付流水号',
    item_name varchar(100) not null comment '项目名称',
    item_type varchar(20) not null comment '项目类型',
    amount decimal(10,2) not null comment '支付金额',
    channel varchar(20) not null comment '支付渠道',
    status varchar(20) not null comment '状态',
    paid_time datetime not null comment '支付时间',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (record_id),
    unique key uk_payment_no (payment_no),
    key idx_payment_record_user_time (user_id, paid_time)
) engine=innodb auto_increment=100 comment='校园缴费记录';

insert into campus_payment_item values(100, 201, 'PAYITEM202606170001', '2026 学年住宿费', 'dorm', 1200.00, 'unpaid', date_add(curdate(), interval 20 day), 'admin', sysdate(), '', null, '学生演示待缴');
insert into campus_payment_item values(101, 201, 'PAYITEM202606170002', '英语四级报名费', 'exam', 30.00, 'unpaid', date_add(curdate(), interval 10 day), 'admin', sysdate(), '', null, '学生演示待缴');
insert into campus_payment_item values(102, 201, 'PAYITEM202606170003', '2025 秋季教材费', 'other', 268.50, 'paid', date_sub(curdate(), interval 30 day), 'admin', sysdate(), 'admin', sysdate(), '学生演示已缴');

insert into campus_payment_record values(100, 102, 201, 'PAY202606170001', '2025 秋季教材费', 'other', 268.50, 'demo', 'paid', date_sub(sysdate(), interval 7 day), 'admin', sysdate(), '', null, '');

insert into sys_menu values(2040, '缴费中心', 2000, 7, 'payment', 'campus/payment/index', '', '', 1, 0, 'C', '0', '0', 'campus:payment:view', 'money', 'admin', sysdate(), '', null, '校园缴费中心');
insert into sys_menu values(2041, '缴费查看', 2040, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:payment:view', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2042, '演示支付', 2040, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:payment:pay', '#', 'admin', sysdate(), '', null, '');

insert into sys_role_menu values(110, 2040);
insert into sys_role_menu values(110, 2041);
insert into sys_role_menu values(110, 2042);
