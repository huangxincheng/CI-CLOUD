package com.husen.ci.gateway;

import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.framework.json.JSONUtils;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;

/***
 @Author:MrHuang
 @Date: 2019/6/27 10:57
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
class GatewayHandlerCommon {

    public static final String GATEWAY_START_TIME = qualify("GATEWAY_START_TIME");

    public static final String GATEWAY_REQUEST_BODY = qualify("GATEWAY_REQUEST_BODY");

    public static final String GATEWAY_RESPONSE_RESULT = qualify("GATEWAY_RESPONSE_RESULT");

    private static final String PASS_AUTH = "I_PASS_AUTH";

    private static final String AUTH_TOKEN = "AUTH_TOKEN";

    /**
     * 增加前缀
     * @param attr
     * @return
     */
    private static String qualify(String attr) {
        return GatewayHandlerCommon.class.getName() + "." + attr;
    }

    /**
     * 将result转换成DataBuffer
     * @param response
     * @param result
     * @return
     */
    private static DataBuffer getDataBuffer(ServerHttpResponse response, GlobalApiResponse result) {
        return response.bufferFactory().wrap(JSONUtils.object2Bytes(result));
    }
    /**
     * 将result转换成DataBuffer
     * @param result
     * @return
     */
    private static DataBuffer getDataBuffer(Object result) {
        byte[] bytes = JSONUtils.object2Bytes(result);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

    /**
     * 将result转换成DataBuffer
     * @param result
     * @return
     */
    private static DataBuffer getDataBuffer(String result) {
        byte[] bytes = result.getBytes(StandardCharsets.UTF_8);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

    /**
     * 处理前存入请求时间 替换Request Body数据请求
     * @param exchange
     */
    static void handlerPre(ServerWebExchange exchange) {
        exchange.getAttributes().put(GATEWAY_START_TIME, System.currentTimeMillis());
    }

    /**
     * 处理后打印日志
     * @param exchange
     */
    static void handlerPost(ServerWebExchange exchange) {
        Long startTime = exchange.getAttribute(GATEWAY_START_TIME);
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        LinkedHashSet<URI> originalUris = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        Object body = exchange.getAttribute(GATEWAY_REQUEST_BODY);
        if (startTime != null) {
            log.info("{\"logType\":{},\"method\":{},\"uri\":{},\"originUri\":{},\"body\":{},\"route\":{},\"timeOff\":{}}",
                    "Rsp Data Info",
                    exchange.getRequest().getMethodValue(),
                    exchange.getRequest().getURI().toString(),
                    Optional.ofNullable(originalUris).map(Objects::toString).orElse(""),
                    Optional.ofNullable(body).orElse(""),
                    Optional.ofNullable(route).map(Route::getId).orElse(""),
                    (System.currentTimeMillis() - startTime) + "ms"
            );
        }
    }




}
