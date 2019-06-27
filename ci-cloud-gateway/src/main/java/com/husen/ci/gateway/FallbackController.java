package com.husen.ci.gateway;

import com.husen.ci.framework.api.GlobalApiCode;
import com.husen.ci.framework.api.GlobalApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

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

    private static final String HYSTRIX_EXCEPTION_ATTRIBUTE = "org.springframework.cloud.gateway.support.ServerWebExchangeUtils.hystrixExecutionException";

    @RequestMapping
    public GlobalApiResponse fallback(ServerWebExchange exchange) {
        // HystrixGatewayFilterFactory 将异常信息存入exchange的attribute中
        log.error("Global Resposne" ,(Throwable)exchange.getAttributes().get(HYSTRIX_EXCEPTION_ATTRIBUTE));
        GlobalHandlerCommon.handlerPost(exchange);
        return GlobalApiResponse.toFail(GlobalApiCode.GATEWAY_FALLBACK_CODE, GlobalApiCode.GATEWAY_FALLBACK_CODE_MSG);
    }
}
