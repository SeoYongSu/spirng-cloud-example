package com.example.userservice.model.auth.api;

import com.example.userservice.model.auth.data.SignupRequest;
import com.example.userservice.model.auth.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component("SignupHandler")
@RequiredArgsConstructor
public class SignupHandler {



    private final SignupService signupService;

    /**
     * POST /signup
     * RequestBody :  SignupRequest.class
     */
    public Mono<ServerResponse> signup(ServerRequest request){
        Mono<SignupRequest> body = request.bodyToMono(SignupRequest.class);

        return body.flatMap(signupService::signup)
                .then(ServerResponse.ok().bodyValue("회원가입 성공"))
                .onErrorResume(e-> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }
}
