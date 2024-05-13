package com.example.lostandfoundbackground.interceptors.admin;

import com.example.lostandfoundbackground.dto.AdminDTO;
import com.example.lostandfoundbackground.interceptors.base.PwdInterceptor;
import com.example.lostandfoundbackground.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.example.lostandfoundbackground.constants.RedisConstants.ADMIN_SMS_CODE_KEY;

/**
 * @author archi
 */
@Slf4j
public class AdminPwdInterceptor extends PwdInterceptor<AdminDTO> {
    @Override
    protected boolean getAllowModifyPwd(AdminDTO admin) {
        Map<Object,Object>result = RedisUtils.hmget(ADMIN_SMS_CODE_KEY +admin.getPhone());
        //redis中不存在 result,也就是已经修改过,不允许未经验证重复修改
        //当result为true时也就是验证通过，允许修改密码
        String verified = (String) result.get("verified");
        return Boolean.parseBoolean(verified);
    }
}