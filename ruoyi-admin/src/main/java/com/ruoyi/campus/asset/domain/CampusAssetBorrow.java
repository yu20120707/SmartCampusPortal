package com.ruoyi.campus.asset.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 校园资产借用申请
 */
public class CampusAssetBorrow extends BaseEntity
{
    private Long borrowId;

    private Long assetId;

    private String assetNo;

    private String assetName;

    private Long applicantUserId;

    private String applicantName;

    private String purpose;

    private String status;

    private Long approverUserId;

    private String approverName;

    private String reviewComment;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    public Long getBorrowId()
    {
        return borrowId;
    }

    public void setBorrowId(Long borrowId)
    {
        this.borrowId = borrowId;
    }

    public Long getAssetId()
    {
        return assetId;
    }

    public void setAssetId(Long assetId)
    {
        this.assetId = assetId;
    }

    public String getAssetNo()
    {
        return assetNo;
    }

    public void setAssetNo(String assetNo)
    {
        this.assetNo = assetNo;
    }

    public String getAssetName()
    {
        return assetName;
    }

    public void setAssetName(String assetName)
    {
        this.assetName = assetName;
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

    public String getPurpose()
    {
        return purpose;
    }

    public void setPurpose(String purpose)
    {
        this.purpose = purpose;
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

    public Date getApplyTime()
    {
        return applyTime;
    }

    public void setApplyTime(Date applyTime)
    {
        this.applyTime = applyTime;
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
