package com.ruoyi.campus.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.campus.dashboard.service.ICampusDashboardService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 校园领导驾驶舱接口
 */
@RestController
@RequestMapping("/campus/dashboard")
public class CampusDashboardController extends BaseController
{
    @Autowired
    private ICampusDashboardService campusDashboardService;

    @PreAuthorize("@ss.hasPermi('campus:dashboard:view')")
    @GetMapping("/leader")
    public AjaxResult leader()
    {
        return success(campusDashboardService.selectLeaderDashboard());
    }
}
