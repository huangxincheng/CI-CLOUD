package com.husen.ci.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 @Author:MrHuang
 @Date: 2019/6/25 21:42
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Configuration
@Slf4j
public class GatewayFilterConfig {

    @Bean
    public GlobalGatewayFilterFactory globalGatewayFilterFactory() {
        return new GlobalGatewayFilterFactory();
    }

    @Bean
    public AuthGatewayFilterFactory authGatewayFilterFactory() {
        return new AuthGatewayFilterFactory();
    }
}
