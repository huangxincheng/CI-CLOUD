package com.husen.ci.framework.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/***
 @Author:MrHuang
 @Date: 2019/6/13 14:57
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
@ApiModel("统一请求Req Data")
public class GlobalApiRequest<T> implements Serializable {

    @ApiModelProperty(value = "请求来源", required = true)
    private String source;

    @ApiModelProperty(value = "请求内容")
    private T payLoad;
}
