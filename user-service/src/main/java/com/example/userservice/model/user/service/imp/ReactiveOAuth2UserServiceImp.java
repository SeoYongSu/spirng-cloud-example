package com.example.userservice.model.user.service.imp;

import com.example.userservice.app.lib.StrLib;
import com.example.userservice.app.security.UserPrincipal;
import com.example.userservice.app.security.oauth.user.OAuth2Attributes;
import com.example.userservice.model.user.domain.User;
import com.example.userservice.model.user.repository.ReactiveUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactiveOAuth2UserServiceImp implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final ReactiveUserRepository userRepository;

    @Override
    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) {
        final DefaultReactiveOAuth2UserService delegate = new DefaultReactiveOAuth2UserService();

        String providerType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        return delegate.loadUser(userRequest).flatMap(oAuth2User -> {

            OAuth2Attributes userInfo = OAuth2Attributes.of(providerType, oAuth2User.getAttributes());

            /**
             * OAtuh2 Provider의 필수데이터 확인
             * 서드파티 validation Check
             */
            if(StrLib.isEmptyStr(userInfo.getEmail())) {
                OAuth2Error error = new OAuth2Error("providerNoData");
                return Mono.error(new Exception("OAUTH2공급자" + providerType +"에서  Email정보를 찾을수 없습니다."));
            }


            //Process
            return userRepository.findByUserID(userInfo.getId())
                    .map(UserPrincipal::create)
                    .switchIfEmpty(userRepository.findByEmail(userInfo.getEmail())
                    .flatMap(user->{
                        if(user.getProviderType().equals("LOCAL")){
//                            return Mono.just(UserPrincipal.create(user));
                        }
                        OAuth2Error error = new OAuth2Error("이미가입됨~", user.getProviderType()+"로 가입된 내역이 있습니다.", null);
                        return Mono.error( new OAuth2AuthenticationException( error ));
                    }))
                    .switchIfEmpty(Mono.defer(()->{
                        User user = User.builder()
                                .userID(userInfo.getId())
                                .password("N/A")
                                .email(userInfo.getEmail())
                                .mobile(userInfo.getMobile())
                                .name(userInfo.getName())
                                .providerType(providerType)
                                .build();
                        return userRepository.save(user).map(UserPrincipal::create);
                    }));
        });
    }
}
