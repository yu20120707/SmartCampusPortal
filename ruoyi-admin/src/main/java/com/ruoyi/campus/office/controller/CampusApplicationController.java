package com.ruoyi.campus.office.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.campus.office.domain.CampusApplication;
import com.ruoyi.campus.office.service.ICampusApplicationService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 校园申请接口
 */
@RestController
@RequestMapping("/campus/office/applications")
public class CampusApplicationController extends BaseController
{
    @Autowired
    private ICampusApplicationService campusApplicationService;

    @PreAuthorize("@ss.hasPermi('campus:office:apply')")
    @GetMapping("/my")
    public AjaxResult myApplications()
    {
        return success(campusApplicationService.selectMyApplications(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:office:apply')")
    @PostMapping
    public AjaxResult add(@RequestBody CampusApplication application)
    {
        return toAjax(campusApplicationService.insertApplication(application));
    }

    @PreAuthorize("@ss.hasPermi('campus:office:apply')")
    @PutMapping("/{applicationId}/submit")
    public AjaxResult submit(@PathVariable Long applicationId)
    {
        return toAjax(campusApplicationService.submitApplication(applicationId, getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:office:approve')")
    @GetMapping("/todo")
    public AjaxResult todoApplications()
    {
        return success(campusApplicationService.selectTodoApplications());
    }

    @PreAuthorize("@ss.hasPermi('campus:office:approve')")
    @PutMapping("/{applicationId}/approve")
    public AjaxResult approve(@PathVariable Long applicationId, @RequestBody CampusApplication application)
    {
        return toAjax(campusApplicationService.approveApplication(applicationId, getUserId(), getUsername(),
                application.getReviewComment()));
    }

    @PreAuthorize("@ss.hasPermi('campus:office:approve')")
    @PutMapping("/{applicationId}/reject")
    public AjaxResult reject(@PathVariable Long applicationId, @RequestBody CampusApplication application)
    {
        return toAjax(campusApplicationService.rejectApplication(applicationId, getUserId(), getUsername(),
                application.getReviewComment()));
    }

    @PreAuthorize("@ss.hasPermi('campus:office:apply')")
    @GetMapping("/leaders")
    public AjaxResult leaders()
    {
        return success(campusApplicationService.selectLeaders());
    }

}
