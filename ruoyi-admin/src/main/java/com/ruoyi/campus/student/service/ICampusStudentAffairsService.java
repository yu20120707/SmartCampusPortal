package com.ruoyi.campus.student.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.campus.student.domain.CampusStudentAffairsProfile;
import com.ruoyi.campus.student.domain.CampusStudentAffairsRecord;

/**
 * 学工信息服务接口
 */
public interface ICampusStudentAffairsService
{
    CampusStudentAffairsProfile selectMyProfile(Long userId);

    List<CampusStudentAffairsRecord> selectMyRecords(Long userId);

    Map<String, Object> selectOverview();
}
