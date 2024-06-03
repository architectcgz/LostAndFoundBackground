package com.example.lostandfoundbackground.config.security.handlers;

import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author archi
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ResponseUtil.response(response, Result.error(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value(),"权限不足[" + accessDeniedException.getMessage() + "]"), HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }
}
