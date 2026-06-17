package com.ruoyi.campus.payment.service;

import java.util.List;
import com.ruoyi.campus.payment.domain.CampusPaymentItem;
import com.ruoyi.campus.payment.domain.CampusPaymentRecord;

/**
 * 校园缴费服务
 */
public interface ICampusPaymentService
{
    List<CampusPaymentItem> selectMyPendingItems(Long userId);

    List<CampusPaymentRecord> selectMyRecords(Long userId);

    int demoPay(Long itemId, Long userId, String username);
}
