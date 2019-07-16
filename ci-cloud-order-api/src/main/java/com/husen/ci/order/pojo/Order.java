package com.husen.ci.order.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "Order", description = "订单信息")
public class Order implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "订单ID")
    private Long orderNo;

    @ApiModelProperty(value = "订单标题")
    private String orderTitle;

    @ApiModelProperty(value = "订单备注")
    private String orderRemake;

    /**
     * 单位分
     */
    @ApiModelProperty(value = "订单金额")
    private Integer orderAmount;
}
