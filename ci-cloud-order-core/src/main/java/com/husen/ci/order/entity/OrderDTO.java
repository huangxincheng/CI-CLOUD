package com.husen.ci.order.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/***
 @Author:MrHuang
 @Date: 2019/6/4 20:04
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
@Document(collection = "order")
public class OrderDTO implements Serializable {

    @Field
    private Long userId;

    /**
     * 如果使用@Id 值为null为默认生成string的唯一值
     * 如果不为null，使用该值，必须唯一
     */
    @Id
    private Long orderNo;

    @Field("order_title")
    private String orderTitle;

    /**
     * 忽略存储到mongo
     */
    @Transient
    private String orderRemake;

    /**
     * 单位分
     */
    @Field
    private Integer orderAmount;

    @Field
    private String host;
}
