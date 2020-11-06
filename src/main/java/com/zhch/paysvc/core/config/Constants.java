package com.zhch.paysvc.core.config;

/**
 * @author lumos
 */
public class Constants {

    public static final String OK = "ok";
    public static final String SUCCESS = "0";
    /**
     * 初始化参数异常
     */
    public static final String COLUMNS = "columns";
    public static final String PAGE_SIZE = "pagesize";
    public static final String CURRENT = "current";
    public static final String STRING_BREAK = ",";
    public static final String UTF_8 = "UTF-8";
    public static final String JWT = "jwt";
    public static final String TOKEN = "token";
    public static final String SERIAL = "serial";
    /**
     * jwt密码, 用于解密jwt串
     */
    public static final String JWT_SECRET = "xingchenyefeng@qq.com";
    public static final String ACCOUNT_ID = "accountId";
    public static final String CLIENT_TYPE = "client-type";
    public static final String CHANNEL_TYPE = "channel-type";
    public static final String LOGIN_DATE_TIME = "loginDateTime";
    public static final String LOGIN_TOKEN_TEMPLATE = "lg:t:%s";
    /**
     * lg:id:用户id:c:clientType:t:channelType
     */
    public static final String LOGIN_ACCOUNT_TEMPLATE = "lg:id:%s";
    /**
     * 验证码redis key模板
     */
    public static final String VCODE_TEMPLATE = "vcode:module:%s:serial:%s";

    /**
     * 用户权限数据redis key模板
     */
    public static final String USER_PERMISSION_TEMPLATE = "ps:id:%s";

    public static final long ANONYMOUS_USER_ID = -1L;
    public static final String UNKNOWN = "unknown";
    public static final Object EMPTY_STRING = "";
    public static final String PRO_PROFILES = "pro";
    public static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    public static final String LOGIN_STATE = "state";

    public static final String MINIO = "minio";
    public static final String MINIO_ENDPOINT = "endpoint";
}
