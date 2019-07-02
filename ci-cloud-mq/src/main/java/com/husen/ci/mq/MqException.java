package com.husen.ci.mq;

/***
 @Author:MrHuang
 @Date: 2019/7/2 10:08
 @DESC: TODO
 @VERSION: 1.0
 ***/
public class MqException extends RuntimeException {

    public MqException() {
        super();
    }

    public MqException(String message) {
        super(message);
    }

    public MqException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqException(Throwable cause) {
        super(cause);
    }
}
