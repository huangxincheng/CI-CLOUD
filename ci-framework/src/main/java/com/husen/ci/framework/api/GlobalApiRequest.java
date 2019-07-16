package com.husen.ci.framework.api;

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
public class GlobalApiRequest<T> implements Serializable {

    private String source;

    private T payLoad;
}
