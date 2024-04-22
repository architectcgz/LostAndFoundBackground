package com.example.lostandfoundbackground.config;

import com.example.lostandfoundbackground.Interceptors.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //不拦截用户的注册接口、登录接口以及管理员的登录接口
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/user/login","/user/register",
                        "/admin/login");
    }
}
