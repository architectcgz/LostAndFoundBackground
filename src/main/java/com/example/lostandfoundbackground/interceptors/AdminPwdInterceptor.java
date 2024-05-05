package com.example.lostandfoundbackground.interceptors;

import com.example.lostandfoundbackground.dto.AdminDTO;
import com.example.lostandfoundbackground.interceptors.base.PwdInterceptor;

/**
 * @author archi
 */
public class AdminPwdInterceptor extends PwdInterceptor<AdminDTO> {
    @Override
    protected boolean getAllowModifyPwd(AdminDTO admin) {
        return admin.getAllowModifyPwd();
    }
}