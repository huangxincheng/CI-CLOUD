package com.husen.ci.order.client;

import com.husen.ci.framework.api.GlobalApiRequest;
import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.framework.common.ProjectCommon;
import com.husen.ci.order.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/***
 @Author:MrHuang
 @Date: 2019/6/25 11:22
 @DESC: TODO
 @VERSION: 1.0
 ***/
@FeignClient(name = ProjectCommon.ORDER_SERVICE)
public interface OrderClient {

    /**
     * 获取订单详情
     * @param orderNo
     * @return
     */
    @GetMapping("api/v1/order/get/{orderNo}")
    GlobalApiResponse<Order> getOrder(@PathVariable("orderNo") Long orderNo);

    /**
     * 保存订单
     * @param request
     * @return
     */
    @PostMapping("api/v1/order/save")
    GlobalApiResponse saveOrder(@RequestBody GlobalApiRequest request);
}
