package com.husen.ci.order.client;

import com.husen.ci.framework.api.GlobalApiRequest;
import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.order.pojo.Order;
import org.springframework.web.bind.annotation.*;

/***
 @Author:MrHuang
 @Date: 2019/7/5 11:48
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RequestMapping("/client/v1/order")
public interface IOrderClientService {

    /**
     * 获取订单详情
     * @param orderNo
     * @return
     */
    @GetMapping("/get/{orderNo}")
    Order getOrder(@PathVariable("orderNo") Long orderNo);

    /**
     * 保存订单
     * @return
     */
    @PostMapping("/save")
    boolean saveOrder(@RequestBody Order order);
}
