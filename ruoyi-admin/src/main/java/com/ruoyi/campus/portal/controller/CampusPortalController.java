package com.ruoyi.campus.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.campus.portal.service.ICampusPortalService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 校园门户聚合接口
 */
@RestController
@RequestMapping("/campus/portal")
public class CampusPortalController extends BaseController
{
    @Autowired
    private ICampusPortalService campusPortalService;

    @PreAuthorize("@ss.hasPermi('campus:portal:view')")
    @GetMapping("/current")
    public AjaxResult current()
    {
        return success(campusPortalService.selectCurrentPortal(getUserId(), getUsername()));
    }
}
