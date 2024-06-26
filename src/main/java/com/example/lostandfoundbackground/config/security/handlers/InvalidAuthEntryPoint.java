package com.example.lostandfoundbackground.config.security.handlers;

import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author archi
 */
@Slf4j
@Component
public class InvalidAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info(Arrays.toString(authException.getStackTrace()));
        ResponseUtil.response(response, Result.error(HttpStatus.UNAUTHORIZED,"身份验证失败,请先登录!"), org.springframework.http.HttpStatus.UNAUTHORIZED);
    }
}
