package com.ruoyi.campus.student.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学工档案视图
 */
public class CampusStudentAffairsProfile extends BaseEntity
{
    private Long profileId;

    private Long studentId;

    private Long userId;

    private String studentNo;

    private String studentName;

    private String collegeName;

    private String majorName;

    private String className;

    private String grade;

    private String counselorName;

    private String politicalStatus;

    private String dormitory;

    private String emergencyContact;

    private String emergencyPhone;

    private String statusSummary;

    public Long getProfileId()
    {
        return profileId;
    }

    public void setProfileId(Long profileId)
    {
        this.profileId = profileId;
    }

    public Long getStudentId()
    {
        return studentId;
    }

    public void setStudentId(Long studentId)
    {
        this.studentId = studentId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getStudentNo()
    {
        return studentNo;
    }

    public void setStudentNo(String studentNo)
    {
        this.studentNo = studentNo;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public String getCollegeName()
    {
        return collegeName;
    }

    public void setCollegeName(String collegeName)
    {
        this.collegeName = collegeName;
    }

    public String getMajorName()
    {
        return majorName;
    }

    public void setMajorName(String majorName)
    {
        this.majorName = majorName;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }

    public String getCounselorName()
    {
        return counselorName;
    }

    public void setCounselorName(String counselorName)
    {
        this.counselorName = counselorName;
    }

    public String getPoliticalStatus()
    {
        return politicalStatus;
    }

    public void setPoliticalStatus(String politicalStatus)
    {
        this.politicalStatus = politicalStatus;
    }

    public String getDormitory()
    {
        return dormitory;
    }

    public void setDormitory(String dormitory)
    {
        this.dormitory = dormitory;
    }

    public String getEmergencyContact()
    {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact)
    {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyPhone()
    {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone)
    {
        this.emergencyPhone = emergencyPhone;
    }

    public String getStatusSummary()
    {
        return statusSummary;
    }

    public void setStatusSummary(String statusSummary)
    {
        this.statusSummary = statusSummary;
    }
}
