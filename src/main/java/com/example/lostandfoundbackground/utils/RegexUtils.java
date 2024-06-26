package com.example.lostandfoundbackground.utils;

import cn.hutool.core.util.StrUtil;
import com.example.lostandfoundbackground.constants.RegexPatterns;

/**
 * @author archi
 */
public class RegexUtils {
    /*
        用户名是否为有效格式
     */

    public static boolean isUserNameValid(String name){
        return match(name,RegexPatterns.USERNAME_REGEX);
    }

    /**
     * 是否是有效手机格式
     * @param phone 要校验的手机号
     * @return true:符合，false：不符合
     */
    public static boolean isPhoneValid(String phone){
        return match(phone, RegexPatterns.PHONE_REGEX);
    }

    /**
     * 是否位有效密码格式
     * @param password 要检验的密码
     * @return true:符合，false: 不符合
     */

    public static boolean isPasswordValid(String password){
        return match(password,RegexPatterns.PASSWORD_REGEX);
    }

    /**
     * 是否是有效邮箱格式
     * @param email 要校验的邮箱
     * @return true:符合，false：不符合
     */
    public static boolean isEmailValid(String email){
        return match(email, RegexPatterns.EMAIL_REGEX);
    }

    /**
     * 是否是有效验证码格式
     * @param code 要校验的验证码
     * @return true:符合，false：不符合
     */
    public static boolean isCodeValid(String code){
        return match(code, RegexPatterns.VERIFY_CODE_REGEX);
    }

    // 校验是否符合正则格式
    private static boolean match(String str, String regex){
        if (StrUtil.isBlank(str)) {
            return false;
        }
        return str.matches(regex);
    }
}
