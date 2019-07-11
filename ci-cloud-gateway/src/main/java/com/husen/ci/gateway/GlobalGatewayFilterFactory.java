package com.husen.ci.gateway;

import com.husen.ci.gateway.utils.GlobalHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

            // TODO 执行前
            GlobalHelper.handlerPre(exchange);

            // TODO 执行后
            Runnable runnable = () -> {
                GlobalHelper.handlerPost(exchange);
            };

            MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
            ServerRequest serverRequest = new DefaultServerRequest(exchange);
            if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)){
                // 如果是json格式，将body内容转化为object or map 都可
                Mono<Object> modifiedBody = serverRequest.bodyToMono(Object.class)
                        .flatMap(body -> {
                            exchange.getAttributes().put(GlobalHelper.GATEWAY_REQUEST_BODY, body);
                            return Mono.just(body);
                });
                return getVoidMono(exchange, chain, Object.class, modifiedBody).then(Mono.fromRunnable(runnable));
            } else if(MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)){
                // 如果是表单请求
                Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                        // .log("modify_request_mono", Level.INFO)
                        .flatMap(body -> {
                            exchange.getAttributes().put(GlobalHelper.GATEWAY_REQUEST_BODY, body);
                            return Mono.just(body);
                        });

                return getVoidMono(exchange, chain, String.class, modifiedBody).then(Mono.fromRunnable(runnable));
            }
            // TODO 这里未来还可以限制一些格式
            return chain.filter(exchange.mutate().request(exchange.getRequest()).build()).then(Mono.fromRunnable(runnable));
        });
    }

    /**
     * 参照 ModifyRequestBodyGatewayFilterFactory.java 截取的方法
     * @param exchange
     * @param chain
     * @param outClass
     * @param modifiedBody
     * @return
     */
    private Mono<Void> getVoidMono(ServerWebExchange exchange, GatewayFilterChain chain, Class outClass, Mono<?> modifiedBody) {
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());

        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage,  new BodyInserterContext())
                // .log("modify_request", Level.INFO)
                .then(Mono.defer(() -> {
                    ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
                            exchange.getRequest()) {
                        @Override
                        public HttpHeaders getHeaders() {
                            long contentLength = headers.getContentLength();
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.putAll(super.getHeaders());
                            if (contentLength > 0) {
                                httpHeaders.setContentLength(contentLength);
                            } else {
                                // TODO: this causes a 'HTTP/1.1 411 Length Required' on httpbin.org
                                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                            }
                            return httpHeaders;
                        }

                        @Override
                        public Flux<DataBuffer> getBody() {
                            return outputMessage.getBody();
                        }
                    };
                    return chain.filter(exchange.mutate().request(decorator).build());
                }));
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
