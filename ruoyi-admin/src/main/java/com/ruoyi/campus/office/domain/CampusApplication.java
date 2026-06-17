package com.ruoyi.campus.office.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 校园申请对象 campus_application
 */
public class CampusApplication extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long applicationId;

    private String applicationNo;

    private String applicationType;

    private String title;

    private String content;

    private Long applicantUserId;

    private String applicantName;

    private String applicantRole;

    private String status;

    private Long approverUserId;

    private String approverName;

    private String reviewComment;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    public Long getApplicationId()
    {
        return applicationId;
    }

    public void setApplicationId(Long applicationId)
    {
        this.applicationId = applicationId;
    }

    public String getApplicationNo()
    {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo)
    {
        this.applicationNo = applicationNo;
    }

    public String getApplicationType()
    {
        return applicationType;
    }

    public void setApplicationType(String applicationType)
    {
        this.applicationType = applicationType;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Long getApplicantUserId()
    {
        return applicantUserId;
    }

    public void setApplicantUserId(Long applicantUserId)
    {
        this.applicantUserId = applicantUserId;
    }

    public String getApplicantName()
    {
        return applicantName;
    }

    public void setApplicantName(String applicantName)
    {
        this.applicantName = applicantName;
    }

    public String getApplicantRole()
    {
        return applicantRole;
    }

    public void setApplicantRole(String applicantRole)
    {
        this.applicantRole = applicantRole;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Long getApproverUserId()
    {
        return approverUserId;
    }

    public void setApproverUserId(Long approverUserId)
    {
        this.approverUserId = approverUserId;
    }

    public String getApproverName()
    {
        return approverName;
    }

    public void setApproverName(String approverName)
    {
        this.approverName = approverName;
    }

    public String getReviewComment()
    {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment)
    {
        this.reviewComment = reviewComment;
    }

    public Date getSubmitTime()
    {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime)
    {
        this.submitTime = submitTime;
    }

    public Date getReviewTime()
    {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime)
    {
        this.reviewTime = reviewTime;
    }
}
