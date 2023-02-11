package com.example.userservice.app.config;

import com.example.userservice.app.security.jwt.JwtTokenAuthenticationFilter;
import com.example.userservice.app.security.jwt.JwtTokenProvider;
import com.example.userservice.app.security.oauth.OAuth2AuthenticationFailureHandler;
import com.example.userservice.app.security.oauth.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@EnableWebFluxSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    /**
     * Spring Security PasswordEncoder Bean 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ReactiveAuthenticationManager Bean 등록
     */
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    /**
     * Webflux SecurityWebFilterChain Bean
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        //
        http.authorizeExchange(exchange->{
            exchange.pathMatchers("/login/**","/auth/**","/signup/**","/oauth2/**","/signupUser").permitAll()
                    .anyExchange().authenticated();
//            .anyExchange().permitAll();
        });
        http.csrf().disable();
//        http.formLogin();
        http.oauth2Login(oauth2-> oauth2
                .authenticationSuccessHandler(oAuth2AuthenticationSuccessHandler)
                .authenticationFailureHandler(oAuth2AuthenticationFailureHandler)
        );

//        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        http.addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC);
        return http.build();
    }



}
