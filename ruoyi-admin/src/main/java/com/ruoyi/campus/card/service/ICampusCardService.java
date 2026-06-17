package com.ruoyi.campus.card.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.campus.card.domain.CampusCardAccount;
import com.ruoyi.campus.card.domain.CampusCardTransaction;

/**
 * 校园一卡通服务
 */
public interface ICampusCardService
{
    CampusCardAccount selectCurrentAccount(Long userId);

    List<CampusCardTransaction> selectMyTransactions(Long userId);

    int recharge(Long userId, String username, BigDecimal amount);
}
