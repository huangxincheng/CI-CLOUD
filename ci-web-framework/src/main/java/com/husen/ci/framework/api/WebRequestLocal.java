package com.husen.ci.framework.api;

import lombok.Data;
import lombok.experimental.Accessors;

/***
 @Author:MrHuang
 @Date: 2019/6/24 10:52
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
public  class WebRequestLocal {

    private String serverIp;

    private String clientIp;

    private long clientReqTime;

    private String traceId;

    private String spanId;

    private String tokenId;

    private String clientReqType;
}