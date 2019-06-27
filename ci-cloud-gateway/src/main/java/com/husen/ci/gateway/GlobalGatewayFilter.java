package com.husen.ci.gateway;

import com.husen.ci.framework.api.GlobalApiCode;
import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.framework.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/***
 @Author:MrHuang
 @Date: 2019/6/25 20:56 此类作用已被GlobalGatewayFilterFactory替换
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
@Deprecated
public class GlobalGatewayFilter implements GlobalFilter, Ordered {

    private static final String GATEWAY_START_TIME = "GATEWAY_START_TIME";

    private static final String PASS_AUTH = "I_PASS_AUTH";

    /**
     * 判断是否Pass认证
     * @param exchange
     * @return
     */
    private boolean isPassAuth(ServerWebExchange exchange) {
        List<String> authList = exchange.getRequest().getHeaders().get(PASS_AUTH);
        if (authList != null && authList.size() > 0) {
            return true;
        }
        String authParam = exchange.getRequest().getQueryParams().getFirst(PASS_AUTH);
        if (authParam != null) {
            return true;
        }
        return false;
    }

    /**
     * 封装返回值
     *
     * @param response
     * @param result
     * @return
     */
    private DataBuffer getBodyBuffer(ServerHttpResponse response, GlobalApiResponse result) {
        return response.bufferFactory().wrap(JSONUtils.object2Bytes(result));
    }

    private void handlerPre(ServerWebExchange exchange) {
        exchange.getAttributes().put(GATEWAY_START_TIME, System.currentTimeMillis());
    }

    private void handlerPost(ServerWebExchange exchange) {
        Long startTime = exchange.getAttribute(GATEWAY_START_TIME);
        if (startTime != null) {
            Flux<DataBuffer> body = exchange.getRequest().getBody();
            AtomicReference<String> bodyRef = new AtomicReference<>();
            body.subscribe(buffer -> {
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
                DataBufferUtils.release(buffer);
                bodyRef.set(charBuffer.toString());
            });
            log.info("{} {} {} {} {}",
                    "Gateway Response ",
                    exchange.getRequest().getMethodValue(),
                    exchange.getRequest().getURI().toString(),
                    bodyRef.get(),
                    (System.currentTimeMillis() - startTime) + "ms"
            );
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        this.handlerPre(exchange);
        boolean passAuth = this.isPassAuth(exchange);
        if (!passAuth) {
            // 不能pass的直接失败
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return exchange.getResponse()
                    .writeWith(
                         Flux.just(this.getBodyBuffer(exchange.getResponse(),
                                 GlobalApiResponse.toFail(GlobalApiCode.UNAUTH_CODE, GlobalApiCode.UNAUTH_CODE_MSG)))
                    ).then(Mono.fromRunnable(() -> {
                            this.handlerPost(exchange);
                    }));
        }
        return chain.filter(exchange).then(
            Mono.fromRunnable(() -> {
                this.handlerPost(exchange);
            })
        );
    }



    /**
     * 值越大则优先级越低。
     * @return
     */
    @Override
    public int getOrder() {
        return FilterOrder.GLOBAL_FILTER_ORDER;
    }




}
