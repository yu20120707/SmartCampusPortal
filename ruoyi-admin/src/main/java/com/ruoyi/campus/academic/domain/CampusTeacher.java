package com.ruoyi.campus.academic.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教师对象 campus_teacher
 */
public class CampusTeacher extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long teacherId;

    private Long userId;

    private String teacherNo;

    private String teacherName;

    private String collegeName;

    private String title;

    public Long getTeacherId()
    {
        return teacherId;
    }

    public void setTeacherId(Long teacherId)
    {
        this.teacherId = teacherId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getTeacherNo()
    {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo)
    {
        this.teacherNo = teacherNo;
    }

    public String getTeacherName()
    {
        return teacherName;
    }

    public void setTeacherName(String teacherName)
    {
        this.teacherName = teacherName;
    }

    public String getCollegeName()
    {
        return collegeName;
    }

    public void setCollegeName(String collegeName)
    {
        this.collegeName = collegeName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
