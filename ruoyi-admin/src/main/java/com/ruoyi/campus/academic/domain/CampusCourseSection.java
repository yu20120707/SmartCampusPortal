package com.ruoyi.campus.academic.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教学班与课表对象 campus_course_section
 */
public class CampusCourseSection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long sectionId;

    private Long courseId;

    private String courseCode;

    private String courseName;

    private Long teacherId;

    private String teacherName;

    private Long termId;

    private String termName;

    private Integer weekday;

    private Integer startSection;

    private Integer endSection;

    private String classroom;

    private String weekRange;

    private Integer studentCount;

    public Long getSectionId()
    {
        return sectionId;
    }

    public void setSectionId(Long sectionId)
    {
        this.sectionId = sectionId;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public String getCourseCode()
    {
        return courseCode;
    }

    public void setCourseCode(String courseCode)
    {
        this.courseCode = courseCode;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public Long getTeacherId()
    {
        return teacherId;
    }

    public void setTeacherId(Long teacherId)
    {
        this.teacherId = teacherId;
    }

    public String getTeacherName()
    {
        return teacherName;
    }

    public void setTeacherName(String teacherName)
    {
        this.teacherName = teacherName;
    }

    public Long getTermId()
    {
        return termId;
    }

    public void setTermId(Long termId)
    {
        this.termId = termId;
    }

    public String getTermName()
    {
        return termName;
    }

    public void setTermName(String termName)
    {
        this.termName = termName;
    }

    public Integer getWeekday()
    {
        return weekday;
    }

    public void setWeekday(Integer weekday)
    {
        this.weekday = weekday;
    }

    public Integer getStartSection()
    {
        return startSection;
    }

    public void setStartSection(Integer startSection)
    {
        this.startSection = startSection;
    }

    public Integer getEndSection()
    {
        return endSection;
    }

    public void setEndSection(Integer endSection)
    {
        this.endSection = endSection;
    }

    public String getClassroom()
    {
        return classroom;
    }

    public void setClassroom(String classroom)
    {
        this.classroom = classroom;
    }

    public String getWeekRange()
    {
        return weekRange;
    }

    public void setWeekRange(String weekRange)
    {
        this.weekRange = weekRange;
    }

    public Integer getStudentCount()
    {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount)
    {
        this.studentCount = studentCount;
    }
}
