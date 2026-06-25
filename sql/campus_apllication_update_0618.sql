drop table if exists campus_application;
-- auto-generated definition
create table campus_application
(
    application_id    bigint auto_increment comment '申请ID'
        primary key,
    application_no    varchar(40)              not null comment '申请编号',
    application_type  varchar(30)              not null comment '申请类型',
    title             varchar(100)             not null comment '申请标题',
    content           varchar(1000)            not null comment '申请说明',
    applicant_user_id bigint                   not null comment '申请人用户ID',
    applicant_name    varchar(50)              not null comment '申请人名称',
    applicant_role    varchar(30)  default ''  null comment '申请人角色',
    status            char         default '1' null comment '状态（0草稿 1待审批 2已通过 3已驳回）',
    approver_user_id   bigint                   null comment '审批人用户ID',
    approver_name      varchar(50)  default ''  null comment '审批人名称',
    review_comment    varchar(500) default ''  null comment '审批意见',
    submit_time       datetime                 null comment '提交时间',
    review_time       datetime                 null comment '审批时间',
    create_by         varchar(64)  default ''  null comment '创建者',
    create_time       datetime                 null comment '创建时间',
    update_by         varchar(64)  default ''  null comment '更新者',
    update_time       datetime                 null comment '更新时间',
    remark            varchar(500)             null comment '备注',
    constraint uk_campus_application_no
        unique (application_no)
)
    comment '校园申请表';

create index idx_campus_application_applicant
    on campus_application (applicant_user_id);

create index idx_campus_application_status
    on campus_application (status)