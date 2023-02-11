package com.example.userservice.model.demo.controller;

import com.example.userservice.app.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class DemoController {

    @GetMapping("/test")
    public Mono<String> testCt(@AuthenticationPrincipal UserDetails user){

        return Mono.just("Hellow "+ user.getUsername());
    }


    private final WebClient.Builder webClientBuilder;

    @GetMapping("/reactive/test")
    public Mono<String> reactiveTest(@AuthenticationPrincipal OAuth2User user){
        WebClient client = WebClient.create();


//        Mono<String> clientData = client.get()
////                .uri("http://localhost:9992/board")
//                .uri("host.docker.internal:board-service:9992")
//                .retrieve()
//                .bodyToMono(String.class)
//                .flatMap(result -> {
//                    return Mono.just(user.getUsername() + "님~~  " + result);
//                });
        Mono<String> clientData = webClientBuilder
                .build().get()
                .uri("http://board-service/board")
                .retrieve()//재시도 횟수
                .bodyToMono(String.class)
                .onErrorResume(throwable->{
                    return Mono.just("board 서비스 오류 임~");
                });


        return  clientData.flatMap(result -> {
            return Mono.just("_"+user.getName() + "님~~  " + result + user);
        });
    }

}
