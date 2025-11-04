package com.ces.village.service.impl;

import com.ces.village.common.CurrentUser;
import com.ces.village.constant.JwtClaimsConstant;
import com.ces.village.pojo.dto.WxOpenIdResponse;
import com.ces.village.properties.JwtProperties;
import com.ces.village.service.LoginService;
import com.ces.village.utils.JwtUtil;
import com.ces.village.utils.WxApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录、注册 前端服务
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private WxApiUtil wxApiUtil;


    /**
     * 通过code获取用户的openid
     *
     * @param code
     * @return
     */
    @Override
    public WxOpenIdResponse getWxOpenId(String code) {
        return wxApiUtil.code2OpenId(code);
    }

    /**
     * 刷新token
     *
     * @param oldToken
     * @return
     */
    @Override
    public String refreshToken(String oldToken) {
        CurrentUser currentUser = JwtUtil.parseJWTToCurUser(jwtProperties, oldToken);
        if (currentUser != null) {
            if (currentUser.isAdmin()) {
                return createAdminLoginToken(currentUser.getId());
            } else {
                return createUserLoginToken(currentUser.getId());
            }
        }
        return null;
    }


    /**
     * 通过普通用户id获取用户的token
     * 此token用于前端和后端交互
     *
     * @param userId
     * @return
     */
    @Override
    public String createUserLoginToken(Object userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, userId);
        // 设置身份为用户
        claims.put(JwtClaimsConstant.IDENTITY, JwtClaimsConstant.IDENTITY_USER);
        String token = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTokenTtl(),
                claims);
        return token;
    }

    /**
     * 通过管理员的用户id获取用户的token
     * 此token用于前端和后端交互
     *
     * @param adminUserId
     * @return
     */
    @Override
    public String createAdminLoginToken(Object adminUserId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, adminUserId);
        // 设置身份为管理员
        claims.put(JwtClaimsConstant.IDENTITY, JwtClaimsConstant.IDENTITY_ADMIN);
        String token = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTokenTtl(),
                claims);
        return token;
    }


}
