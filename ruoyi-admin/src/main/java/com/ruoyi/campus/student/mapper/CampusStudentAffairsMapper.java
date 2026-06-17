package com.ruoyi.campus.student.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.campus.student.domain.CampusStudentAffairsProfile;
import com.ruoyi.campus.student.domain.CampusStudentAffairsRecord;

/**
 * 学工信息Mapper
 */
public interface CampusStudentAffairsMapper
{
    CampusStudentAffairsProfile selectProfileByUserId(Long userId);

    List<CampusStudentAffairsRecord> selectRecordsByUserId(Long userId);

    List<CampusStudentAffairsProfile> selectProfileList();

    List<Map<String, Object>> selectRecordStats();
}
