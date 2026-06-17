package com.ruoyi.campus.payment.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.campus.payment.domain.CampusPaymentItem;
import com.ruoyi.campus.payment.domain.CampusPaymentRecord;
import com.ruoyi.campus.payment.mapper.CampusPaymentMapper;
import com.ruoyi.campus.payment.service.ICampusPaymentService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.uuid.IdUtils;

/**
 * 校园缴费服务实现
 */
@Service
public class CampusPaymentServiceImpl implements ICampusPaymentService
{
    @Autowired
    private CampusPaymentMapper campusPaymentMapper;

    @Override
    public List<CampusPaymentItem> selectMyPendingItems(Long userId)
    {
        return campusPaymentMapper.selectPendingItemsByUserId(userId);
    }

    @Override
    public List<CampusPaymentRecord> selectMyRecords(Long userId)
    {
        return campusPaymentMapper.selectRecordsByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int demoPay(Long itemId, Long userId, String username)
    {
        if (itemId == null)
        {
            throw new ServiceException("缴费项目不能为空");
        }

        CampusPaymentItem item = campusPaymentMapper.selectPendingItemForUpdate(itemId, userId);
        if (item == null)
        {
            throw new ServiceException("缴费项目不存在或已支付");
        }

        int rows = campusPaymentMapper.markItemPaid(itemId, userId, username);
        if (rows == 0)
        {
            throw new ServiceException("缴费项目状态更新失败");
        }

        CampusPaymentRecord record = new CampusPaymentRecord();
        record.setItemId(item.getItemId());
        record.setUserId(userId);
        record.setPaymentNo("PAY" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase());
        record.setItemName(item.getItemName());
        record.setItemType(item.getItemType());
        record.setAmount(item.getAmount());
        record.setChannel("demo");
        record.setStatus("paid");
        record.setPaidTime(new Date());
        record.setCreateBy(username);
        return campusPaymentMapper.insertRecord(record);
    }
}
