-- ----------------------------
-- SmartCampusPortal V1 menu, role, and demo user seed data
-- Apply after sql/ry_20260417.sql and sql/campus_v1_init.sql.
-- Demo password hash is reused for all local campus demo accounts.
-- ----------------------------

insert into sys_role values(110, '学生', 'student', 10, 5, 1, 1, '0', '0', 'admin', sysdate(), '', null, '校园学生角色');
insert into sys_role values(111, '教师', 'teacher', 11, 5, 1, 1, '0', '0', 'admin', sysdate(), '', null, '校园教师角色');
insert into sys_role values(112, '领导', 'leader', 12, 1, 1, 1, '0', '0', 'admin', sysdate(), '', null, '校园领导角色');

insert into sys_user values(201, 103, 'student', '学生用户', '00', 'student@example.com', '13800000001', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '校园学生演示账号');
insert into sys_user values(202, 103, 'teacher', '教师用户', '00', 'teacher@example.com', '13800000002', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '校园教师演示账号');
insert into sys_user values(203, 103, 'leader', '领导用户', '00', 'leader@example.com', '13800000003', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '校园领导演示账号');

insert into sys_user_role values(201, 110);
insert into sys_user_role values(202, 111);
insert into sys_user_role values(203, 112);

insert into sys_menu values(2000, '智慧校园', 0, 5, 'campus', null, '', '', 1, 0, 'M', '0', '0', '', 'education', 'admin', sysdate(), '', null, '智慧校园目录');
insert into sys_menu values(2001, '校园门户', 2000, 1, 'portal', 'campus/portal/index', '', '', 1, 0, 'C', '0', '0', 'campus:portal:view', 'dashboard', 'admin', sysdate(), '', null, '校园门户首页');
insert into sys_menu values(2002, '我的教务', 2000, 2, 'academic/student', 'campus/academic/student', '', '', 1, 0, 'C', '0', '0', 'campus:academic:view', 'education', 'admin', sysdate(), '', null, '学生教务视图');
insert into sys_menu values(2003, '任课视图', 2000, 3, 'academic/teacher', 'campus/academic/teacher', '', '', 1, 0, 'C', '0', '0', 'campus:teacher:view', 'people', 'admin', sysdate(), '', null, '教师任课视图');
insert into sys_menu values(2004, '领导驾驶舱', 2000, 4, 'dashboard', 'campus/dashboard/index', '', '', 1, 0, 'C', '0', '0', 'campus:dashboard:view', 'chart', 'admin', sysdate(), '', null, '领导驾驶舱');

insert into sys_menu values(2010, '门户查看', 2001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:portal:view', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2011, '教务查看', 2002, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:academic:view', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2012, '任课查看', 2003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:teacher:view', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(2013, '驾驶舱查看', 2004, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'campus:dashboard:view', '#', 'admin', sysdate(), '', null, '');

insert into sys_role_menu values(110, 2000);
insert into sys_role_menu values(110, 2001);
insert into sys_role_menu values(110, 2002);
insert into sys_role_menu values(110, 2010);
insert into sys_role_menu values(110, 2011);

insert into sys_role_menu values(111, 2000);
insert into sys_role_menu values(111, 2001);
insert into sys_role_menu values(111, 2003);
insert into sys_role_menu values(111, 2010);
insert into sys_role_menu values(111, 2012);

insert into sys_role_menu values(112, 2000);
insert into sys_role_menu values(112, 2001);
insert into sys_role_menu values(112, 2004);
insert into sys_role_menu values(112, 2010);
insert into sys_role_menu values(112, 2013);
