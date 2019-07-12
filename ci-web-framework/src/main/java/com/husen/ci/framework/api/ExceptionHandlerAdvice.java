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

    @ExceptionHandler(GlobalApiException.class)
    @ResponseStatus(code = HttpStatus.OK)
    public GlobalApiResponse handlerGlobalApiException(GlobalApiException e) {
        return GlobalApiResponse.toSuccess(GlobalApiCode.BUSIN_ERROR_CODE, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.OK)
    public GlobalApiResponse handlerException(Exception e) {
        return GlobalApiResponse.toFail(e);
    }


}
