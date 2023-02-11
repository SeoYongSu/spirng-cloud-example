package com.example.userservice.model.auth.api;

import com.example.userservice.app.security.jwt.JwtTokenProvider;
import com.example.userservice.model.auth.data.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LoginHandlerTest {
    @Mock
    private ReactiveAuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider tokenProvider;
    @InjectMocks
    private LoginHandler loginHandler;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(
                RouterFunctions.route(RequestPredicates.POST("/login"), loginHandler::login)
        ).build();


    }

    @Test
    @DisplayName("로그인 성공")
    public void login_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpassword");

        Authentication authentication = new UsernamePasswordAuthenticationToken("testuser", "testpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mono.just(authentication));
        when(tokenProvider.createAccessToken(authentication)).thenReturn("accessToken");
        when(tokenProvider.createRefreshToken(authentication)).thenReturn("refreshToken");

        webTestClient.post().uri("/login")
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("로그인성공")
                .jsonPath("$.data.accessToken").isEqualTo("accessToken")
                .jsonPath("$.data.refreshToken").isEqualTo("refreshToken");
    }

    @Test
    @DisplayName("로그인 실패")
    public void login_Unauthorized() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.error(new BadCredentialsException("로그인 실패")));

        webTestClient.post().uri("/login")
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("로그인 실패");
    }

}