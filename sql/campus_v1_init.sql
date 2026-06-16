-- ----------------------------
-- SmartCampusPortal V1 campus business schema and seed data
-- Apply after sql/ry_20260417.sql.
-- ----------------------------

drop table if exists campus_exam;
drop table if exists campus_score;
drop table if exists campus_student_course;
drop table if exists campus_course_section;
drop table if exists campus_course;
drop table if exists campus_term;
drop table if exists campus_teacher;
drop table if exists campus_student;

create table campus_student (
  student_id    bigint(20)   not null auto_increment comment '学生ID',
  user_id       bigint(20)   default null comment '关联系统用户ID',
  student_no    varchar(32)  not null comment '学号',
  student_name  varchar(50)  not null comment '学生姓名',
  college_name  varchar(100) default '' comment '学院',
  major_name    varchar(100) default '' comment '专业',
  class_name    varchar(100) default '' comment '班级',
  grade         varchar(20)  default '' comment '年级',
  status        char(1)      default '0' comment '状态（0正常 1停用）',
  create_by     varchar(64)  default '' comment '创建者',
  create_time   datetime comment '创建时间',
  update_by     varchar(64)  default '' comment '更新者',
  update_time   datetime comment '更新时间',
  remark        varchar(500) default null comment '备注',
  primary key (student_id),
  unique key uk_campus_student_no (student_no),
  key idx_campus_student_user (user_id)
) engine=innodb auto_increment=100 comment='校园学生表';

create table campus_teacher (
  teacher_id    bigint(20)   not null auto_increment comment '教师ID',
  user_id       bigint(20)   default null comment '关联系统用户ID',
  teacher_no    varchar(32)  not null comment '工号',
  teacher_name  varchar(50)  not null comment '教师姓名',
  college_name  varchar(100) default '' comment '学院',
  title         varchar(50)  default '' comment '职称',
  status        char(1)      default '0' comment '状态（0正常 1停用）',
  create_by     varchar(64)  default '' comment '创建者',
  create_time   datetime comment '创建时间',
  update_by     varchar(64)  default '' comment '更新者',
  update_time   datetime comment '更新时间',
  remark        varchar(500) default null comment '备注',
  primary key (teacher_id),
  unique key uk_campus_teacher_no (teacher_no),
  key idx_campus_teacher_user (user_id)
) engine=innodb auto_increment=100 comment='校园教师表';

create table campus_term (
  term_id       bigint(20)   not null auto_increment comment '学期ID',
  term_name     varchar(50)  not null comment '学期名称',
  start_date    date         not null comment '开始日期',
  end_date      date         not null comment '结束日期',
  current_flag  char(1)      default 'N' comment '是否当前学期（Y是 N否）',
  primary key (term_id)
) engine=innodb auto_increment=100 comment='校园学期表';

create table campus_course (
  course_id    bigint(20)   not null auto_increment comment '课程ID',
  course_code  varchar(32)  not null comment '课程编码',
  course_name  varchar(100) not null comment '课程名称',
  credit       decimal(4,1) default 0.0 comment '学分',
  course_type  varchar(50)  default '' comment '课程类型',
  dept_name    varchar(100) default '' comment '开课单位',
  status       char(1)      default '0' comment '状态（0正常 1停用）',
  primary key (course_id),
  unique key uk_campus_course_code (course_code)
) engine=innodb auto_increment=100 comment='校园课程表';

create table campus_course_section (
  section_id     bigint(20)  not null auto_increment comment '教学班ID',
  course_id      bigint(20)  not null comment '课程ID',
  teacher_id     bigint(20)  not null comment '教师ID',
  term_id        bigint(20)  not null comment '学期ID',
  weekday        int(1)      not null comment '星期（1-7）',
  start_section  int(2)      not null comment '开始节次',
  end_section    int(2)      not null comment '结束节次',
  classroom      varchar(50) default '' comment '教室',
  week_range     varchar(50) default '' comment '周次',
  primary key (section_id),
  key idx_campus_section_course (course_id),
  key idx_campus_section_teacher (teacher_id),
  key idx_campus_section_term (term_id)
) engine=innodb auto_increment=100 comment='校园教学班表';

