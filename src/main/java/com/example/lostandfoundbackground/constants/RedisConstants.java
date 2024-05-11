package com.example.lostandfoundbackground.constants;

public class RedisConstants {
    public static final String LOGIN_ADMIN_KEY = "login:admin:token:";
    public static final String LOGIN_ADMIN_PHONE = "login:admin:phone:";
    public static final Long LOGIN_ADMIN_TTL = 36000L;

    public static final String ADMIN_SMS_CODE_KEY = "admin:sms:code:";

    public static final String USER_SMS_CODE_KEY = "user:sms:code:";

    public static final Long LOGIN_CODE_TTL = 3L;
    public static final String LOGIN_USER_KEY = "login:user:token:";

    public static final String LOGIN_USER_PHONE = "login:user:phone:";
    public static final Long LOGIN_USER_TTL = 36000L;

    public static final String ADMIN_VERIFICATION_STATUS = "admin:verificationStatus:phone:";

    public static final String USER_VERIFICATION_STATUS = "user:verificationStatus:phone:";
}
