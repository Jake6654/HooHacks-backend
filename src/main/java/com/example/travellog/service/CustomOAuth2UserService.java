package com.example.travellog.service;

import com.example.travellog.document.User;
import com.example.travellog.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;


    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("OAuth2 Provider에서 받은 attributes: {}", attributes);


        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String photoUrl = (String) attributes.get("picture");

        if (email == null) {
            throw new OAuth2AuthenticationException("이메일 정보가 없습니다. 다른 OAuth2 프로바이더를 확인하세요.");
        }


        log.info("추출된 email: {}, name: {}, picture: {}", email, name, photoUrl);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUserName(name);
                    newUser.setPhotoUrl(photoUrl);
                    return userRepository.save(newUser);
                });


        return new DefaultOAuth2User(
                Collections.emptyList(),
                attributes,
                "email"
        );
    }


}

