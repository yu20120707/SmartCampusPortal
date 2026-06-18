package com.ruoyi.system.api;

import com.ruoyi.common.api.system.SysUserApi;
import com.ruoyi.common.core.domain.entity.SysUser;
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
    public List<SysUser> selectLeaderList() {
        return sysUserService.selectLeaderList();
    }
}
