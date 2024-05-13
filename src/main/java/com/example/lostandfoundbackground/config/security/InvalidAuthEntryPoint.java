package com.example.lostandfoundbackground.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author archi
 */
@Slf4j
@Component
public class InvalidAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("Jwt authentication failed. " + authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Jwt authentication failed. " + authException.getMessage());
    }
}
