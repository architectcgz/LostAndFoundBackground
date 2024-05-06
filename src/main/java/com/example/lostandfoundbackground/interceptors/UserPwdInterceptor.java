package com.example.lostandfoundbackground.interceptors;

import com.example.lostandfoundbackground.dto.UserDTO;
import com.example.lostandfoundbackground.interceptors.base.PwdInterceptor;
import com.example.lostandfoundbackground.utils.RedisUtils;

import static com.example.lostandfoundbackground.constants.RedisConstants.USER_VERIFICATION_STATUS;

/**
 * @author archi
 */
public class UserPwdInterceptor extends PwdInterceptor<UserDTO> {
    @Override
    protected boolean getAllowModifyPwd(UserDTO user) {

        String result = RedisUtils.get(USER_VERIFICATION_STATUS+user.getPhone());
        if(result == null){
            //redis中不存在 result,也就是已经修改过,不允许未经验证重复修改
            return false;
        }
        //当result为true时也就是验证通过，允许修改密码
        return Boolean.parseBoolean(result);
    }
}