package com.ruoyi.campus.office.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.system.api.SysUserApi;
import com.ruoyi.common.enums.StatusType;
import com.ruoyi.system.domain.vo.SysUserVO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.campus.office.domain.CampusApplication;
import com.ruoyi.campus.office.mapper.CampusApplicationMapper;
import com.ruoyi.campus.office.service.ICampusApplicationService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;

/**
 * 校园申请服务实现
 */
@Service
public class CampusApplicationServiceImpl implements ICampusApplicationService
{
    private static final int MAX_TITLE_LENGTH = 100;

    private static final int MAX_CONTENT_LENGTH = 1000;

    @Autowired
    private CampusApplicationMapper campusApplicationMapper;

    @Resource
    private SysUserApi sysUserApi;

    @Override
    public List<CampusApplication> selectMyApplications(Long userId)
    {
        return campusApplicationMapper.selectMyApplications(userId);
    }

    @Override
    public List<CampusApplication> selectTodoApplications()
    {
        return campusApplicationMapper.selectTodoApplications(SecurityUtils.getUserId());
    }

    @Override
    public int insertApplication(CampusApplication application)
    {
        validateApplication(application);
        application.setApplicationNo("APP" + IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase());
        application.setApplicantUserId(SecurityUtils.getUserId());
        application.setApplicantName(SecurityUtils.getUsername());
        application.setApplicantRole(resolveApplicantRole());
        application.setStatus(StatusType.ING.getCode());
        application.setSubmitTime(new Date());
        application.setCreateBy(SecurityUtils.getUsername());
        return campusApplicationMapper.insertApplication(application);
    }

    @Override
    public int submitApplication(Long applicationId, Long userId)
    {
        int rows = campusApplicationMapper.submitApplication(applicationId, userId);
        if (rows == 0)
        {
            throw new ServiceException("只能提交自己的草稿申请");
        }
        return rows;
    }

    @Override
    public int approveApplication(Long applicationId, Long approverUserId, String approverName, String reviewComment)
    {
        return review(applicationId, approverUserId, approverName, reviewComment, true);
    }

    @Override
    public int rejectApplication(Long applicationId, Long approverUserId, String approverName, String reviewComment)
    {
        return review(applicationId, approverUserId, approverName, reviewComment, false);
    }

    @Override
    public List<SysUserVO> selectLeaders() {
        return sysUserApi.selectLeaderList();
    }

    private int review(Long applicationId, Long approverUserId, String approverName, String reviewComment, boolean approved)
    {
        if (StringUtils.isNotEmpty(reviewComment) && reviewComment.length() > 500)
        {
            throw new ServiceException("审批意见不能超过500个字符");
        }
        int rows = approved
                ? campusApplicationMapper.approveApplication(applicationId, approverUserId, approverName, reviewComment)
                : campusApplicationMapper.rejectApplication(applicationId, approverUserId, approverName, reviewComment);
        if (rows == 0)
        {
            throw new ServiceException("申请不存在或已审批");
        }
        return rows;
    }

    private void validateApplication(CampusApplication application)
    {
        if (application == null)
        {
            throw new ServiceException("申请内容不能为空");
        }
        if (StringUtils.isEmpty(application.getApplicationType()))
        {
            throw new ServiceException("申请类型不能为空");
        }
        if (StringUtils.isEmpty(application.getTitle()))
        {
            throw new ServiceException("申请标题不能为空");
        }
        if (application.getTitle().length() > MAX_TITLE_LENGTH)
        {
            throw new ServiceException("申请标题不能超过100个字符");
        }
        if (StringUtils.isEmpty(application.getContent()))
        {
            throw new ServiceException("申请说明不能为空");
        }
        if (application.getContent().length() > MAX_CONTENT_LENGTH)
        {
            throw new ServiceException("申请说明不能超过1000个字符");
        }
    }

    private String resolveApplicantRole()
    {
        if (SecurityUtils.hasRole("student"))
        {
            return "student";
        }
        if (SecurityUtils.hasRole("teacher"))
        {
            return "teacher";
        }
        return "campus";
    }
}
