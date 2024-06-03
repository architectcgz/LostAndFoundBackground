package com.example.lostandfoundbackground.config.security.authManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author archi
 */
@Slf4j
public class DelegatingAuthManager implements AuthenticationManager {

    private final CustomUserAuthManager customUserAuthManager;
    private final CustomAdminAuthManager customAdminAuthManager;

    public DelegatingAuthManager(CustomUserAuthManager customUserAuthManager, CustomAdminAuthManager customAdminAuthManager) {
        this.customUserAuthManager = customUserAuthManager;
        this.customAdminAuthManager = customAdminAuthManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURI();
        if (url.startsWith("/admin")) {
            log.info("使用CustomAdminAuthManager来认证");
            return customAdminAuthManager.authenticate(authentication);
        } else if (url.startsWith("/user")) {
            log.info("使用CustomUserAuthManager来认证");
            return customUserAuthManager.authenticate(authentication);
        } else {
            throw new AuthenticationServiceException("Cannot determine appropriate AuthenticationManager for request: " + url);
        }
    }
}

