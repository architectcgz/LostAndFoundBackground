package com.example.lostandfoundbackground.interceptors.admin;

import com.example.lostandfoundbackground.dto.AdminDTO;
import com.example.lostandfoundbackground.interceptors.base.RefreshTokenInterceptor;
import lombok.extern.slf4j.Slf4j;
/**
 * @author archi
 */
@Slf4j
public class AdminRefreshTokenInterceptor extends RefreshTokenInterceptor {
    public AdminRefreshTokenInterceptor(String loginKey, Long loginTTL) {
        super(loginKey, loginTTL);
    }

    @Override
    protected Object createDTO() {
        return new AdminDTO();
    }
}
