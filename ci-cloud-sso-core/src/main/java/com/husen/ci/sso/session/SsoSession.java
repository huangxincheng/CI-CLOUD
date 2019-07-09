package com.husen.ci.sso.session;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/***
 @Author:MrHuang
 @Date: 2019/7/9 10:20
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
public class SsoSession implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * tokenSessionId
     */
    private String tokenSessionId;

    /**
     * 用户刷新Sessiond的时间戳
     */
    private long freshTime;

    /**
     * redis设置的失效秒数
     */
    private int expireSecond;
}
