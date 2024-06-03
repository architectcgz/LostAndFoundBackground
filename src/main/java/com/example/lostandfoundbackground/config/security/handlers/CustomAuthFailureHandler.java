package com.example.lostandfoundbackground.config.security.handlers;

import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author archi
 */

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ResponseUtil.response(response, Result.error(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value(),"登录失败[" + exception.getMessage() + "]"),HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }
}
