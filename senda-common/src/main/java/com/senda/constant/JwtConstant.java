package com.senda.constant;

public class JwtConstant {

    //自定义密钥，至少 256 位
    public static final String SECRET_KEY = "sendaDeliveryExampleSuperSecretKeyForHS256Algorithm";

    //过期时间，单位：毫秒（此处为 1 *  小时）
    public static final Integer HOUR = 24;
    public static final Long EXPIRATION_TIME = 3600000L * HOUR;

}
