package com.husen.ci.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

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
}
