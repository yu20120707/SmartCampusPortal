package com.ruoyi.campus.office.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.campus.office.domain.CampusApplication;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 校园申请 Mapper
 */
public interface CampusApplicationMapper
{
    CampusApplication selectApplicationById(@Param("applicationId") Long applicationId);

    List<CampusApplication> selectMyApplications(@Param("userId") Long userId);

    List<CampusApplication> selectTodoApplications(Long userId);

    int insertApplication(CampusApplication application);

    int submitApplication(@Param("applicationId") Long applicationId, @Param("userId") Long userId);

    int approveApplication(@Param("applicationId") Long applicationId, @Param("approverUserId") Long approverUserId,
            @Param("approverName") String approverName, @Param("reviewComment") String reviewComment);

    int rejectApplication(@Param("applicationId") Long applicationId, @Param("approverUserId") Long approverUserId,
            @Param("approverName") String approverName, @Param("reviewComment") String reviewComment);

    /**
     * 查询可选审批人（管理员、教师、领导）
     */
    List<SysUser> selectApprovers();
}
