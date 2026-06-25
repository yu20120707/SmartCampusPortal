package com.ruoyi.campus.office.service;

import java.util.List;

import com.ruoyi.campus.office.domain.CampusApplication;
import com.ruoyi.system.domain.vo.SysUserVO;

/**
 * 校园申请服务
 */
public interface ICampusApplicationService
{
    List<CampusApplication> selectMyApplications(Long userId);

    List<CampusApplication> selectTodoApplications();

    int insertApplication(CampusApplication application);

    int submitApplication(Long applicationId, Long userId);

    int approveApplication(Long applicationId, Long approverUserId, String approverName, String reviewComment);

    int rejectApplication(Long applicationId, Long approverUserId, String approverName, String reviewComment);

    List<SysUserVO> selectLeaders();
}
