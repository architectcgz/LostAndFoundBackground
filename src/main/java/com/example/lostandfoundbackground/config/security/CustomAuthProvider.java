package com.example.lostandfoundbackground.config.security;

import com.example.lostandfoundbackground.service.impl.MyAdminDetailService;
import com.example.lostandfoundbackground.service.impl.MyUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author archi
 */
@Component
@Slf4j
public class CustomAuthProvider implements AuthenticationProvider {

    private final MyAdminDetailService myAdminDetailService;
    private final MyUserDetailService myUserDetailService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthProvider(MyUserDetailService myUserDetailService,
                              MyAdminDetailService myAdminDetailService,
                              PasswordEncoder passwordEncoder){
        this.myUserDetailService = myUserDetailService;
        this.myAdminDetailService = myAdminDetailService;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURL().toString();
        log.info("访问的url是 "+url);
        if (url.contains("/admin")) {
            log.info("使用adminAuthenticationProvider来认证");
            return adminAuthenticationProvider().authenticate(authentication);
        } else if (url.contains("/user")) {
            log.info("使用userAuthenticationProvider来认证");
            return userAuthenticationProvider().authenticate(authentication);
        }
        throw new AuthenticationServiceException("Unable to authenticate user for provided credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication
                .equals(UsernamePasswordAuthenticationToken.class);
    }

    public AuthenticationProvider userAuthenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    public AuthenticationProvider adminAuthenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myAdminDetailService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
