package com.husen.ci.framework.api;

/***
 @Author:MrHuang
 @Date: 2019/7/12 15:02
 @DESC: TODO GlobalCallException
 @VERSION: 1.0
 ***/
public class GlobalCallException extends RuntimeException {

    public GlobalCallException() {
        super();
    }


    public GlobalCallException(String message) {
        super(message);
    }

    public GlobalCallException(String message, Throwable cause) {
        super(message, cause);
    }
}