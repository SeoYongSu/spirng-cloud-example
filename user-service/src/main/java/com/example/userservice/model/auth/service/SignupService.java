package com.example.userservice.model.auth.service;

import com.example.userservice.model.auth.data.SignupRequest;
import com.example.userservice.model.user.domain.User;
import com.example.userservice.model.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final PasswordEncoder encoder;

    private final UserService userService;

    public Mono<Void> signup(SignupRequest request) {
        return userService.existsByEmail(request.getEmail())
                .flatMap(exists->{
                    if(exists)
                        return Mono.error(new RuntimeException("이미 사용중인 이메일 입니다."));

                    User user = User.builder()
                            .userID(UUID.randomUUID().toString())
                            .email(request.getEmail())
                            .password(encoder.encode(request.getPassword()))
                            .providerType("LOCAL")
                            .build();
                    return userService.regist(user).then();
                });
    }
}
