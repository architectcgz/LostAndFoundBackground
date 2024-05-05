package com.example.lostandfoundbackground.interceptors;

import com.example.lostandfoundbackground.dto.UserDTO;
import com.example.lostandfoundbackground.interceptors.base.RefreshTokenInterceptor;


/**
 * @author archi
 */

public class UserRefreshTokenInterceptor extends RefreshTokenInterceptor {

    public UserRefreshTokenInterceptor(String loginKey, Long loginTTL) {
        super(loginKey, loginTTL);
    }

    @Override
    protected Object createDTO() {
        return new UserDTO();
    }
}
