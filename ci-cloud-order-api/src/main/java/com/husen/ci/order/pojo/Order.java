package com.husen.ci.order.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/***
 @Author:MrHuang
 @Date: 2019/6/4 20:04
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
public class Order implements Serializable {
    private Long userId;

    private Long orderNo;

    private String orderTitle;

    private String orderRemake;

    /**
     * 单位分
     */
    private Integer orderAmount;
}
