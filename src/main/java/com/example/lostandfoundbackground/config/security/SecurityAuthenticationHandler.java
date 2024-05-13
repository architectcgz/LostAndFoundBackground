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
