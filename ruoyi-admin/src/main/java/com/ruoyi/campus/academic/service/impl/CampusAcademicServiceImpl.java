package com.ruoyi.campus.academic.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.campus.academic.domain.CampusCourseSection;
import com.ruoyi.campus.academic.domain.CampusExam;
import com.ruoyi.campus.academic.domain.CampusScore;
import com.ruoyi.campus.academic.domain.CampusStudent;
import com.ruoyi.campus.academic.domain.CampusTeacher;
import com.ruoyi.campus.academic.mapper.CampusAcademicMapper;
import com.ruoyi.campus.academic.service.ICampusAcademicService;
import com.ruoyi.common.exception.ServiceException;

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

    @Override
    public List<CampusCourseSection> selectAvailableSections(Long userId)
    {
        getStudentId(userId);
        return campusAcademicMapper.selectAvailableSections(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int enrollSection(Long sectionId, Long userId)
    {
        Long studentId = getStudentId(userId);
        if (campusAcademicMapper.countCurrentSection(sectionId) == 0)
        {
            throw new ServiceException("教学班不存在或不在当前学期");
        }
        if (campusAcademicMapper.countActiveSelection(studentId, sectionId) > 0)
        {
            throw new ServiceException("已选择该课程");
        }
        if (campusAcademicMapper.countScheduleConflict(userId, sectionId) > 0)
        {
            throw new ServiceException("该课程与已选课程时间冲突");
        }
        return campusAcademicMapper.insertStudentCourse(studentId, sectionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int dropSection(Long sectionId, Long userId)
    {
        Long studentId = getStudentId(userId);
        int rows = campusAcademicMapper.dropStudentCourse(studentId, sectionId);
        if (rows == 0)
        {
            throw new ServiceException("未找到可退选课程");
        }
        return rows;
    }

    private Long getStudentId(Long userId)
    {
        Long studentId = campusAcademicMapper.selectStudentIdByUserId(userId);
        if (studentId == null)
        {
            throw new ServiceException("未找到当前学生档案");
        }
        return studentId;
    }
}
