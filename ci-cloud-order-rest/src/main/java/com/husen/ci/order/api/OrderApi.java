package com.husen.ci.order.api;

import com.husen.ci.framework.api.GlobalApiRequest;
import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.order.pojo.Order;
import com.husen.ci.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/***
 @Author:MrHuang
 @Date: 2019/6/25 13:46
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RestController
@RequestMapping("/api/v1/order")
public class OrderApi {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/get/{orderNo}")
    public GlobalApiResponse<Order> getOrder(@PathVariable Long orderNo) {
        Order order = orderService.queryOrder(orderNo);
        return GlobalApiResponse.toSuccess(order);
    }

    @PostMapping("/save")
    public GlobalApiResponse<Boolean> saveOrder(@RequestBody GlobalApiRequest<Order> request) {
        Order order = request.getPayLoad();
        return GlobalApiResponse.toSuccess(orderService.saveOrder(order));
    }
}
