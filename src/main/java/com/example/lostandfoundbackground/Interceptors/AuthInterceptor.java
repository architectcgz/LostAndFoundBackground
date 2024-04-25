package com.example.lostandfoundbackground.Interceptors;

import com.example.lostandfoundbackground.service.RedisService;
import com.example.lostandfoundbackground.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取令牌验证
        String token = request.getHeader("Authorization");
        try {
            //token为null或为空串，不通过
            if(token == null || token.isEmpty()){
                return false;
            }
            //ValueOperations<String,String> operations = redisService.
        }catch (Exception e){
            //设置错误响应码401
            response.setStatus(401);
            //不通过
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //请求接口结束后执行，清理ThreadLocal中占用的内存，防止内存泄露
        ThreadLocalUtil.remove();
    }
}
