package com.ruoyi.campus.student.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.campus.student.domain.CampusStudentAffairsProfile;
import com.ruoyi.campus.student.domain.CampusStudentAffairsRecord;
import com.ruoyi.campus.student.mapper.CampusStudentAffairsMapper;
import com.ruoyi.campus.student.service.ICampusStudentAffairsService;

/**
 * 学工信息服务实现
 */
@Service
public class CampusStudentAffairsServiceImpl implements ICampusStudentAffairsService
{
    @Autowired
    private CampusStudentAffairsMapper campusStudentAffairsMapper;

    @Override
    public CampusStudentAffairsProfile selectMyProfile(Long userId)
    {
        return campusStudentAffairsMapper.selectProfileByUserId(userId);
    }

    @Override
    public List<CampusStudentAffairsRecord> selectMyRecords(Long userId)
    {
        return campusStudentAffairsMapper.selectRecordsByUserId(userId);
    }

    @Override
    public Map<String, Object> selectOverview()
    {
        Map<String, Object> overview = new HashMap<>();
        overview.put("profiles", campusStudentAffairsMapper.selectProfileList());
        overview.put("recordStats", campusStudentAffairsMapper.selectRecordStats());
        return overview;
    }
}
