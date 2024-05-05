package com.example.lostandfoundbackground.interceptors.base;

import com.example.lostandfoundbackground.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author archi
 */
public abstract class PwdInterceptor<T> implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        T user = ThreadLocalUtil.get();
        return user != null && getAllowModifyPwd(user);
    }

    protected abstract boolean getAllowModifyPwd(T user);
}
