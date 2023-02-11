package com.example.userservice.model.auth.service;

import com.example.userservice.app.security.jwt.JwtTokenProvider;
import com.example.userservice.model.auth.data.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccessTokenService {
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public Mono<Token> login(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Mono<Authentication> authentication = authenticationManager.authenticate(authenticationToken);
        return authentication.flatMap(auth->{
            String accessToken = tokenProvider.createAccessToken(auth);
            String refreshToken = tokenProvider.createRefreshToken(auth);

            Token token = Token.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            return Mono.just(token);
        }).onErrorResume(error->{
            return Mono.error(new RuntimeException("인증 실패"));
        });
    }

    public Mono<Token> createAccessToken(Authentication authentication){
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return Mono.just(token);
    }


}
