package com.ruoyi.campus.academic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.campus.academic.domain.CampusScore;
import com.ruoyi.campus.academic.service.ICampusAcademicService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 校园教务只读接口
 */
@RestController
@RequestMapping("/campus/academic")
public class CampusAcademicController extends BaseController
{
    @Autowired
    private ICampusAcademicService campusAcademicService;

    @PreAuthorize("@ss.hasPermi('campus:academic:view')")
    @GetMapping("/profile/student")
    public AjaxResult studentProfile()
    {
        return success(campusAcademicService.selectCurrentStudent(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:academic:view')")
    @GetMapping("/courses/my")
    public AjaxResult myCourses()
    {
        return success(campusAcademicService.selectMyCourses(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:academic:view')")
    @GetMapping("/schedule/my")
    public AjaxResult mySchedule()
    {
        return success(campusAcademicService.selectMySchedule(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:academic:view')")
    @GetMapping("/grades/my")
    public AjaxResult myScores()
    {
        return success(campusAcademicService.selectMyScores(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:academic:view')")
    @GetMapping("/exams/my")
    public AjaxResult myExams()
    {
        return success(campusAcademicService.selectMyExams(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:academic:view')")
    @GetMapping("/electives/available")
    public AjaxResult availableElectives()
    {
        return success(campusAcademicService.selectAvailableSections(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:academic:view')")
    @PostMapping("/electives/{sectionId}/enroll")
    public AjaxResult enroll(@PathVariable Long sectionId)
    {
        return toAjax(campusAcademicService.enrollSection(sectionId, getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:academic:view')")
    @DeleteMapping("/electives/{sectionId}")
    public AjaxResult drop(@PathVariable Long sectionId)
    {
        return toAjax(campusAcademicService.dropSection(sectionId, getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:teacher:view')")
    @GetMapping("/teacher/profile")
    public AjaxResult teacherProfile()
    {
        return success(campusAcademicService.selectCurrentTeacher(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:teacher:view')")
    @GetMapping("/teacher/courses")
    public AjaxResult teachingCourses()
    {
        return success(campusAcademicService.selectMyTeachingCourses(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:teacher:view')")
    @GetMapping("/teacher/schedule")
    public AjaxResult teachingSchedule()
    {
        return success(campusAcademicService.selectMyTeachingSchedule(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:teacher:view')")
    @GetMapping("/teacher/exams")
    public AjaxResult teachingExams()
    {
        return success(campusAcademicService.selectMyTeachingExams(getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:teacher:view')")
    @GetMapping("/teacher/sections/{sectionId}/scores")
    public AjaxResult teachingScores(@PathVariable Long sectionId)
    {
        return success(campusAcademicService.selectTeachingScores(sectionId, getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('campus:teacher:view')")
    @PutMapping("/teacher/sections/{sectionId}/students/{studentId}/score")
    public AjaxResult saveTeachingScore(@PathVariable Long sectionId, @PathVariable Long studentId,
            @RequestBody CampusScore score)
    {
        return toAjax(campusAcademicService.saveTeachingScore(sectionId, studentId, score, getUserId()));
    }
}
