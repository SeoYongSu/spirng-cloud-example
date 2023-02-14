package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GatewayFilter implements org.springframework.cloud.gateway.filter.GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());

        ServerHttpRequest request = exchange.getRequest();
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if( bearerToken == null) {
            log.info("Header에 AUTHORIZATION 없음");
            return chain.filter(exchange);
        }
        
        ServerHttpResponse response = exchange.getResponse();


        return chain.filter(exchange)
                .doAfterTerminate(() -> log.info("Response status: {}", exchange.getResponse().getStatusCode()));
    }
}
