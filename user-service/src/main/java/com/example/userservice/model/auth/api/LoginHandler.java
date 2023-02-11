package com.example.userservice.model.auth.api;

import com.example.userservice.app.security.jwt.JwtTokenProvider;
import com.example.userservice.model.ApiDataResult;
import com.example.userservice.model.ApiResult;
import com.example.userservice.model.auth.data.LoginRequest;

import com.example.userservice.model.auth.data.Token;
import com.example.userservice.model.auth.service.AccessTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component("LoginHandler")
@RequiredArgsConstructor
public class LoginHandler {

    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public Mono<ServerResponse> login(ServerRequest request){
        Mono<LoginRequest> body = request.bodyToMono(LoginRequest.class);

        Mono<Authentication> authentication = body.flatMap(data -> {
            return authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword()) );
        });

        return authentication.flatMap(auth-> {
                    String accessToken = tokenProvider.createAccessToken(auth);
                    String refreshToken = tokenProvider.createRefreshToken(auth);

                    Token token = Token.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                    return  ServerResponse.ok().bodyValue(new ApiDataResult<>(true, "로그인성공" ,token));
                }).onErrorResume(e->{
                    return ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(new ApiResult(false, e.getMessage()));
                });
    }
}
