package com.example.lostandfoundbackground.service.impl;

import com.example.lostandfoundbackground.config.security.userDetails.SecurityUserDetails;
import com.example.lostandfoundbackground.entity.User;
import com.example.lostandfoundbackground.mapper.UserMapper;
import com.example.lostandfoundbackground.utils.JsonUtils;
import com.example.lostandfoundbackground.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.lostandfoundbackground.constants.RedisConstants.LOGIN_USER_PHONE;

/**
 * @author archi
 */
@Slf4j
@Service
public class MyUserDetailService implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从redis中先查找用户，不存在再去数据库找,找到存放到redis中
        //校验手机号和密码

        String key = LOGIN_USER_PHONE + username;
        User user = null;
        String jsonUser = RedisUtils.get(key);
        //将json反序列化为administrator类型
        user = JsonUtils.jsonToJavaBean(jsonUser, User.class);
        if(user == null){
            user = userMapper.findUserBaseByPhone(username);
            if(user == null){
                log.info("User name not found: " + username);
                throw new UsernameNotFoundException("User name not found: " + username);
            }
        }

        // 1. commaSeparatedStringToAuthorityList放入角色时需要加前缀ROLE_，而在controller使用时不需要加ROLE_前缀
        // 2. 放入的是权限时，不能加ROLE_前缀，hasAuthority与放入的权限名称对应即可
        SecurityUserDetails userDetails = new SecurityUserDetails(
                user,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"),
                true,true,true,true);
        //mysql中查到的信息放到redis里，下次登录可以先从redis中查询
        //设置3天失效3*24*60
        RedisUtils.storeBeanAsJson(user,key,4320);
        return userDetails;
    }
}
