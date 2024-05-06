package com.example.lostandfoundbackground.config;

import com.example.lostandfoundbackground.interceptors.*;
import com.example.lostandfoundbackground.interceptors.base.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.example.lostandfoundbackground.constants.RedisConstants.*;

/**
 * @author archi
 */
@Configuration
public class AuthConfig implements WebMvcConfigurer {

    //不需要登录也能访问的地址
    private final String []WHITE_LIST = {
            "/admin/login",
            "/user/login"
    };
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //TODO 添加一些用户不用登录也能看到的接口

        //登录拦截器,注意这里是admin与user共用一个登录拦截器
        //如果分开创建两个拦截器，如果不排除彼此，会出现互相拦截的情况
        //所以这里用一个登录拦截器即可
        //耗时1hour测出来的bug byd

        registry.addInterceptor(new LoginInterceptor())
                        .excludePathPatterns(WHITE_LIST).order(1);


        //不拦截用户的注册接口、登录接口以及管理员的登录接口
        //Admin token刷新的拦截器
        registry.addInterceptor(new AdminRefreshTokenInterceptor(LOGIN_ADMIN_KEY,LOGIN_ADMIN_TTL))
                .addPathPatterns("/admin/**").order(0);

        //修改密码的拦截器，必须验证码验证完成后才允许访问修改密码的接口
        registry.addInterceptor(new AdminPwdInterceptor())
                .addPathPatterns("/admin/modifyPwd").order(2);

        //User token刷新的拦截器
        registry.addInterceptor(new UserRefreshTokenInterceptor(LOGIN_USER_KEY,LOGIN_USER_TTL))
                .addPathPatterns("/user/**").order(0);

        registry.addInterceptor(new UserPwdInterceptor())
                .addPathPatterns("/user/modifyPwd").order(2);
    }

}
