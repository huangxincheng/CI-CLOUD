package com.husen.ci.sso.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/***
 @Author:MrHuang
 @Date: 2019/7/9 10:37
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
public class SsoRsp implements Serializable {

    private int status;

    private String msg;

}
