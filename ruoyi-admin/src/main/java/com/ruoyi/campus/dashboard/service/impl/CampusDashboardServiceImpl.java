package com.ruoyi.campus.dashboard.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.campus.dashboard.mapper.CampusDashboardMapper;
import com.ruoyi.campus.dashboard.service.ICampusDashboardService;

/**
 * 校园领导驾驶舱服务实现
 */
@Service
public class CampusDashboardServiceImpl implements ICampusDashboardService
{
    @Autowired
    private CampusDashboardMapper campusDashboardMapper;

    @Override
    public Map<String, Object> selectLeaderDashboard()
    {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("cards", campusDashboardMapper.selectDashboardCards());
        dashboard.put("courseTypeStats", campusDashboardMapper.selectCourseTypeStats());
        dashboard.put("collegeStudentStats", campusDashboardMapper.selectCollegeStudentStats());
        dashboard.put("scoreTrend", campusDashboardMapper.selectScoreTrend());
        return dashboard;
    }
}
