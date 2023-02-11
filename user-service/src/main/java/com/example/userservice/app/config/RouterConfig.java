package com.example.userservice.app.config;

import com.example.userservice.model.auth.api.LoginHandler;
import com.example.userservice.model.auth.api.SignupHandler;
import com.example.userservice.model.user.api.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {

    @Bean
    RouterFunction<ServerResponse> signupEndPoint(SignupHandler handler) {
        return RouterFunctions
                .route( 	POST("/signup"), 		handler :: signup);

    }

    @Bean
    RouterFunction<ServerResponse> loginEndPoint(LoginHandler handler) {
        return RouterFunctions
                .route( 	POST("/login"), 		handler :: login);
    }

    @Bean
    RouterFunction<ServerResponse> userEndPoint(UserHandler handler) {
        return RouterFunctions
                .route( 	GET("/my"), 		handler :: getInfo);
    }


}
