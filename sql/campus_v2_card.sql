-- ----------------------------
-- SmartCampusPortal V2 campus card seed data
-- Apply after sql/campus_v1_menu.sql.
-- This is an internal account/ledger demo, not a real payment-channel integration.
-- ----------------------------

drop table if exists campus_card_transaction;
drop table if exists campus_card_account;

create table campus_card_account (
    account_id bigint(20) not null auto_increment comment '账户ID',
    user_id bigint(20) not null comment '用户ID',
    card_no varchar(32) not null comment '卡号',
    holder_name varchar(64) not null comment '持卡人',
    holder_type varchar(20) not null comment '持卡人类型',
    balance decimal(10,2) not null default 0.00 comment '余额',
    status char(1) not null default '0' comment '状态（0正常 1停用）',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (account_id),
    unique key uk_card_account_user (user_id),
    unique key uk_card_account_no (card_no)
) engine=innodb auto_increment=100 comment='校园一卡通账户';

create table campus_card_transaction (
    transaction_id bigint(20) not null auto_increment comment '流水ID',
    account_id bigint(20) not null comment '账户ID',
    transaction_no varchar(64) not null comment '流水号',
    transaction_type varchar(20) not null comment '交易类型',
    amount decimal(10,2) not null comment '交易金额',
    balance_after decimal(10,2) not null comment '交易后余额',
    scene_name varchar(100) not null comment '交易场景',
    status varchar(20) not null default 'success' comment '交易状态',
    transaction_time datetime not null comment '交易时间',
    create_by varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark varchar(500) default null comment '备注',
    primary key (transaction_id),
    unique key uk_card_transaction_no (transaction_no),
    key idx_card_transaction_account (account_id, transaction_time)
) engine=innodb auto_increment=100 comment='校园一卡通交易流水';

insert into campus_card_account values(100, 201, 'CAMPUS2010001', '学生用户', 'student', 86.50, '0', 'admin', sysdate(), '', null, '学生演示一卡通');
insert into campus_card_account values(101, 202, 'CAMPUS2020001', '教师用户', 'teacher', 126.00, '0', 'admin', sysdate(), '', null, '教师演示一卡通');

insert into campus_card_transaction values(100, 100, 'CARD202606170001', 'recharge', 100.00, 100.00, '线上充值', 'success', date_sub(sysdate(), interval 2 day), 'admin', sysdate(), '', null, '');
insert into campus_card_transaction values(101, 100, 'CARD202606170002', 'consume', 13.50, 86.50, '第一食堂午餐', 'success', date_sub(sysdate(), interval 1 day), 'admin', sysdate(), '', null, '');
insert into campus_card_transaction values(102, 101, 'CARD202606170003', 'recharge', 150.00, 150.00, '线上充值', 'success', date_sub(sysdate(), interval 3 day), 'admin', sysdate(), '', null, '');
insert into campus_card_transaction values(103, 101, 'CARD202606170004', 'consume', 24.00, 126.00, '校园超市', 'success', date_sub(sysdate(), interval 1 day), 'admin', sysdate(), '', null, '');
insert into campus_card_transaction values(104, 100, 'CARD202606170005', 'consume', 6.00, 80.50, '图书馆打印', 'success', date_sub(sysdate(), interval 20 hour), 'admin', sysdate(), '', null, '');
insert into campus_card_transaction values(105, 100, 'CARD202606170006', 'consume', 4.50, 76.00, '校园公交', 'success', date_sub(sysdate(), interval 10 hour), 'admin', sysdate(), '', null, '');
insert into campus_card_transaction values(106, 100, 'CARD202606170007', 'recharge', 20.00, 96.00, '线上充值', 'success', date_sub(sysdate(), interval 3 hour), 'admin', sysdate(), '', null, '');
insert into campus_card_transaction values(107, 100, 'CARD202606170008', 'consume', 9.50, 86.50, '第二食堂晚餐', 'success', date_sub(sysdate(), interval 1 hour), 'admin', sysdate(), '', null, '');
insert into campus_card_transaction values(108, 101, 'CARD202606170009', 'consume', 18.00, 108.00, '教师餐厅午餐', 'success', date_sub(sysdate(), interval 6 hour), 'admin', sysdate(), '', null, '');
insert into campus_card_transaction values(109, 101, 'CARD202606170010', 'refund', 18.00, 126.00, '教师餐厅退款', 'success', date_sub(sysdate(), interval 5 hour), 'admin', sysdate(), '', null, '');

insert into sys_menu values(2030, '一卡通', 2000, 6, 'card', 'campus/card/index', '', '', 1, 0, 'C', '0', '0', 'campus:card:view', 'money', 'admin', sysdate(), '', null, '校园一卡通');
insert into sys_menu values(2031, '一卡通查看', 2030, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:card:view', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2032, '一卡通充值', 2030, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:card:recharge', '#', 'admin', sysdate(), '', null, '');

insert into sys_role_menu values(110, 2030);
insert into sys_role_menu values(110, 2031);
insert into sys_role_menu values(110, 2032);
insert into sys_role_menu values(111, 2030);
insert into sys_role_menu values(111, 2031);
insert into sys_role_menu values(111, 2032);
