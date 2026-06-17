package com.ruoyi.campus.academic.service;

import java.util.List;
import com.ruoyi.campus.academic.domain.CampusCourseSection;
import com.ruoyi.campus.academic.domain.CampusExam;
import com.ruoyi.campus.academic.domain.CampusScore;
import com.ruoyi.campus.academic.domain.CampusStudent;
import com.ruoyi.campus.academic.domain.CampusTeacher;

/**
 * 校园教务服务接口
 */
public interface ICampusAcademicService
{
    CampusStudent selectCurrentStudent(Long userId);

    CampusTeacher selectCurrentTeacher(Long userId);

    List<CampusCourseSection> selectMyCourses(Long userId);

    List<CampusCourseSection> selectMySchedule(Long userId);

    List<CampusScore> selectMyScores(Long userId);

    List<CampusExam> selectMyExams(Long userId);

    List<CampusCourseSection> selectMyTeachingCourses(Long userId);

    List<CampusCourseSection> selectMyTeachingSchedule(Long userId);

    List<CampusExam> selectMyTeachingExams(Long userId);

    List<CampusScore> selectTeachingScores(Long sectionId, Long userId);

    int saveTeachingScore(Long sectionId, Long studentId, CampusScore score, Long userId);

    List<CampusCourseSection> selectAvailableSections(Long userId);

    int enrollSection(Long sectionId, Long userId);

    int dropSection(Long sectionId, Long userId);
}
