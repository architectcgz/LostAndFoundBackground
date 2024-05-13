package com.example.lostandfoundbackground.interceptors.base;

import com.example.lostandfoundbackground.dto.AdminDTO;
import com.example.lostandfoundbackground.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author archi
 */
@Slf4j
public class CategoryInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入分类拦截器");
        Object o = ThreadLocalUtil.get();
        if(o==null){
            log.info("ThreadLocal中没有内容");
            return false;
        }
        log.info(o.getClass().toString());
        //只有管理员可以管理分类,用户可以通过请求管理员添加分类的方式来让管理员添加分类
        return o.getClass() == AdminDTO.class;
    }
}
