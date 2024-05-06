package com.example.lostandfoundbackground.interceptors.base;

import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.utils.JsonUtils;
import com.example.lostandfoundbackground.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

/**
 * @author archi
 */
@Slf4j
public abstract class PwdInterceptor<T> implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        T user = ThreadLocalUtil.get();
        //设置编码格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter pw = response.getWriter();

        log.info("\n尝试修改密码,此时的用户是:\n"+ user);
        //登录的用户要修改密码必须先验证修改密码的验证码
        if(!getAllowModifyPwd(user)){
            try {
                pw.write(JsonUtils.javaBeanToJsonString(Result.error(1,"不允许修改密码,请进行验证码校验!")));
            }catch (RuntimeException e){
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    protected abstract boolean getAllowModifyPwd(T user);
}
