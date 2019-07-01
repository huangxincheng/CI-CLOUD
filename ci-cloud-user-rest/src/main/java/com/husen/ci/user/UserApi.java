package com.husen.ci.user;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/saveUser/{userName}")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public GlobalApiResponse saveUser(@PathVariable String userName) {
        User user = new User().setUserName(userName);
        return GlobalApiResponse.toSuccess(userService.createUser(user));
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
         return GlobalApiResponse.toSuccess(MqUtils.syncSend(topic, tags, msg));
    }

    public GlobalApiResponse defaultFallback(Throwable throwable) {
        return GlobalApiResponse.toFail(GlobalApiCode.SERVER_HYSTRIX_UNKONW_CODE, GlobalApiCode.SERVER_HYSTRIX_UNKNOW_CODE_MSG, throwable);
    }
}
