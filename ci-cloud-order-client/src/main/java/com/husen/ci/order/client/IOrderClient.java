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
@FeignClient(name = ProjectCommon.ORDER_SERVICE, fallbackFactory = OrderClientFallbackFactory.class)
public interface IOrderClient extends IOrderClientService{

}
