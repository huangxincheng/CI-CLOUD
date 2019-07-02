package com.husen.ci.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/***
 @Author:MrHuang
 @Date: 2019/6/26 18:14
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
public class GlobalGatewayFilterFactory extends AbstractGatewayFilterFactory<GlobalGatewayFilterFactory.Config> {

    /**
     * 定义可以再yaml中声明的属性变量
     */
    private static final String HANDLER = "handler";

    /**
     * constructor
     */
    public GlobalGatewayFilterFactory(){
        // 这里需要将自定义的config传过去，否则会报告ClassCastException
        super(Config.class);
    }


    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(HANDLER);
    }


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            GatewayHandlerCommon.handlerPre(exchange);
            return chain.filter(exchange).then(Mono.fromRunnable(() ->  {
                GatewayHandlerCommon.handlerPost(exchange);
            }));
        });
    }

    /**
     * 自定义的config类，用来设置传入的参数
     */
    public static class Config {

        private boolean handler;

        public boolean isHandler() {
            return handler;
        }

        public void setHandler(boolean handler) {
            this.handler = handler;
        }
    }
}
