package com.husen.ci.order.service;

import com.husen.ci.order.pojo.Order;

/***
 @Author:MrHuang
 @Date: 2019/6/25 13:11
 @DESC: TODO
 @VERSION: 1.0
 ***/
public interface IOrderService {

    boolean saveOrder(Order order);

    Order queryOrder(Long orderNo);
}
