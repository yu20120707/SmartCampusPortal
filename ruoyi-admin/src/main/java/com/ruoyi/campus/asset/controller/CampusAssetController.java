package com.ruoyi.campus.asset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.campus.asset.domain.CampusAssetBorrow;
import com.ruoyi.campus.asset.service.ICampusAssetService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 校园资产接口
 */
@RestController
@RequestMapping("/campus/asset")
public class CampusAssetController extends BaseController
{
    @Autowired
    private ICampusAssetService campusAssetService;

    @PreAuthorize("@ss.hasPermi('campus:asset:view')")
    @GetMapping("/assets/available")
    public AjaxResult availableAssets()
    {
        return success(campusAssetService.selectAvailableAssets());
    }

    @PreAuthorize("@ss.hasPermi('campus:asset:borrow')")
    @GetMapping("/borrows/my")
    public AjaxResult myBorrows()
    {
        return success(campusAssetService.selectMyBorrows(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:asset:borrow')")
    @PostMapping("/borrows")
    public AjaxResult applyBorrow(@RequestBody CampusAssetBorrow borrow)
    {
        return toAjax(campusAssetService.applyBorrow(borrow, getUserId(), getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('campus:asset:approve')")
    @GetMapping("/borrows/todo")
    public AjaxResult todoBorrows()
    {
        return success(campusAssetService.selectTodoBorrows());
    }

    @PreAuthorize("@ss.hasPermi('campus:asset:approve')")
    @PutMapping("/borrows/{borrowId}/approve")
    public AjaxResult approve(@PathVariable Long borrowId, @RequestBody CampusAssetBorrow borrow)
    {
        String reviewComment = borrow == null ? null : borrow.getReviewComment();
        return toAjax(campusAssetService.approveBorrow(borrowId, getUserId(), getUsername(), reviewComment));
    }

    @PreAuthorize("@ss.hasPermi('campus:asset:approve')")
    @PutMapping("/borrows/{borrowId}/reject")
    public AjaxResult reject(@PathVariable Long borrowId, @RequestBody CampusAssetBorrow borrow)
    {
        String reviewComment = borrow == null ? null : borrow.getReviewComment();
        return toAjax(campusAssetService.rejectBorrow(borrowId, getUserId(), getUsername(), reviewComment));
    }
}
