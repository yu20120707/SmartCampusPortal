package com.ruoyi.campus.asset.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.campus.asset.domain.CampusAsset;
import com.ruoyi.campus.asset.domain.CampusAssetBorrow;

/**
 * 校园资产数据访问
 */
public interface CampusAssetMapper
{
    List<CampusAsset> selectAvailableAssets();

    CampusAsset selectAvailableAssetForUpdate(@Param("assetId") Long assetId);

    CampusAssetBorrow selectPendingBorrowForUpdate(@Param("borrowId") Long borrowId);

    List<CampusAssetBorrow> selectMyBorrows(@Param("userId") Long userId);

    List<CampusAssetBorrow> selectTodoBorrows();

    int insertBorrow(CampusAssetBorrow borrow);

    int decreaseAvailableQuantity(@Param("assetId") Long assetId, @Param("updateBy") String updateBy);

    int approveBorrow(@Param("borrowId") Long borrowId, @Param("approverUserId") Long approverUserId,
            @Param("approverName") String approverName, @Param("reviewComment") String reviewComment);

    int rejectBorrow(@Param("borrowId") Long borrowId, @Param("approverUserId") Long approverUserId,
            @Param("approverName") String approverName, @Param("reviewComment") String reviewComment);
}
