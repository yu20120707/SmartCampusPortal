package com.ruoyi.system.api;

import com.ruoyi.system.domain.vo.SysUserVO;
import com.ruoyi.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccc
 */
@Service
public class SysUserApiImpl implements SysUserApi {

    @Resource
    private ISysUserService sysUserService;

    @Override
    public List<SysUserVO> selectLeaderList() {
        return sysUserService.selectLeaderList();
    }
}
