package com.husen.ci.framework.api;

/***
 @Author:MrHuang
 @Date: 2019/6/14 10:56
 @DESC: TODO 全局ApiCode码
 @VERSION: 1.0
 ***/
public interface GlobalApiCode {

    /**
     * 成功响应码
     * Msg : Success
     */
    int SUCCESS_CODE = 0;

    /**
     * 业务错误响应码
     * Msg: GlobalApiException e.getMeesage
     */
    int BUSIN_ERROR_CODE = 10000;

    /**
     * 网关Fallback错误响应码
     */
    int GATEWAY_FALLBACK_CODE = -10000;

    /**
     * 网关Fallback错误响应码信息
     */
    String GATEWAY_FALLBACK_CODE_MSG = "Gateway Fallback";


    /**
     * 业务未知错误响应码
     */
    int UNKNOW_CODE = -10001;

    /**
     * 业务未知错误响应码信息
     */
    String UNKONW_CODE_MSG = "BUSIN_UNKNOW";

    /**
     * 业务Hystrix未知错误响应码
     */
    int HYSTRIX_UNKNOW_CODE = -10003;

    /**
     * 业务Hystrix未知错误响应码信息
     */
    String HYSTRIX_UNKNOW_CODE_MSG = "HYSTRIX_UNKNOW";

    /**
     * 鉴权失败
     */
    int UNAUTH_CODE = -10002;

    /**
     * 鉴权失败
     */
    String UNAUTH_CODE_MSG = "鉴权失败";
}
