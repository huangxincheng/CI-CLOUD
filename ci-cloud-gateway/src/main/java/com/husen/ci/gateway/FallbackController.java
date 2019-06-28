package com.husen.ci.gateway;

import com.husen.ci.framework.api.GlobalApiCode;
import com.husen.ci.framework.api.GlobalApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/***
 @Author:MrHuang
 @Date: 2019/6/26 11:54
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RequestMapping("/fallbackCmd")
@RestController
@Slf4j
public class FallbackController {


    @RequestMapping
    public GlobalApiResponse fallback(ServerWebExchange exchange) {
        // HystrixGatewayFilterFactory 将异常信息存入exchange的attribute中
        log.error("Global Resposne" ,(Throwable)exchange.getAttributes().get(ServerWebExchangeUtils.HYSTRIX_EXECUTION_EXCEPTION_ATTR));
        GatewayHandlerCommon.handlerPost(exchange);
        return GlobalApiResponse.toFail(GlobalApiCode.GATEWAY_FALLBACK_CODE, GlobalApiCode.GATEWAY_FALLBACK_CODE_MSG);
    }
}
