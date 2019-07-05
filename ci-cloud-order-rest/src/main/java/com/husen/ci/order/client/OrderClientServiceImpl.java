package com.husen.ci.order.client;

import com.husen.ci.order.pojo.Order;
import com.husen.ci.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/***
 @Author:MrHuang
 @Date: 2019/7/5 11:52
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RestController
public class OrderClientServiceImpl implements IOrderClientService {

    @Autowired
    private IOrderService orderService;

    @Override
    public Order getOrder(Long orderNo) {
        return orderService.queryOrder(orderNo);
    }

    @Override
    public boolean saveOrder(Order order) {
        return orderService.saveOrder(order);
    }
}
