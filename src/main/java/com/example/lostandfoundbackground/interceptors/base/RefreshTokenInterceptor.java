package com.example.lostandfoundbackground.interceptors.base;

import cn.hutool.core.bean.BeanUtil;
import com.example.lostandfoundbackground.utils.RedisUtils;
import com.example.lostandfoundbackground.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * @author archi
 */
@Slf4j
public abstract class RefreshTokenInterceptor implements HandlerInterceptor {
    protected final String loginKey;
    protected final Long loginTTL;

    public RefreshTokenInterceptor(String loginKey, Long loginTTL) {
        this.loginKey = loginKey;
        this.loginTTL = loginTTL;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取请求头中的token
        String token = request.getHeader("Authorization");
        log.info("请求头中的token是:"+token);
        //admin或user没有token时放行,使其可以登录获取token
        if(ObjectUtils.isEmpty(token)){
            log.info("没有token,放行，去获取token");
            return true;
        }
        //基于token获取redis中的user
        String key = loginKey + token;
        log.info("RefreshToken拦截器的key为:"+key);
        Map<Object,Object> userMap = RedisUtils.hmget(key);
        log.info("userMap是否为空:"+(userMap.isEmpty()||userMap==null));
        for (Object o: userMap.keySet()){
            log.info(o.toString());
        }
        //如果用户不存在,放行,让其获取token
        if(userMap.isEmpty()){
            log.info("用户在ThreadLocal中不存在，放行，让其获取token");
            log.info("通过RefreshTokenInterceptor:"+true);
            return true;
        }
        //将查询到的hash转化为DTO
        Object dto = BeanUtil.fillBeanWithMap(userMap, createDTO(), false);
        //存在,保存信息到ThreadLocal
        ThreadLocalUtil.set(dto);
        System.out.println(dto);
        //刷新token的有效期,这一步类似手机App登录如果每天使用,token就不会过期
        RedisUtils.expire(key,loginTTL);
        //放行
        log.info("通过RefreshTokenInterceptor:"+true);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户
        ThreadLocalUtil.remove();
    }

    protected abstract Object createDTO();
}