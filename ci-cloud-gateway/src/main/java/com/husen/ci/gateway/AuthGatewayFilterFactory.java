package com.husen.ci.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/***
 @Author:MrHuang
 @Date: 2019/6/28 14:26
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

    /**
     * constructor
     */
    public AuthGatewayFilterFactory(){
        // 这里需要将自定义的config传过去，否则会报告ClassCastException
        super(AuthGatewayFilterFactory.Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (!GlobalHandlerCommon.isPassAuth(exchange) && !GlobalHandlerCommon.isAuth(exchange)) {
                return GlobalHandlerCommon.returnAuthFail(exchange);
            }
            return chain.filter(exchange);
        });
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList();
    }

    /**
     * 自定义的config类，用来设置传入的参数
     */
    public static class Config {

    }
}
