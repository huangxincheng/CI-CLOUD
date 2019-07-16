package com.husen.ci.user.api;

import com.husen.ci.framework.api.GlobalApiCode;
import com.husen.ci.framework.api.GlobalApiRequest;
import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.mq.MqUtils;
import com.husen.ci.order.client.IOrderClient;
import com.husen.ci.order.pojo.Order;
import com.husen.ci.user.pojo.User;
import com.husen.ci.user.service.IUserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/***
 @Author:MrHuang
 @Date: 2019/6/25 15:14
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "用户相关API")
public class UserApi {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderClient orderClient;

    @ApiOperation(value="获取用户详细信息", notes="根据userId获取用户详细信息")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", paramType = "path")
    @GetMapping("/get/{userId}")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public GlobalApiResponse get(@PathVariable String userId) {
        return GlobalApiResponse.toSuccess(userService.getOneById(userId));
    }

    @ApiOperation(value="获取用户列表", notes="获取用户列表")
    @GetMapping("/getAll")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public GlobalApiResponse getAll() {
        return GlobalApiResponse.toSuccess(userService.getAll());
    }


    @ApiOperation(value="创建用户", notes="保存用户信息")
    @PostMapping("/saveUser")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public GlobalApiResponse saveUser(@RequestBody @ApiParam GlobalApiRequest<User> request) {
        return GlobalApiResponse.toSuccess(userService.createUser(request.getPayLoad()));
    }


    @ApiOperation(value="保存订单", notes="保存订单信息")
    @PostMapping("/saveOrder")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public @ApiParam GlobalApiResponse saveOrder(@RequestBody  @ApiParam GlobalApiRequest<Order> request) {
        return GlobalApiResponse.toSuccess(orderClient.saveOrder(request.getPayLoad()));
    }

    @ApiOperation(value="查询订单", notes="根据订单ID查询订单")
    @ApiImplicitParam(name = "orderNo", value = "订单ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryOrder/{orderNo}")
    @HystrixCommand(defaultFallback = "defaultFallback")
    public GlobalApiResponse queryOrder(@PathVariable Long orderNo) {
        return GlobalApiResponse.toSuccess(orderClient.getOrder(orderNo));
    }

    @GetMapping("/sendMQ/{topic}/{tags}/{msg}")
    @HystrixCommand(defaultFallback = "defaultFallback")
    @ApiOperation(value="更新信息", notes="根据url的id来指定更新用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topic", value = "MQ的topic", required = true, dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "tags", value = "MQ的tags", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "msg", value = "MQ的msg", required = true, dataType = "String", paramType = "path")
    })
    public GlobalApiResponse sendMQ(@PathVariable String topic, @PathVariable String tags, @PathVariable String msg) {
         return GlobalApiResponse.toSuccess(MqUtils.syncSendDelay(topic, tags, msg, 2));
    }

    public GlobalApiResponse defaultFallback(Throwable throwable) {
        return GlobalApiResponse.toFail(GlobalApiCode.SERVER_HYSTRIX_UNKONW_CODE, GlobalApiCode.SERVER_HYSTRIX_UNKNOW_CODE_MSG, throwable);
    }
}
