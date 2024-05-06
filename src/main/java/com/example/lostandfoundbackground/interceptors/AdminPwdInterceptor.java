package com.example.lostandfoundbackground.interceptors;

import com.example.lostandfoundbackground.dto.AdminDTO;
import com.example.lostandfoundbackground.interceptors.base.PwdInterceptor;
import com.example.lostandfoundbackground.utils.RedisUtils;

import java.util.Map;

import static com.example.lostandfoundbackground.constants.RedisConstants.ADMIN_VERIFICATION_STATUS;

/**
 * @author archi
 */
public class AdminPwdInterceptor extends PwdInterceptor<AdminDTO> {
    @Override
    protected boolean getAllowModifyPwd(AdminDTO admin) {
        String result = RedisUtils.get(ADMIN_VERIFICATION_STATUS+admin.getPhone());
        if(result == null){
            //redis中不存在 result,也就是已经修改过,不允许未经验证重复修改
            return false;
        }
        //当result为true时也就是验证通过，允许修改密码
        return Boolean.parseBoolean(result);
    }
}