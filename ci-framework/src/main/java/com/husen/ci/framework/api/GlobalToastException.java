package com.husen.ci.framework.api;

/***
 @Author:MrHuang
 @Date: 2019/6/13 22:49
 @DESC: TODO GlobalToastException
 @VERSION: 1.0
 ***/
public class GlobalToastException extends RuntimeException {

    public GlobalToastException() {
        super();
    }


    public GlobalToastException(String message) {
        super(message);
    }

    public GlobalToastException(String message, Throwable cause) {
        super(message, cause);
    }
}
