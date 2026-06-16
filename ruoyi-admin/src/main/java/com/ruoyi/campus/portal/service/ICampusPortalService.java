package com.ruoyi.campus.portal.service;

import java.util.Map;

/**
 * 校园门户聚合服务接口
 */
public interface ICampusPortalService
{
    Map<String, Object> selectCurrentPortal(Long userId, String username);
}
