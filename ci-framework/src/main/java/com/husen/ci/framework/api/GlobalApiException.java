package com.husen.ci.framework.api;

/***
 @Author:MrHuang
 @Date: 2019/6/13 22:49
 @DESC: TODO GlobalApiException
 @VERSION: 1.0
 ***/
public class GlobalApiException extends RuntimeException {

    public GlobalApiException() {
        super();
    }


    public GlobalApiException(String message) {
        super(message);
    }

    public GlobalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
