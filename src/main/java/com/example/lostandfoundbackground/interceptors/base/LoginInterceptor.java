package com.example.lostandfoundbackground.interceptors.base;

import com.example.lostandfoundbackground.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author archi
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入登录拦截器");
        // 1.判断是否需要拦截（ThreadLocal中是否有用户）
        Object o = ThreadLocalUtil.get();
        if (o == null) {
            // 没有，需要拦截，设置状态码
            response.setStatus(401);
            // 拦截
            return false;
        }
        // 有用户，则放行
        log.info(o.getClass()+"通过RefreshTokenInterceptor:"+true);
        return true;
    }
}
