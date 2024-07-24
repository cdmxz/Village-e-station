package com.ces.Village.service;

import com.ces.Village.pojo.dto.WxOpenIdResponse;

/**
 * 登录、注册 前端服务
 */
public interface LoginService {
    /**
     * 通过普通用户id获取用户的token
     * 此token用于前端和后端交互
     */
    String createUserLoginToken(Object userId);

    /**
     * 通过管理员的用户id获取用户的token
     * 此token用于前端和后端交互
     */
    String createAdminLoginToken(Object adminUserId);

    /**
     * 通过code获取用户的openid
     */
    WxOpenIdResponse getWxOpenId(String code);

    /**
     * 刷新token
     * @param oldToken
     * @return
     */
    String refreshToken(String oldToken);
}
