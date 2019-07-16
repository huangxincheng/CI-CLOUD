package com.husen.ci.user.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "User", description = "用户信息")
public class User implements Serializable {

    @ApiModelProperty(value = "用户ID", required = true)
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "用户状态")
    private Integer userStatus;

    @ApiModelProperty(value = "用户创建时间")
    private LocalDateTime userCreateTime;

    @ApiModelProperty(value = "用户激活时间")
    private LocalDateTime userActiveTime;
}
