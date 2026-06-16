package com.ruoyi.campus.dashboard.mapper;

import java.util.List;
import java.util.Map;

/**
 * 校园领导驾驶舱 Mapper
 */
public interface CampusDashboardMapper
{
    List<Map<String, Object>> selectDashboardCards();

    List<Map<String, Object>> selectCourseTypeStats();

    List<Map<String, Object>> selectCollegeStudentStats();

    List<Map<String, Object>> selectScoreTrend();
}
