package com.ruoyi.campus.card.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.campus.card.domain.CampusCardAccount;
import com.ruoyi.campus.card.domain.CampusCardTransaction;
import com.ruoyi.campus.card.mapper.CampusCardMapper;
import com.ruoyi.campus.card.service.ICampusCardService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.uuid.IdUtils;

/**
 * 校园一卡通服务实现
 */
@Service
public class CampusCardServiceImpl implements ICampusCardService
{
    private static final BigDecimal MIN_RECHARGE_AMOUNT = new BigDecimal("0.01");

    private static final BigDecimal MAX_RECHARGE_AMOUNT = new BigDecimal("1000.00");

    @Autowired
    private CampusCardMapper campusCardMapper;

    @Override
    public CampusCardAccount selectCurrentAccount(Long userId)
    {
        return campusCardMapper.selectAccountByUserId(userId);
    }

    @Override
    public List<CampusCardTransaction> selectMyTransactions(Long userId)
    {
        return campusCardMapper.selectTransactionsByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int recharge(Long userId, String username, BigDecimal amount)
    {
        validateRechargeAmount(amount);
        CampusCardAccount account = campusCardMapper.selectAccountByUserId(userId);
        if (account == null)
        {
            throw new ServiceException("未找到当前用户的一卡通账户");
        }
        if (!"0".equals(account.getStatus()))
        {
            throw new ServiceException("当前一卡通账户不可充值");
        }

        BigDecimal newBalance = account.getBalance().add(amount);
        int rows = campusCardMapper.updateAccountBalance(account.getAccountId(), newBalance, username);
        if (rows == 0)
        {
            throw new ServiceException("一卡通账户余额更新失败");
        }

        CampusCardTransaction transaction = new CampusCardTransaction();
        transaction.setAccountId(account.getAccountId());
        transaction.setTransactionNo("CARD" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase());
        transaction.setTransactionType("recharge");
        transaction.setAmount(amount);
        transaction.setBalanceAfter(newBalance);
        transaction.setSceneName("门户演示充值");
        transaction.setStatus("success");
        transaction.setTransactionTime(new Date());
        transaction.setCreateBy(username);
        return campusCardMapper.insertTransaction(transaction);
    }

    private void validateRechargeAmount(BigDecimal amount)
    {
        if (amount == null)
        {
            throw new ServiceException("充值金额不能为空");
        }
        if (amount.compareTo(MIN_RECHARGE_AMOUNT) < 0)
        {
            throw new ServiceException("充值金额必须大于0");
        }
        if (amount.compareTo(MAX_RECHARGE_AMOUNT) > 0)
        {
            throw new ServiceException("单次充值金额不能超过1000元");
        }
    }
}
