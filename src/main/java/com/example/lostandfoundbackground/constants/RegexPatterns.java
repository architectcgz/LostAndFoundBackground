package com.example.lostandfoundbackground.constants;

public abstract class RegexPatterns {

    /*
        用户名正则
     */

    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]{4,10}$";

    /**
     * 手机号正则
     */
    public static final String PHONE_REGEX = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

    /*
    密码正则表达式 必须包含大小写字母和特殊符号，至少8位
     */
    public static final String PASSWORD_REGEX = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,20}$";
    /**
     * 邮箱正则
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * 验证码正则, 6位数字或字母
     */
    public static final String VERIFY_CODE_REGEX = "^[a-zA-Z\\d]{6}$";

}
