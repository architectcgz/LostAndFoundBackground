package com.example.lostandfoundbackground.constants;

/**
 * @author archi
 */
public class SystemConstants {
    public static final String IMAGE_UPLOAD_DIR = "D:\\lesson\\nginx-1.18.0\\html\\hmdp\\imgs\\";
    public static final String USER_NAME_PREFIX = "user_";
    public static final String ADMIN_NAME_PREFIX = "admin_";

    public static final String ROLE_USER = "USER";

    public static final String ROLE_ADMIN = "ADMIN";
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final int MAX_PAGE_SIZE = 10;

    //3 days
    public static final Long REFRESH_TOKEN_EXPIRATION = 259200000L;

    //2 hours
    public static final Long ACCESS_TOKEN_EXPIRATION = 7200000L;

    public static final Long REDIS_ONE_DAY_EXPIRATION = 1440L;
    public static final Long REDIS_THREE_DAYS_EXPIRATION = 4320L;

    public static final Long REDIS_ONE_WEEK_EXPIRATION = 10080L;
}
