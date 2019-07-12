package com.husen.ci.framework.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/***
 @Author:MrHuang
 @Date: 2019/6/14 11:38
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * toast提示异常，这种错误一般是提示，认为非异常
     * @param e
     * @return
     */
    @ExceptionHandler(GlobalToastException.class)
    @ResponseStatus(code = HttpStatus.OK)
    public GlobalApiResponse handlerGlobalToastException(GlobalToastException e) {
        return GlobalApiResponse.toSuccess(GlobalApiCode.GLOBAL_TOAST_CODE, e.getMessage());
    }


    /**
     * call异常， 这种错误一般是内部系统调用返回不是预期。
     * @param e
     * @return
     */
    @ExceptionHandler(GlobalCallException.class)
    @ResponseStatus(code = HttpStatus.OK)
    public GlobalApiResponse handlerGlobalCallException(GlobalCallException e) {
        return GlobalApiResponse.toFail(GlobalApiCode.GLOBAL_CALL_CODE, GlobalApiCode.GLOBAL_CALL_CODE_MSG, e);
    }


    /**
     * 全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.OK)
    public GlobalApiResponse handlerException(Exception e) {
        return GlobalApiResponse.toFail(GlobalApiCode.UNKNOW_CODE, GlobalApiCode.UNKONW_CODE_MSG, e);
    }


}
