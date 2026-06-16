package com.ruoyi.campus.academic.mapper;

import java.util.List;
import com.ruoyi.campus.academic.domain.CampusCourseSection;
import com.ruoyi.campus.academic.domain.CampusExam;
import com.ruoyi.campus.academic.domain.CampusScore;
import com.ruoyi.campus.academic.domain.CampusStudent;
import com.ruoyi.campus.academic.domain.CampusTeacher;
import org.apache.ibatis.annotations.Param;

/**
 * 校园教务只读查询 Mapper
 */
public interface CampusAcademicMapper
{
    CampusStudent selectStudentByUserId(@Param("userId") Long userId);

    CampusTeacher selectTeacherByUserId(@Param("userId") Long userId);

    List<CampusCourseSection> selectStudentCourses(@Param("userId") Long userId);

    List<CampusCourseSection> selectStudentSchedule(@Param("userId") Long userId);

    List<CampusScore> selectStudentScores(@Param("userId") Long userId);

    List<CampusExam> selectStudentExams(@Param("userId") Long userId);

    List<CampusCourseSection> selectTeacherCourses(@Param("userId") Long userId);

    List<CampusCourseSection> selectTeacherSchedule(@Param("userId") Long userId);

    List<CampusExam> selectTeacherExams(@Param("userId") Long userId);

}
