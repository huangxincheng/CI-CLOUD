package com.husen.ci.gateway;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

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


    private static final String IGNORE = "ignore";

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
            if (!GatewayHandlerCommon.isPassAuth(exchange, config) && !GatewayHandlerCommon.isAuth(exchange)) {
                return GatewayHandlerCommon.returnAuthFail(exchange);
            }
            return chain.filter(exchange);
        });
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(IGNORE);
    }

    /**
     * 自定义的config类，用来设置传入的参数
     */
    @Data
    public static class Config {

        private List<ServiceRoute> ignore;

        private List<String> notAuthService;
    }

    /**
     * 自定义的ServiceRoute 用来设置Config中的配置
     */
    @Data
    public static class ServiceRoute {

        private String service;

        private List<String> uri;
    }
}
