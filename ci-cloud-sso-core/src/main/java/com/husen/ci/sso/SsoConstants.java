package com.husen.ci.sso;

/***
 @Author:MrHuang
 @Date: 2019/7/9 14:39
 @DESC: TODO
 @VERSION: 1.0
 ***/
public class SsoConstants {

    /**
     * 请求头中的用户标识Token
     */
    public static final String RQ_HEADER_TOEKN_SESSION_ID = "TOKEN_SESSION_ID";

    /**
     * 请求头中用户认证失败重定向URL
     */
    public static final String RQ_HEADER_REDIRECT_URL = "REDIRECT_URL";

    /**
     * 请求头中调整用户认证
     */
    public static final String RQ_PASS_TOKEN_AUTH = "PASS_TOKEN_AUTH";
}
