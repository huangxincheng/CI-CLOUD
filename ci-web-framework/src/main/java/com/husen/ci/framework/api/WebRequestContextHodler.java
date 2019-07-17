package com.husen.ci.framework.api;

/***
 @Author:MrHuang
 @Date: 2019/6/24 10:50
 @DESC: TODO
 @VERSION: 1.0
 ***/
public class WebRequestContextHodler {

    private static final ThreadLocal<WebRequestLocal> CONTEXT = new ThreadLocal<>();

    public static WebRequestLocal getWebRequest() {
        return CONTEXT.get();
    }

    protected static void setWebRequest(WebRequestLocal bean) {
        CONTEXT.set(bean);
    }

    protected static void removeWebRequest() {
        CONTEXT.remove();
    }
}
