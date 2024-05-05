package com.example.lostandfoundbackground.interceptors;

import com.example.lostandfoundbackground.dto.UserDTO;
import com.example.lostandfoundbackground.interceptors.base.PwdInterceptor;

/**
 * @author archi
 */
public class UserPwdInterceptor extends PwdInterceptor<UserDTO> {
    @Override
    protected boolean getAllowModifyPwd(UserDTO user) {
        return user.getAllowModifyPwd();
    }
}