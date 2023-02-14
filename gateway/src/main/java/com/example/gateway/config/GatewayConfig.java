package com.example.gateway.config;

import com.example.gateway.filter.GatewayFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GatewayConfig {

    /**
     * 회원가입 Router
     */
    @Bean
    public RouteLocator singupRouter(RouteLocatorBuilder builder){
        return builder.routes()
                .route("signup",r->
                        r.path("/signup")
                                .and().method(HttpMethod.POST)
                                .uri("lb://USER-SERVICE/")
                ).build();
    }

    /**
     * Login 처리용 Router
     */
    @Bean
    public RouteLocator oAuthRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r ->
                        r.path("/login/**","/oauth2/**","/auth/**")
                                .filters(f->f.rewritePath("/redirect/(?<segment>.*)", "/${segment}"))
//                                .uri("http://localhost:9901/")) //IP로 설정
                                .uri("lb://USER-SERVICE/"))
                .build();
    }


    /**
     * Filter 설정된 Router 관리
     */
    private final GatewayFilter gatewayFilter;
    @Bean
    public RouteLocator userRouter(RouteLocatorBuilder builder){
        return builder.routes()
                .route("example_route", r -> r.path("/user")
                        .filters(f->
                                f.filter(gatewayFilter)
                                        .rewritePath("/user", "/my"))
                        .uri("lb://USER-SERVICE/"))
                .build();
    }
}
