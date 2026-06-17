package com.ruoyi.campus.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.campus.student.service.ICampusStudentAffairsService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 学工信息接口
 */
@RestController
@RequestMapping("/campus/student")
public class CampusStudentAffairsController extends BaseController
{
    @Autowired
    private ICampusStudentAffairsService campusStudentAffairsService;

    @PreAuthorize("@ss.hasPermi('campus:student:view')")
    @GetMapping("/profile/my")
    public AjaxResult myProfile()
    {
        return success(campusStudentAffairsService.selectMyProfile(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:student:view')")
    @GetMapping("/records/my")
    public AjaxResult myRecords()
    {
        return success(campusStudentAffairsService.selectMyRecords(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:student:manage')")
    @GetMapping("/overview")
    public AjaxResult overview()
    {
        return success(campusStudentAffairsService.selectOverview());
    }
}
