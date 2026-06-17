package com.ruoyi.campus.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.campus.payment.service.ICampusPaymentService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 校园缴费接口
 */
@RestController
@RequestMapping("/campus/payment")
public class CampusPaymentController extends BaseController
{
    @Autowired
    private ICampusPaymentService campusPaymentService;

    @PreAuthorize("@ss.hasPermi('campus:payment:view')")
    @GetMapping("/items/pending")
    public AjaxResult pendingItems()
    {
        return success(campusPaymentService.selectMyPendingItems(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:payment:view')")
    @GetMapping("/records/my")
    public AjaxResult myRecords()
    {
        return success(campusPaymentService.selectMyRecords(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:payment:pay')")
    @PostMapping("/items/{itemId}/demo-pay")
    public AjaxResult demoPay(@PathVariable Long itemId)
    {
        return toAjax(campusPaymentService.demoPay(itemId, getUserId(), getUsername()));
    }
}
