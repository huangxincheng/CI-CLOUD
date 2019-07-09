package com.husen.ci.response;

import com.husen.ci.sso.response.SsoRsp;
import lombok.Data;
import lombok.experimental.Accessors;

/***
 @Author:MrHuang
 @Date: 2019/7/9 10:41
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
public class SsoLoginRsp extends SsoRsp {

    private String tokenSessionId;
}
