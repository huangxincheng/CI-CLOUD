package com.husen.ci.sso;

import com.husen.ci.framework.api.GlobalApiCode;
import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.framework.json.JSONUtils;
import com.husen.ci.sso.helper.SsoLoginHelper;
import com.husen.ci.sso.session.SsoSession;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/***
 @Author:MrHuang
 @Date: 2019/7/11 15:22
 @DESC: TODO
 @VERSION: 1.0
 ***/
public class AuthGatewayCommonHelper {

    /**
     * 判断是否Pass认证
     * @param exchange
     * @return
     */
    static boolean isPassAuth(ServerWebExchange exchange, AuthGatewayFilterFactory.Config config) {
        boolean isPass = exchange.getRequest().getHeaders().containsKey(SsoConstants.RQ_PASS_TOKEN_AUTH);
        if (isPass) {
            return true;
        }
        isPass = exchange.getRequest().getQueryParams().containsKey(SsoConstants.RQ_PASS_TOKEN_AUTH);
        if (isPass) {
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
        if (route == null) {
            return true;
        }
        // 判断是否在不需要认证的服务列表中
        List<String> notAuthServiceList = config.getIgnoreAuthServices();
        if (notAuthServiceList != null && notAuthServiceList.contains(route.getId())) {
            return true;
        }
        // 判断是否在不需要认证的请求uri中
        String rawPath = exchange.getRequest().getURI().getRawPath();
        List<AuthGatewayFilterFactory.ServiceRoute> notAuthRouteList = config.getIgnoreAuthRoutes();
        if (notAuthRouteList != null && notAuthRouteList.size() > 0) {
            for (AuthGatewayFilterFactory.ServiceRoute notAuthRoute : notAuthRouteList) {
                if (route.getId().equals(notAuthRoute.getRouteId())) {
                    for (String uri : notAuthRoute.getUri()) {
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
        String tokenSessionId = exchange.getRequest().getHeaders().getFirst(SsoConstants.RQ_HEADER_TOEKN_SESSION_ID);
        if (!StringUtils.isEmpty(tokenSessionId)) {
            SsoSession ssoSession = SsoLoginHelper.loginCheck(tokenSessionId);
            if (!Objects.isNull(ssoSession)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回认证失败JSON数据
     * @param exchange
     * @return
     */
    static Mono<Void> returnAuthFailData(ServerWebExchange exchange) {
        // 不能pass的直接失败
        GlobalApiResponse rsp = GlobalApiResponse.toSuccess(GlobalApiCode.UNAUTH_CODE, GlobalApiCode.UNAUTH_CODE_MSG);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_LENGTH, JSONUtils.object2Bytes(rsp).length + "");
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return exchange.getResponse()
                .writeWith(
                        Flux.just(getDataBuffer(exchange.getResponse(), rsp))
                );
    }

    /**
     * 返回认证失败重定向页面
     * @param exchange
     * @return
     */
    static Mono<Void> returnAuthFailRedirect(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set(HttpHeaders.LOCATION, exchange.getRequest().getHeaders().getFirst(SsoConstants.RQ_HEADER_REDIRECT_URL));
        return response.setComplete();
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
}