create table campus_student_course (
  student_id  bigint(20) not null comment '学生ID',
  section_id  bigint(20) not null comment '教学班ID',
  status      char(1)    default '0' comment '状态（0正常 1退课）',
  primary key (student_id, section_id)
) engine=innodb comment='学生选课关系表';

create table campus_score (
  score_id     bigint(20)   not null auto_increment comment '成绩ID',
  student_id   bigint(20)   not null comment '学生ID',
  section_id   bigint(20)   not null comment '教学班ID',
  score        decimal(5,2) default null comment '成绩',
  grade_point  decimal(3,2) default null comment '绩点',
  exam_type    varchar(30)  default '期末' comment '考试类型',
  primary key (score_id),
  key idx_campus_score_student (student_id),
  key idx_campus_score_section (section_id)
) engine=innodb auto_increment=100 comment='校园成绩表';

create table campus_exam (
  exam_id     bigint(20)  not null auto_increment comment '考试ID',
  section_id  bigint(20)  not null comment '教学班ID',
  exam_time   datetime    not null comment '考试时间',
  classroom   varchar(50) default '' comment '考场',
  seat_no     varchar(20) default '' comment '座位号',
  exam_type   varchar(30) default '期末' comment '考试类型',
  primary key (exam_id),
  key idx_campus_exam_section (section_id)
) engine=innodb auto_increment=100 comment='校园考试安排表';

insert into campus_student values
(100, 201, '20240001', '陈一一', '计算机学院', '软件工程', '软工2401', '2024', '0', 'admin', sysdate(), '', null, 'V1样例学生'),
(101, null, '20240002', '李二二', '计算机学院', '软件工程', '软工2401', '2024', '0', 'admin', sysdate(), '', null, 'V1样例学生'),
(102, null, '20230001', '王三三', '信息管理学院', '信息管理', '信管2301', '2023', '0', 'admin', sysdate(), '', null, 'V1样例学生');

insert into campus_teacher values
(100, 202, 'T2024001', '周老师', '计算机学院', '副教授', '0', 'admin', sysdate(), '', null, 'V1样例教师'),
(101, null, 'T2024002', '吴老师', '信息管理学院', '讲师', '0', 'admin', sysdate(), '', null, 'V1样例教师');

insert into campus_term values
(100, '2026春季学期', '2026-02-24', '2026-07-10', 'Y'),
(101, '2025秋季学期', '2025-09-01', '2026-01-16', 'N');

insert into campus_course values
(100, 'CS101', 'Java程序设计', 3.0, '专业必修', '计算机学院', '0'),
(101, 'CS205', '数据库系统', 3.5, '专业必修', '计算机学院', '0'),
(102, 'GE100', '大学英语', 2.0, '公共基础', '公共教学部', '0'),
(103, 'IM201', '信息系统分析', 3.0, '专业选修', '信息管理学院', '0');

insert into campus_course_section values
(100, 100, 100, 100, 1, 1, 2, '明德楼A201', '1-16周'),
(101, 101, 100, 100, 3, 3, 4, '明德楼B305', '1-16周'),
(102, 102, 101, 100, 5, 1, 2, '博学楼C102', '1-16周'),
(103, 103, 101, 100, 2, 5, 6, '博学楼D401', '1-12周');

insert into campus_student_course values
(100, 100, '0'),
(100, 101, '0'),
(100, 102, '0'),
(101, 100, '0'),
(101, 102, '0'),
(102, 103, '0');

insert into campus_score values
(100, 100, 100, 91.50, 4.00, '期末'),
(101, 100, 101, 86.00, 3.60, '期末'),
(102, 100, 102, 88.00, 3.70, '期末'),
(103, 101, 100, 79.00, 3.00, '期末'),
(104, 102, 103, 84.50, 3.40, '期末');

insert into campus_exam values
(100, 100, '2026-06-25 09:00:00', '明德楼A301', '12', '期末'),
(101, 101, '2026-06-28 14:00:00', '明德楼B501', '08', '期末'),
(102, 102, '2026-07-01 09:00:00', '博学楼C201', '20', '期末'),
(103, 103, '2026-06-30 10:00:00', '博学楼D501', '05', '期末');
