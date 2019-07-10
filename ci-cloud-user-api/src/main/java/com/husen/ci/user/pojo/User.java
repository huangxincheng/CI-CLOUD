package com.husen.ci.user.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


/***
 @Author:MrHuang
 @Date: 2019/6/4 17:34
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
public class User implements Serializable {

    private String userId;

    private String userName;

    private String password;

    private Integer userStatus;

    private LocalDateTime userCreateTime;

    private LocalDateTime userActiveTime;
}
