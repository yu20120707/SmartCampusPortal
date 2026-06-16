package com.ruoyi.campus.academic.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.campus.academic.domain.CampusCourseSection;
import com.ruoyi.campus.academic.domain.CampusExam;
import com.ruoyi.campus.academic.domain.CampusScore;
import com.ruoyi.campus.academic.domain.CampusStudent;
import com.ruoyi.campus.academic.domain.CampusTeacher;
import com.ruoyi.campus.academic.mapper.CampusAcademicMapper;
import com.ruoyi.campus.academic.service.ICampusAcademicService;

/**
 * 校园教务服务实现
 */
@Service
public class CampusAcademicServiceImpl implements ICampusAcademicService
{
    @Autowired
    private CampusAcademicMapper campusAcademicMapper;

    @Override
    public CampusStudent selectCurrentStudent(Long userId)
    {
        return campusAcademicMapper.selectStudentByUserId(userId);
    }

    @Override
    public CampusTeacher selectCurrentTeacher(Long userId)
    {
        return campusAcademicMapper.selectTeacherByUserId(userId);
    }

    @Override
    public List<CampusCourseSection> selectMyCourses(Long userId)
    {
        return campusAcademicMapper.selectStudentCourses(userId);
    }

    @Override
    public List<CampusCourseSection> selectMySchedule(Long userId)
    {
        return campusAcademicMapper.selectStudentSchedule(userId);
    }

    @Override
    public List<CampusScore> selectMyScores(Long userId)
    {
        return campusAcademicMapper.selectStudentScores(userId);
    }

    @Override
    public List<CampusExam> selectMyExams(Long userId)
    {
        return campusAcademicMapper.selectStudentExams(userId);
    }

    @Override
    public List<CampusCourseSection> selectMyTeachingCourses(Long userId)
    {
        return campusAcademicMapper.selectTeacherCourses(userId);
    }

    @Override
    public List<CampusCourseSection> selectMyTeachingSchedule(Long userId)
    {
        return campusAcademicMapper.selectTeacherSchedule(userId);
    }

    @Override
    public List<CampusExam> selectMyTeachingExams(Long userId)
    {
        return campusAcademicMapper.selectTeacherExams(userId);
    }
}
