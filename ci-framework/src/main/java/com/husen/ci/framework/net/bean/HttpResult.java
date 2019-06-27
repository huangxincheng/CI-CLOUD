package com.husen.ci.framework.net.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/***
 @Author:MrHuang
 @Date: 2019/6/6 17:18
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
public class HttpResult implements Serializable {

    public static final int SUCCESS_CODE = 200;

    /**
     * 状态码
     */
    private int code;

    /**
     * 响应内容
     */
    private String content;

    /**
     * 响应消息头
     */
    private Map<String, List<String>> headers;

    /**
     * 判断是否请求成功
     *
     * @return
     */
    public boolean ok() {
        return SUCCESS_CODE == this.code;
    }


    public String firsrHeaderValue(String headerName) {
        if (this.headers == null) {
            return null;
        }
        if (this.headers.get(headerName) == null) {
            return null;
        }
        return this.headers.get(headerName).get(0);
    }


}
