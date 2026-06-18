package com.ruoyi.common.api.system;

import com.ruoyi.common.core.domain.entity.SysUser;

import java.util.List;

/**
 * @author ccc
 */
public interface SysUserApi {

    public List<SysUser> selectLeaderList();
}
