package com.husen.ci.gateway;

import com.husen.ci.gateway.utils.GlobalHelper;
import lombok.Data;
import lombok.experimental.Accessors;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.DefaultClientResponse;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/***
 @Author:MrHuang
 @Date: 2019/7/12 10:27
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Deprecated
public class GlobalRspPostGatewayFilterFactory extends AbstractGatewayFilterFactory<GlobalRspPostGatewayFilterFactory.Config> {

    /**
     * constructor
     */
    public GlobalRspPostGatewayFilterFactory(){
        // 这里需要将自定义的config传过去，否则会报告ClassCastException
        super(GlobalRspPostGatewayFilterFactory.Config.class);
    }


    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.emptyList();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new ModifyResponseGatewayFilter(Optional.ofNullable(config).orElse(new Config()).setInClass(Object.class).setOutClass(Object.class));
    }

    public class ModifyResponseGatewayFilter implements GatewayFilter, Ordered {

        private final GlobalRspPostGatewayFilterFactory.Config config;

        public ModifyResponseGatewayFilter(GlobalRspPostGatewayFilterFactory.Config config) {
            this.config = config;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

            ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(
                    exchange.getResponse()) {

                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    Class inClass = config.getInClass();
                    Class outClass = config.getOutClass();
                    String originalResponseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE,
                            originalResponseContentType);
                    GlobalRspPostGatewayFilterFactory.ResponseAdapter responseAdapter = new GlobalRspPostGatewayFilterFactory.ResponseAdapter(body, httpHeaders);
                    DefaultClientResponse clientResponse = new DefaultClientResponse(responseAdapter, ExchangeStrategies.withDefaults());

                    // TODO: flux or mono
                    Mono modifiedBody = clientResponse.bodyToMono(inClass)
                            .flatMap(originalBody -> {
                                exchange.getAttributes().put(GlobalHelper.GATEWAY_RESPONSE_RESULT, originalBody);
                                return Mono.just(originalBody);
                            });

                    BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody,
                            outClass);
                    CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, exchange.getResponse().getHeaders());
                    return bodyInserter.insert(outputMessage, new BodyInserterContext())
                            .then(Mono.defer(() -> {
                                Flux<DataBuffer> messageBody = outputMessage.getBody();
                                HttpHeaders headers = getDelegate().getHeaders();
                                if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)) {
                                    messageBody = messageBody.doOnNext(data -> headers.setContentLength(data.readableByteCount()));
                                }
                                // TODO: use isStreamingMediaType?
                                return getDelegate().writeWith(messageBody);
                            }));
                }

                @Override
                public Mono<Void> writeAndFlushWith(
                        Publisher<? extends Publisher<? extends DataBuffer>> body) {
                    return writeWith(Flux.from(body).flatMapSequential(p -> p));
                }
            };

            return chain.filter(exchange.mutate().response(responseDecorator).build());
        }

        @Override
        public int getOrder() {
            return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
        }

    }

    public class ResponseAdapter implements ClientHttpResponse {

        private final Flux<DataBuffer> flux;

        private final HttpHeaders headers;

        public ResponseAdapter(Publisher<? extends DataBuffer> body,
                               HttpHeaders headers) {
            this.headers = headers;
            if (body instanceof Flux) {
                flux = (Flux) body;
            }
            else {
                flux = ((Mono) body).flux();
            }
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return flux;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        @Override
        public HttpStatus getStatusCode() {
            return null;
        }

        @Override
        public int getRawStatusCode() {
            return 0;
        }

        @Override
        public MultiValueMap<String, ResponseCookie> getCookies() {
            return null;
        }

    }

    /**
     * 自定义的config类，用来设置传入的参数
     */
    @Data
    @Accessors(chain = true)
    public static class Config {

        private Class inClass;

        private Class outClass;

    }
}
