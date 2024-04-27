package com.example.lostandfoundbackground.Interceptors;

import cn.hutool.core.bean.BeanUtil;
import com.example.lostandfoundbackground.dto.AdminDTO;
import com.example.lostandfoundbackground.dto.UserDTO;
import com.example.lostandfoundbackground.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Slf4j
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;
    private final String loginKey;
    private final Long loginTTL;

    //1表示是user 2表示admin
    private final Byte userType;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate, String loginKey, Long loginTTL,Byte userType ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.loginKey = loginKey;
        this.loginTTL = loginTTL;
        this.userType = userType;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取请求头中的token
        String token = request.getHeader("Authorization");
        log.info("token"+token);
        //admin或user没有token时放行，也就是token长度为0时放行,使其可以登录获取token
        if(!StringUtils.hasLength(token)){
            return true;
        }
        //基于token获取redis中的user
        String key = loginKey + token;
        Map<Object,Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        System.out.println(userMap);
        //如果用户不存在,放行,让其获取token
        if(userMap.isEmpty()){
            return true;
        }
        //将查询到的hash转化为DTO
        Object dto = createDtoByUserType(userMap);
        //存在,保存信息到ThreadLocal
        Thread t = Thread.currentThread();
        log.info("设置threadlocal的线程名"+t.getName());
        ThreadLocalUtil.set(dto);
        System.out.println(dto);
        log.info("测试");
        //刷新token的有效期,这一步类似手机App登录如果每天使用,token就不会过期
        stringRedisTemplate.expire(key,loginTTL, TimeUnit.MINUTES);
        //放行
        return true;
    }

    private Object createDtoByUserType(Map<Object,Object> userMap){
        //1表示用户
        if(this.userType==1){
            return BeanUtil.fillBeanWithMap(userMap,new UserDTO(),false);
        } else if (this.userType == 2) {
            return BeanUtil.fillBeanWithMap(userMap,new AdminDTO(),false);
        }
        return null;
    }
}
