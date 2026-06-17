package com.ruoyi.campus.asset.service;

import java.util.List;
import com.ruoyi.campus.asset.domain.CampusAsset;
import com.ruoyi.campus.asset.domain.CampusAssetBorrow;

/**
 * 校园资产服务
 */
public interface ICampusAssetService
{
    List<CampusAsset> selectAvailableAssets();

    List<CampusAssetBorrow> selectMyBorrows(Long userId);

    List<CampusAssetBorrow> selectTodoBorrows();

    int applyBorrow(CampusAssetBorrow borrow, Long userId, String username);

    int approveBorrow(Long borrowId, Long approverUserId, String approverName, String reviewComment);

    int rejectBorrow(Long borrowId, Long approverUserId, String approverName, String reviewComment);
}
