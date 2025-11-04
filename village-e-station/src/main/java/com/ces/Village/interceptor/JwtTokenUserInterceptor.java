package com.ces.village.interceptor;

import com.ces.village.annotation.LoginRequired;
import com.ces.village.common.BaseContext;
import com.ces.village.common.CurrentUser;
import com.ces.village.common.R;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.constant.JwtClaimsConstant;
import com.ces.village.properties.JwtProperties;
import com.ces.village.utils.JsonConvertUtil;
import com.ces.village.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * jwt令牌校验的拦截器
 * 登录拦截器
 */
@Component
@Log4j2
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    public JwtTokenUserInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 校验jwt
     *
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod method)) {
            // 当前拦截到的不是动态方法，直接放行
            return true;
        }
        boolean hasLoginAnnotation = method.getMethod().isAnnotationPresent(LoginRequired.class);
        if (!hasLoginAnnotation) {
            // 不存在LoginRequired注解，则直接通过
            log.info("不存在LoginRequired注解，则直接通过");
            return true;
        }
        LoginRequired loginRequired = method.getMethod().getAnnotation(LoginRequired.class);
        // required=false,则无需检查是否登录
        if (!loginRequired.required()) {
            log.info("required=false,无需检查是否登录");
            return true;
        }
        // 登录状态检查,使用response返回指定信息
        log.info("required=true,需检查是否登录");
        // 1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getTokenName());
        if (StringUtil.isNullOrEmpty(token)) {
            responseError(response, ErrorCodeEnum.USER_NOT_LOGGED_IN);
            return false;
        }
        try {
            // 2、校验用户令牌
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            boolean isAdmin = claims.get(JwtClaimsConstant.IDENTITY).equals(JwtClaimsConstant.IDENTITY_ADMIN);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户的id：{}，是否为管理员：{}", userId, isAdmin);
            if (loginRequired.requireAdmin()) {
                log.info("requireAdmin=true，需检查当前用户是否为管理员");
                if (!isAdmin) {
                    responseError(response, ErrorCodeEnum.USER_NOT_HAVE_PERMISSION);
                    return false;
                }
            }
            // 把当前用户加入到本地线程变量
            BaseContext.setCurrentUser(new CurrentUser(userId, isAdmin));
            // 3、通过，放行
            return true;
        } catch (Exception ex) {
            // 不通过
            log.error("jwt校验失败:{}", ex.getMessage());
            responseError(response, ErrorCodeEnum.TOKEN_INVALID);
        }
        return false;
    }


    /**
     * 向客户端响应 错误码
     */
    private void responseError(HttpServletResponse response, ErrorCodeEnum errorCodeEnum) {
        try {
            R<?> r = R.error(errorCodeEnum);
            String json = JsonConvertUtil.toJsonString(r);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            // 获取PrintWriter对象
            PrintWriter out = response.getWriter();
            out.print(json);
            // 释放PrintWriter对象
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
