package com.ruoyi.campus.payment.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.campus.payment.domain.CampusPaymentItem;
import com.ruoyi.campus.payment.domain.CampusPaymentRecord;

/**
 * 校园缴费数据访问
 */
public interface CampusPaymentMapper
{
    List<CampusPaymentItem> selectPendingItemsByUserId(@Param("userId") Long userId);

    List<CampusPaymentRecord> selectRecordsByUserId(@Param("userId") Long userId);

    CampusPaymentItem selectPendingItemForUpdate(@Param("itemId") Long itemId, @Param("userId") Long userId);

    int markItemPaid(@Param("itemId") Long itemId, @Param("userId") Long userId, @Param("updateBy") String updateBy);

    int insertRecord(CampusPaymentRecord record);
}
