package com.example.lostandfoundbackground.config;

import com.example.lostandfoundbackground.Interceptors.AdminLoginInterceptor;
import com.example.lostandfoundbackground.Interceptors.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.example.lostandfoundbackground.constants.RedisConstants.*;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //TODO 添加一些用户不用登录也能看到的接口
        //登录拦截器
        //不拦截用户的注册接口、登录接口以及管理员的登录接口
        //Admin token刷新的拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate,LOGIN_ADMIN_KEY,LOGIN_ADMIN_TTL, (byte) 2))
                .addPathPatterns("/**").order(0);

        registry.addInterceptor(new AdminLoginInterceptor())
                .excludePathPatterns("/admin/login").order(1);
        //User token刷新的拦截器
//        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate,LOGIN_USER_KEY,LOGIN_USER_TTL, (byte) 1))
//                .addPathPatterns("/user/**").order(0);
    }

}
