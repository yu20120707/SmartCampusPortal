package com.ruoyi.campus.asset.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.campus.asset.domain.CampusAsset;
import com.ruoyi.campus.asset.domain.CampusAssetBorrow;
import com.ruoyi.campus.asset.mapper.CampusAssetMapper;
import com.ruoyi.campus.asset.service.ICampusAssetService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;

/**
 * 校园资产服务实现
 */
@Service
public class CampusAssetServiceImpl implements ICampusAssetService
{
    private static final int MAX_PURPOSE_LENGTH = 500;

    @Autowired
    private CampusAssetMapper campusAssetMapper;

    @Override
    public List<CampusAsset> selectAvailableAssets()
    {
        return campusAssetMapper.selectAvailableAssets();
    }

    @Override
    public List<CampusAssetBorrow> selectMyBorrows(Long userId)
    {
        return campusAssetMapper.selectMyBorrows(userId);
    }

    @Override
    public List<CampusAssetBorrow> selectTodoBorrows()
    {
        return campusAssetMapper.selectTodoBorrows();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int applyBorrow(CampusAssetBorrow borrow, Long userId, String username)
    {
        validateBorrow(borrow);
        CampusAsset asset = campusAssetMapper.selectAvailableAssetForUpdate(borrow.getAssetId());
        if (asset == null)
        {
            throw new ServiceException("资产不存在或暂无可借数量");
        }

        CampusAssetBorrow newBorrow = new CampusAssetBorrow();
        newBorrow.setAssetId(asset.getAssetId());
        newBorrow.setApplicantUserId(userId);
        newBorrow.setApplicantName(username);
        newBorrow.setPurpose(borrow.getPurpose());
        newBorrow.setStatus("1");
        newBorrow.setApplyTime(new Date());
        newBorrow.setCreateBy(username);
        return campusAssetMapper.insertBorrow(newBorrow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approveBorrow(Long borrowId, Long approverUserId, String approverName, String reviewComment)
    {
        validateReviewComment(reviewComment);
        CampusAssetBorrow borrow = campusAssetMapper.selectPendingBorrowForUpdate(borrowId);
        if (borrow == null)
        {
            throw new ServiceException("借用申请不存在或已处理");
        }

        int stockRows = campusAssetMapper.decreaseAvailableQuantity(borrow.getAssetId(), approverName);
        if (stockRows == 0)
        {
            throw new ServiceException("资产暂无可借数量");
        }

        int rows = campusAssetMapper.approveBorrow(borrowId, approverUserId, approverName, reviewComment);
        if (rows == 0)
        {
            throw new ServiceException("借用申请状态更新失败");
        }
        return rows;
    }

    @Override
    public int rejectBorrow(Long borrowId, Long approverUserId, String approverName, String reviewComment)
    {
        validateReviewComment(reviewComment);
        int rows = campusAssetMapper.rejectBorrow(borrowId, approverUserId, approverName, reviewComment);
        if (rows == 0)
        {
            throw new ServiceException("借用申请不存在或已处理");
        }
        return rows;
    }

    private void validateBorrow(CampusAssetBorrow borrow)
    {
        if (borrow == null || borrow.getAssetId() == null)
        {
            throw new ServiceException("借用资产不能为空");
        }
        if (StringUtils.isEmpty(borrow.getPurpose()))
        {
            throw new ServiceException("借用用途不能为空");
        }
        if (borrow.getPurpose().length() > MAX_PURPOSE_LENGTH)
        {
            throw new ServiceException("借用用途不能超过500个字符");
        }
    }

    private void validateReviewComment(String reviewComment)
    {
        if (StringUtils.isNotEmpty(reviewComment) && reviewComment.length() > 500)
        {
            throw new ServiceException("审批意见不能超过500个字符");
        }
    }
}
