package com.ruoyi.campus.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.campus.academic.domain.CampusStudent;
import com.ruoyi.campus.academic.domain.CampusTeacher;
import com.ruoyi.campus.academic.service.ICampusAcademicService;
import com.ruoyi.campus.asset.domain.CampusAssetBorrow;
import com.ruoyi.campus.asset.service.ICampusAssetService;
import com.ruoyi.campus.card.domain.CampusCardTransaction;
import com.ruoyi.campus.card.service.ICampusCardService;
import com.ruoyi.campus.dashboard.service.ICampusDashboardService;
import com.ruoyi.campus.office.domain.CampusApplication;
import com.ruoyi.campus.office.service.ICampusApplicationService;
import com.ruoyi.campus.payment.service.ICampusPaymentService;
import com.ruoyi.campus.portal.service.ICampusPortalService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 校园门户聚合服务实现
 */
@Service
public class CampusPortalServiceImpl implements ICampusPortalService
{
    @Autowired
    private ICampusAcademicService campusAcademicService;

    @Autowired
    private ICampusDashboardService campusDashboardService;

    @Autowired
    private ICampusApplicationService campusApplicationService;

    @Autowired
    private ICampusAssetService campusAssetService;

    @Autowired
    private ICampusCardService campusCardService;

    @Autowired
    private ICampusPaymentService campusPaymentService;

    @Autowired
    private ISysNoticeService noticeService;

    @Override
    public Map<String, Object> selectCurrentPortal(Long userId, String username)
    {
        Map<String, Object> portal = new HashMap<>();
        String roleType = resolveRoleType(userId);
        portal.put("roleType", roleType);
        portal.put("username", username);
        portal.put("shortcuts", buildShortcuts(roleType));
        portal.put("announcements", selectNotices("2", 5));
        portal.put("news", selectNotices("1", 5));
        portal.put("todoItems", buildTodoItems(roleType, userId));
        portal.put("recentTransactions", selectRecentConsumption(userId, 5));
        portal.put("pendingPayments", limitList(campusPaymentService.selectMyPendingItems(userId), 5));

        if ("student".equals(roleType))
        {
            CampusStudent student = campusAcademicService.selectCurrentStudent(userId);
            portal.put("profile", student);
            portal.put("cards", buildStudentCards(userId));
            portal.put("todaySchedule", campusAcademicService.selectMySchedule(userId));
            portal.put("upcomingExams", campusAcademicService.selectMyExams(userId));
        }
        else if ("teacher".equals(roleType))
        {
            CampusTeacher teacher = campusAcademicService.selectCurrentTeacher(userId);
            portal.put("profile", teacher);
            portal.put("cards", buildTeacherCards(userId));
            portal.put("todaySchedule", campusAcademicService.selectMyTeachingSchedule(userId));
            portal.put("upcomingExams", campusAcademicService.selectMyTeachingExams(userId));
        }
        else if ("leader".equals(roleType))
        {
            portal.put("cards", campusDashboardService.selectLeaderDashboard().get("cards"));
            portal.put("dashboard", campusDashboardService.selectLeaderDashboard());
        }
        else
        {
            portal.put("cards", buildAdminCards());
            portal.put("dashboard", campusDashboardService.selectLeaderDashboard());
        }
        return portal;
    }

    private List<Map<String, Object>> selectNotices(String noticeType, int limit)
    {
        SysNotice query = new SysNotice();
        query.setNoticeType(noticeType);
        List<SysNotice> notices = noticeService.selectNoticeList(query);
        List<Map<String, Object>> results = new ArrayList<>();
        for (SysNotice notice : notices)
        {
            if (!"0".equals(notice.getStatus()))
            {
                continue;
            }
            Map<String, Object> item = new HashMap<>();
            item.put("noticeId", notice.getNoticeId());
            item.put("title", notice.getNoticeTitle());
            item.put("type", notice.getNoticeType());
            item.put("createTime", notice.getCreateTime());
            item.put("remark", notice.getRemark());
            results.add(item);
            if (results.size() >= limit)
            {
                break;
            }
        }
        return results;
    }

    private List<Map<String, Object>> buildTodoItems(String roleType, Long userId)
    {
        if ("leader".equals(roleType) || "admin".equals(roleType))
        {
            return buildLeaderTodoItems();
        }
        return buildPersonalTodoItems(userId);
    }

    private List<Map<String, Object>> buildLeaderTodoItems()
    {
        List<Map<String, Object>> todoItems = new ArrayList<>();
        for (CampusApplication application : limitList(campusApplicationService.selectTodoApplications(), 4))
        {
            todoItems.add(todo("OA审批", application.getTitle(), application.getApplicantName(), application.getSubmitTime(),
                    "/campus/office/todo"));
        }
        for (CampusAssetBorrow borrow : limitList(campusAssetService.selectTodoBorrows(), 4))
        {
            todoItems.add(todo("资产审批", borrow.getAssetName(), borrow.getApplicantName(), borrow.getApplyTime(),
                    "/campus/asset/todo"));
        }
        return limitList(todoItems, 6);
    }

    private List<CampusCardTransaction> selectRecentConsumption(Long userId, int limit)
    {
        List<CampusCardTransaction> transactions = new ArrayList<>();
        for (CampusCardTransaction transaction : campusCardService.selectMyTransactions(userId))
        {
            if ("consume".equals(transaction.getTransactionType()))
            {
                transactions.add(transaction);
            }
            if (transactions.size() >= limit)
            {
                break;
            }
        }
        return transactions;
    }

