package com.husen.ci.user;

import com.husen.ci.framework.api.GlobalApiRequest;
import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.order.client.OrderClient;
import com.husen.ci.order.pojo.Order;
import com.husen.ci.user.pojo.User;
import com.husen.ci.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

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
    public GlobalApiResponse<User> get(@PathVariable String userId) {
        return GlobalApiResponse.toSuccess(userService.getOneById(userId));
    }

    @RequestMapping("/getAll")
    public GlobalApiResponse<Collection<User>> getAll() {
        return GlobalApiResponse.toSuccess(userService.getAll());
    }

    @RequestMapping("/saveUser/{userName}")
    public GlobalApiResponse<User> saveUser(@PathVariable String userName) {
        User user = new User().setUserName(userName);
        return GlobalApiResponse.toSuccess(userService.createUser(user));
    }

    @RequestMapping("/saveOrder")
    public GlobalApiResponse<Boolean> saveOrder(@RequestBody GlobalApiRequest<Order> request) {
        return orderClient.saveOrder(request);
    }

    @RequestMapping("/queryOrder/{orderNo}")
    public GlobalApiResponse<Order> queryOrder(@PathVariable Long orderNo) {
        return orderClient.getOrder(orderNo);
    }
}
