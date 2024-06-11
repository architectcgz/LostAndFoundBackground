package com.example.lostandfoundbackground.constants;

/**
 * @author archi
 */
public abstract class RegexPatterns {

    /*
        用户名正则 1-10个中英文字符或数字
     */

    public static final String USERNAME_REGEX = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,10}$";;

    /**
     * 手机号正则
     */
    public static final String PHONE_REGEX = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

    /*
    密码正则表达式 必须包含大小写字母和特殊符号，至少8位,最多20位
     */
    public static final String PASSWORD_REGEX = "^(?![\\d]+$)(?![a-z]+$)(?![A-Z]+$)(?![!#$%^&*]+$)[\\da-zA-Z!#$%^&*]{8,16}$";;
    /**
     * 邮箱正则
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * 验证码正则, 6位数字或字母
     */
    public static final String VERIFY_CODE_REGEX = "^[a-zA-Z\\d]{6}$";

}
