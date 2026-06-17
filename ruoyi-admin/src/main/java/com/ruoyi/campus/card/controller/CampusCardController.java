package com.ruoyi.campus.card.controller;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.campus.card.service.ICampusCardService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 校园一卡通接口
 */
@RestController
@RequestMapping("/campus/card")
public class CampusCardController extends BaseController
{
    @Autowired
    private ICampusCardService campusCardService;

    @PreAuthorize("@ss.hasPermi('campus:card:view')")
    @GetMapping("/account")
    public AjaxResult account()
    {
        return success(campusCardService.selectCurrentAccount(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:card:view')")
    @GetMapping("/transactions/my")
    public AjaxResult myTransactions()
    {
        return success(campusCardService.selectMyTransactions(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:card:recharge')")
    @PostMapping("/recharge")
    public AjaxResult recharge(@RequestBody Map<String, BigDecimal> body)
    {
        BigDecimal amount = body == null ? null : body.get("amount");
        return toAjax(campusCardService.recharge(getUserId(), getUsername(), amount));
    }
}
