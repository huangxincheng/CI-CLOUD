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
        List<String> authList = exchange.getRequest().getHeaders().get(SsoConstants.RQ_PASS_TOKEN_AUTH);
        if (authList != null && authList.size() > 0) {
            return true;
        }
        String authParam = exchange.getRequest().getQueryParams().getFirst(SsoConstants.RQ_PASS_TOKEN_AUTH);
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
        if (route == null) {
            return true;
        }
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
                if (route.getId().equals(serviceRoute.getService())) {
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
        List<String> authTokenList = exchange.getRequest().getHeaders().get(SsoConstants.RQ_HEADER_TOEKN_SESSION_ID);
        if (authTokenList != null && authTokenList.size() > 0) {
            String authToken = authTokenList.get(0);
            SsoSession ssoSession = SsoLoginHelper.loginCheck(authToken);
            if (!Objects.isNull(ssoSession)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回认证失败
     * @param exchange
     * @return
     */
    static Mono<Void> returnAuthFail(ServerWebExchange exchange) {
        // 不能pass的直接失败
        GlobalApiResponse rsp = GlobalApiResponse.toFail(GlobalApiCode.UNAUTH_CODE, GlobalApiCode.UNAUTH_CODE_MSG);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_LENGTH, JSONUtils.object2Bytes(rsp).length + "");
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return exchange.getResponse()
                .writeWith(
                        Flux.just(getDataBuffer(exchange.getResponse(), rsp))
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
}
