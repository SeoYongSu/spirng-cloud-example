package com.example.userservice.model.user.repository;

import com.example.userservice.model.user.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ReactiveUserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUserID(String userID);

    Mono<User> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);

}
