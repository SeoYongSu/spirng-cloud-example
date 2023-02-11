package com.example.userservice.model.user.service;

import com.example.userservice.model.user.domain.User;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<Void> regist(User user);

    Mono<User> findByUserID(String userID);
    Mono<User> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);

}
