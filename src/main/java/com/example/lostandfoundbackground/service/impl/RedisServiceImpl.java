package com.example.lostandfoundbackground.service.impl;

import com.example.lostandfoundbackground.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public boolean saveToken() {
        //ValueOperations<String, Object> ops = stringRedisTemplate.opsForValue();
        return true;
    }

    @Override
    public boolean getToken() {
        return false;
    }
}
