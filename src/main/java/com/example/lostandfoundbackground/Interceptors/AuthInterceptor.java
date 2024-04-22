package com.example.lostandfoundbackground.Interceptors;

import com.example.lostandfoundbackground.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;

}
