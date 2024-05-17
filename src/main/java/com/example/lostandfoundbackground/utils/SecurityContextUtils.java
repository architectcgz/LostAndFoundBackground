package com.example.lostandfoundbackground.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

/**
 * @author archi
 */
@Slf4j
public class SecurityContextUtils {
    public static UserDetails getLocalUserDetail(){
        Object userPrinciple = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userPrinciple instanceof UserDetails){
            return (UserDetails) userPrinciple;
        }else{
            log.info("上下文中没有userDetail对象");
            return null;
        }
    }
}

