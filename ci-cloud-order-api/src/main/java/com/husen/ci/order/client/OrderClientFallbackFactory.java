package com.husen.ci.order.client;

import com.husen.ci.framework.api.GlobalApiRequest;
import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.order.pojo.Order;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import static com.husen.ci.framework.api.GlobalApiCode.FEIGN_HYSTRIX_UNKNOW_CODE;
import static com.husen.ci.framework.api.GlobalApiCode.FEIGN_HYSTRIX_UNKNOW_CODE_MSG;

/***
 @Author:MrHuang
 @Date: 2019/6/27 16:21
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Component
public class OrderClientFallbackFactory implements FallbackFactory<OrderClient> {
    @Override
    public OrderClient create(Throwable throwable) {
        return new OrderClient() {
            @Override
            public GlobalApiResponse<Order> getOrder(Long orderNo) {
                return GlobalApiResponse.toFail(FEIGN_HYSTRIX_UNKNOW_CODE, FEIGN_HYSTRIX_UNKNOW_CODE_MSG, throwable);
            }

            @Override
            public GlobalApiResponse<Boolean> saveOrder(GlobalApiRequest request) {
                return GlobalApiResponse.toFail(FEIGN_HYSTRIX_UNKNOW_CODE, FEIGN_HYSTRIX_UNKNOW_CODE_MSG, throwable);
            }
        };
    }
}
