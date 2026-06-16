package com.ruoyi.campus.academic.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考试安排对象 campus_exam
 */
public class CampusExam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long examId;

    private Long sectionId;

    private String courseName;

    private String teacherName;

    private String termName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date examTime;

    private String classroom;

    private String seatNo;

    private String examType;

    public Long getExamId()
    {
        return examId;
    }

    public void setExamId(Long examId)
    {
        this.examId = examId;
    }

    public Long getSectionId()
    {
        return sectionId;
    }

    public void setSectionId(Long sectionId)
    {
        this.sectionId = sectionId;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public String getTeacherName()
    {
        return teacherName;
    }

    public void setTeacherName(String teacherName)
    {
        this.teacherName = teacherName;
    }

    public String getTermName()
    {
        return termName;
    }

    public void setTermName(String termName)
    {
        this.termName = termName;
    }

    public Date getExamTime()
    {
        return examTime;
    }

    public void setExamTime(Date examTime)
    {
        this.examTime = examTime;
    }

    public String getClassroom()
    {
        return classroom;
    }

    public void setClassroom(String classroom)
    {
        this.classroom = classroom;
    }

    public String getSeatNo()
    {
        return seatNo;
    }

    public void setSeatNo(String seatNo)
    {
        this.seatNo = seatNo;
    }

    public String getExamType()
    {
        return examType;
    }

    public void setExamType(String examType)
    {
        this.examType = examType;
    }
}
