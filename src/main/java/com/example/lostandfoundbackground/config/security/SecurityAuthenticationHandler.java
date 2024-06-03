package com.example.lostandfoundbackground.config.security;

import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * @author archi
 *  * 根据情况设置状态码
 *  * 将返回结果写入到response
 *  * 接口：
 *  * AuthenticationSuccessHandler         ：认证成功 （自定登录接口，json的方式，实现此接口的方法是无效的）
 *  * AuthenticationFailureHandler         ：认证失败
 *  * LogoutSuccessHandler                 ：退出登录成功（自定注销登录接口，实现此接口的方法是无效的）
 *  * SessionInformationExpiredStrategy    ：会话过期 （token认证，实现此接口的方法是无效的）
 *  * AccessDeniedHandler                  ：验证权限失败 403 (用来解决认证过的用户访问无权限资源时的异常)
 *  * AuthenticationEntryPoint             ：匿名用户访问无权限资源时的异常，
 */
public class SecurityAuthenticationHandler implements AuthenticationSuccessHandler,
        AuthenticationFailureHandler,
        AuthenticationEntryPoint,
        AccessDeniedHandler,
        LogoutSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseUtil.response(response, Result.ok(authentication), HttpStatus.OK);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseUtil.response(response, Result.ok(authentication), HttpStatus.OK);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ResponseUtil.response(response,Result.error(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value(),"登录失败[" + exception.getMessage() + "]"),HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseUtil.response(response, Result.error(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value(),"权限不足[" + accessDeniedException.getMessage() + "]"), HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseUtil.response(response, Result.error(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value(),"认证失败[" + authException.getMessage() + "]"), HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }


}
