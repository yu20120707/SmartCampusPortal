package com.ruoyi.campus.card.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.campus.card.domain.CampusCardAccount;
import com.ruoyi.campus.card.domain.CampusCardTransaction;

/**
 * 校园一卡通数据访问
 */
public interface CampusCardMapper
{
    CampusCardAccount selectAccountByUserId(@Param("userId") Long userId);

    List<CampusCardTransaction> selectTransactionsByUserId(@Param("userId") Long userId);

    int updateAccountBalance(@Param("accountId") Long accountId, @Param("balance") BigDecimal balance,
            @Param("updateBy") String updateBy);

    int insertTransaction(CampusCardTransaction transaction);
}