    private List<Map<String, Object>> buildPersonalTodoItems(Long userId)
    {
        List<Map<String, Object>> todoItems = new ArrayList<>();
        for (CampusApplication application : campusApplicationService.selectMyApplications(userId))
        {
            if ("1".equals(application.getStatus()))
            {
                todoItems.add(todo("我的申请", application.getTitle(), "待审批", application.getSubmitTime(),
                        "/campus/office/my"));
            }
        }
        for (CampusAssetBorrow borrow : campusAssetService.selectMyBorrows(userId))
        {
            if ("1".equals(borrow.getStatus()))
            {
                todoItems.add(todo("资产借用", borrow.getAssetName(), "待审批", borrow.getApplyTime(),
                        "/campus/asset/index"));
            }
        }
        return limitList(todoItems, 6);
    }

    private String resolveRoleType(Long userId)
    {
        if (SecurityUtils.hasRole("student") || campusAcademicService.selectCurrentStudent(userId) != null)
        {
            return "student";
        }
        if (SecurityUtils.hasRole("teacher") || campusAcademicService.selectCurrentTeacher(userId) != null)
        {
            return "teacher";
        }
        if (SecurityUtils.hasRole("leader"))
        {
            return "leader";
        }
        return "admin";
    }

    private List<Map<String, Object>> buildStudentCards(Long userId)
    {
        List<Map<String, Object>> cards = new ArrayList<>();
        cards.add(card("我的课程", campusAcademicService.selectMyCourses(userId).size(), "门"));
        cards.add(card("课程表", campusAcademicService.selectMySchedule(userId).size(), "节"));
        cards.add(card("成绩记录", campusAcademicService.selectMyScores(userId).size(), "条"));
        cards.add(card("考试安排", campusAcademicService.selectMyExams(userId).size(), "场"));
        return cards;
    }

    private List<Map<String, Object>> buildTeacherCards(Long userId)
    {
        List<Map<String, Object>> cards = new ArrayList<>();
        cards.add(card("任课课程", campusAcademicService.selectMyTeachingCourses(userId).size(), "门"));
        cards.add(card("教学安排", campusAcademicService.selectMyTeachingSchedule(userId).size(), "节"));
        cards.add(card("考试安排", campusAcademicService.selectMyTeachingExams(userId).size(), "场"));
        return cards;
    }

    private List<Map<String, Object>> buildAdminCards()
    {
        List<Map<String, Object>> cards = new ArrayList<>();
        cards.add(card("门户配置", 4, "项"));
        cards.add(card("V1 模块", 3, "个"));
        return cards;
    }

    private List<Map<String, Object>> buildShortcuts(String roleType)
    {
        List<Map<String, Object>> shortcuts = new ArrayList<>();
        shortcuts.add(shortcut("门户首页", "/campus/portal"));
        if ("student".equals(roleType))
        {
            shortcuts.add(shortcut("我的教务", "/campus/academic/student"));
            shortcuts.add(shortcut("我的申请", "/campus/office/my"));
            shortcuts.add(shortcut("一卡通", "/campus/card"));
            shortcuts.add(shortcut("缴费中心", "/campus/payment"));
            shortcuts.add(shortcut("资产借用", "/campus/asset/index"));
            shortcuts.add(shortcut("我的学工", "/campus/student/my"));
        }
        if ("teacher".equals(roleType))
        {
            shortcuts.add(shortcut("任课视图", "/campus/academic/teacher"));
            shortcuts.add(shortcut("我的申请", "/campus/office/my"));
            shortcuts.add(shortcut("一卡通", "/campus/card"));
            shortcuts.add(shortcut("资产借用", "/campus/asset/index"));
        }
        if ("leader".equals(roleType) || "admin".equals(roleType))
        {
            shortcuts.add(shortcut("领导驾驶舱", "/campus/dashboard"));
            shortcuts.add(shortcut("审批待办", "/campus/office/todo"));
            shortcuts.add(shortcut("资产审批", "/campus/asset/todo"));
            shortcuts.add(shortcut("学工概览", "/campus/student/overview"));
        }
        return shortcuts;
    }

    private Map<String, Object> card(String title, Object value, String unit)
    {
        Map<String, Object> card = new HashMap<>();
        card.put("title", title);
        card.put("value", value);
        card.put("unit", unit);
        return card;
    }

    private Map<String, Object> shortcut(String title, String path)
    {
        Map<String, Object> shortcut = new HashMap<>();
        shortcut.put("title", title);
        shortcut.put("path", path);
        return shortcut;
    }

    private Map<String, Object> todo(String type, String title, String subtitle, Object time, String path)
    {
        Map<String, Object> todo = new HashMap<>();
        todo.put("type", type);
        todo.put("title", title);
        todo.put("subtitle", subtitle);
        todo.put("time", time);
        todo.put("path", path);
        return todo;
    }

    private <T> List<T> limitList(List<T> source, int limit)
    {
        if (source == null || source.size() <= limit)
        {
            return source == null ? new ArrayList<>() : source;
        }
        return new ArrayList<>(source.subList(0, limit));
    }
}
