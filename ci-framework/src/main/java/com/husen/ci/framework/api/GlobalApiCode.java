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
     */
    int SUCCESS_CODE = 0;

    /**
     * 成功响应码信息
     */
    String SUCCESS_CODE_MSG = "SUCCESS";

    /**
     * 鉴权失败响应码
     */
    int UNAUTH_CODE = 401;

    /**
     * 鉴权失败响应码信息
     */
    String UNAUTH_CODE_MSG = "鉴权失败";



    /**
     * 网关Fallback错误响应码
     */
    int GATEWAY_FALLBACK_CODE = -10000;

    /**
     * 网关Fallback错误响应码信息
     */
    String GATEWAY_FALLBACK_CODE_MSG = "网关系统未知异常.";


    /**
     * 业务未知错误响应码
     */
    int UNKNOW_CODE = -20000;

    /**
     * 业务未知错误响应码信息
     */
    String UNKONW_CODE_MSG = "业务系统未知异常.";

    /**
     * GlobalToastException响应码
     * Msg:  e.getMeesage
     */
    int GLOBAL_TOAST_CODE = -20001;

    String GLOBAL_TOAST_CODE_MSG = "业务系统消息提示异常.";


    /**
     * GlobalCallException响应码
     */
    int GLOBAL_CALL_CODE = -20002;

    /**
     * GlobalCallException响应码信息
     */
    String GLOBAL_CALL_CODE_MSG = "业务系统内部调用链路异常.";







    /**
     * Server端业务Hystrix未知错误响应码
     */
    int SERVER_HYSTRIX_UNKONW_CODE = -30000;

    /**
     * Server端业务Hystrix未知错误响应码信息
     */
    String SERVER_HYSTRIX_UNKNOW_CODE_MSG = "SERVER_HYSTRIX_UNKNOW";

    /**
     * Feign端业务Hystrix未知错误响应码
     */
    int FEIGN_HYSTRIX_UNKNOW_CODE = -30001;

    /**
     * Feign端业务Hystrix未知错误响应码信息
     */
    String FEIGN_HYSTRIX_UNKNOW_CODE_MSG = "FEIGN_HYSTRIX_UNKNOW";
}
