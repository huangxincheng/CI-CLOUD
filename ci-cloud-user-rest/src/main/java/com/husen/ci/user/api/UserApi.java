package com.husen.ci.user.api;

import com.husen.ci.framework.api.GlobalApiCode;
import com.husen.ci.framework.api.GlobalApiRequest;
import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.mq.MqUtils;
import com.husen.ci.order.client.OrderClient;
import com.husen.ci.order.pojo.Order;
import com.husen.ci.user.pojo.User;
import com.husen.ci.user.service.IUserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/***
 @Author:MrHuang
 @Date: 2019/6/25 15:14
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RestController
@RequestMapping("/api/v1/user")
public class UserApi {

    @Autowired
    private IUserService userService;

    @Autowired
    private OrderClient orderClient;

    @RequestMapping("/get/{userId}")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public GlobalApiResponse get(@PathVariable String userId) {
        if ("1".equals(userId)) {
            int i = 1 / 0;
        }
        return GlobalApiResponse.toSuccess(userService.getOneById(userId));
    }

    @RequestMapping("/getAll")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public GlobalApiResponse getAll() {
        return GlobalApiResponse.toSuccess(userService.getAll());
    }


    @PostMapping("/saveUser")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public GlobalApiResponse saveUser(@RequestBody GlobalApiRequest<User> request) {
        return GlobalApiResponse.toSuccess(userService.createUser(request.getPayLoad()));
    }


    @RequestMapping("/saveOrder")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public GlobalApiResponse saveOrder(@RequestBody GlobalApiRequest<Order> request) {
        return orderClient.saveOrder(request);
    }

    @RequestMapping("/queryOrder/{orderNo}")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public GlobalApiResponse queryOrder(@PathVariable Long orderNo) {
        return orderClient.getOrder(orderNo);
    }

    @RequestMapping("/sendMQ/{topic}/{tags}/{msg}")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public GlobalApiResponse sendMQ(@PathVariable String topic, @PathVariable String tags, @PathVariable String msg) {
         return GlobalApiResponse.toSuccess(MqUtils.syncSendDelay(topic, tags, msg, 2));
    }

    public GlobalApiResponse defaultFallback(Throwable throwable) {
        return GlobalApiResponse.toFail(GlobalApiCode.SERVER_HYSTRIX_UNKONW_CODE, GlobalApiCode.SERVER_HYSTRIX_UNKNOW_CODE_MSG, throwable);
    }
}
