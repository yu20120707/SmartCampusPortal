package com.ruoyi.campus.academic.domain;

import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 成绩对象 campus_score
 */
public class CampusScore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long scoreId;

    private Long sectionId;

    private String courseName;

    private String termName;

    private BigDecimal credit;

    private BigDecimal score;

    private BigDecimal gradePoint;

    private String examType;

    public Long getScoreId()
    {
        return scoreId;
    }

    public void setScoreId(Long scoreId)
    {
        this.scoreId = scoreId;
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

    public String getTermName()
    {
        return termName;
    }

    public void setTermName(String termName)
    {
        this.termName = termName;
    }

    public BigDecimal getCredit()
    {
        return credit;
    }

    public void setCredit(BigDecimal credit)
    {
        this.credit = credit;
    }

    public BigDecimal getScore()
    {
        return score;
    }

    public void setScore(BigDecimal score)
    {
        this.score = score;
    }

    public BigDecimal getGradePoint()
    {
        return gradePoint;
    }

    public void setGradePoint(BigDecimal gradePoint)
    {
        this.gradePoint = gradePoint;
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
