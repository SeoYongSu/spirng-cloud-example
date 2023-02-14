package com.example.userservice.model.user.service.imp;

import com.example.userservice.app.security.UserPrincipal;
import com.example.userservice.model.user.domain.User;
import com.example.userservice.model.user.repository.ReactiveUserRepository;
import com.example.userservice.model.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveUserDetailServiceImp implements UserService,ReactiveUserDetailsService {

    private final ReactiveUserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username).map(UserPrincipal::create);
    }

    public Mono<Void> regist(User user){
        return userRepository.save(user).then();
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Mono<User> findByUserID(String userID){
        return userRepository.findByUserID(userID);
    }

    public Mono<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }


}
