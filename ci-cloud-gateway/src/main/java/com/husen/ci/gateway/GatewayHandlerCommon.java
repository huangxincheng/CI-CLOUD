package com.husen.ci.gateway;

import com.husen.ci.framework.api.GlobalApiCode;
import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.framework.auth.JwtUtils;
import com.husen.ci.framework.json.JSONUtils;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/***
 @Author:MrHuang
 @Date: 2019/6/27 10:57
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
class GatewayHandlerCommon {

    public static final String GATEWAY_START_TIME = qualify("GATEWAY_START_TIME");

    public static final String GATEWAY_BODY = qualify("GATEWAY_BODY");

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
     * 判断是否Pass认证
     * @param exchange
     * @return
     */
    static boolean isPassAuth(ServerWebExchange exchange, AuthGatewayFilterFactory.Config config) {
        List<String> authList = exchange.getRequest().getHeaders().get(PASS_AUTH);
        if (authList != null && authList.size() > 0) {
            return true;
        }
        String authParam = exchange.getRequest().getQueryParams().getFirst(PASS_AUTH);
        if (authParam != null) {
            return true;
        }
        // 获取当前调整的route
        return isIgnoreAuth(exchange, config);
    }

    /**
     * 是否是忽略认证
     * @param exchange
     * @param config
     * @return
     */
    private static boolean isIgnoreAuth(ServerWebExchange exchange, AuthGatewayFilterFactory.Config config) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);

        // 判断是否在不需要认证的服务列表中
        List<String> notAuthService = config.getNotAuthService();
        if (notAuthService != null && notAuthService.contains(route.getId())) {
            return true;
        }

        // 判断是否在不需要认证的请求uri中
        String rawPath = exchange.getRequest().getURI().getRawPath();
        List<AuthGatewayFilterFactory.ServiceRoute> ignore = config.getIgnore();
        if (ignore != null && ignore.size() > 0) {
            for (AuthGatewayFilterFactory.ServiceRoute serviceRoute : ignore) {
                if (StringUtils.equals(serviceRoute.getService(), route.getId())) {
                    for (String uri : serviceRoute.getUri()) {
                        if (rawPath.startsWith(uri)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否正常成功
     * @param exchange
     * @return
     */
    static boolean isAuth(ServerWebExchange exchange) {
        List<String> authTokenList = exchange.getRequest().getHeaders().get(AUTH_TOKEN);
        String authToken;
        if (authTokenList != null && authTokenList.size() > 0) {
             authToken = authTokenList.get(0);
        } else {
            authToken = exchange.getRequest().getQueryParams().getFirst(AUTH_TOKEN);
        }
        //TODO 这里只简单判断校验就行, 后面可以增加判断Redis中是否存在
        return JwtUtils.checkFormToken(authToken);
    }

    /**
     * 返回认证失败
     * @param exchange
     * @return
     */
    static Mono<Void> returnAuthFail(ServerWebExchange exchange) {
        // 不能pass的直接失败
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return exchange.getResponse()
                .writeWith(
                        Flux.just(GatewayHandlerCommon.getDataBuffer(exchange.getResponse(),
                                GlobalApiResponse.toFail(GlobalApiCode.UNAUTH_CODE, GlobalApiCode.UNAUTH_CODE_MSG)))
                );
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
        if (startTime != null) {
            log.info("{} method:{} url:{} body:{} timeOff:{}",
                    "The Gateway Global Handler",
                    exchange.getRequest().getMethodValue(),
                    exchange.getRequest().getURI().toString(),
                    Optional.ofNullable(exchange.getAttribute(GATEWAY_BODY)).orElse(""),
                    (System.currentTimeMillis() - startTime) + "ms"
            );
        }
    }

}
