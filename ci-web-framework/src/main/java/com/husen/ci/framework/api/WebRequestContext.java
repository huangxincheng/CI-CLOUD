package com.husen.ci.framework.api;

import lombok.Data;
import lombok.experimental.Accessors;

/***
 @Author:MrHuang
 @Date: 2019/6/24 10:50
 @DESC: TODO
 @VERSION: 1.0
 ***/
public class WebRequestContext {

    @Data
    @Accessors(chain = true)
    public static class WebRequestBean {

        private String serverIp;

        private String clientIp;

        private long clientReqTime;

        private String traceId;

        private String spanId;

        private String tokenId;

        private String clientReqType;
    }

    private static final ThreadLocal<WebRequestBean> HOLDER = new ThreadLocal<>();

    public static WebRequestBean getContext() {
        return HOLDER.get();
    }

    protected static void setContext(WebRequestBean bean) {
        HOLDER.set(bean);
    }

    protected static void removeContext() {
        HOLDER.remove();
    }
}
