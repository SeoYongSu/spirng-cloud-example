package com.example.userservice.app.security.oauth;

import com.example.userservice.app.security.UserPrincipal;
import com.example.userservice.app.security.jwt.JwtTokenProvider;
import com.example.userservice.model.ApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    //ServerAuthenticationSuccessHandler		 		-> interFace
    //RedirectServerAuthenticationSuccessHandler   		-> imp sample
    //DelegatingServerAuthenticationSuccessHandler 		-> imp sample
    //WebFilterChainServerAuthenticationSuccessHandler 	-> imp sample
    private final ObjectMapper objectMapper;

    private final JwtTokenProvider tokenProvider;
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

        System.out.println("Success Handler 진입");
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        log.info("authentication : {}",authentication);
        log.info("authentication getPrincipal : {}", authentication.getPrincipal());

        String accessToken = tokenProvider.createAccessToken(authentication);
        ApiResult a = new ApiResult(true,accessToken);

        try {
            DataBuffer body = response.bufferFactory().wrap( objectMapper.writeValueAsBytes(a));
            return response.writeWith( Mono.just(body) );
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }




    }
}
