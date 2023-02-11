package com.example.userservice.model.user.api;

import com.example.userservice.model.user.data.UserData;
import com.example.userservice.model.user.domain.User;
import com.example.userservice.model.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;


@Component("UserHandler")
@RequiredArgsConstructor
@Slf4j
public class UserHandler {

    private final UserService userService;

    public Mono<ServerResponse> getInfo(ServerRequest request){

        Mono<UserData> result = request.principal()
                .flatMap(principal -> {
                    return userService.findByUserID(principal.getName())
                            .map(User::toData);
                });
        return result.flatMap(data->{
            return ServerResponse.ok().bodyValue(data);
        });

    }
}
