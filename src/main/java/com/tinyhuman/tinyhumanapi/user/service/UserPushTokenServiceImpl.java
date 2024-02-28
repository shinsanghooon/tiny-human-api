package com.tinyhuman.tinyhumanapi.user.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserPushService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserPushToken;
import com.tinyhuman.tinyhumanapi.user.domain.UserPushTokenCreate;
import com.tinyhuman.tinyhumanapi.user.service.port.UserPushTokenRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
public class UserPushTokenServiceImpl implements UserPushService {

    private final UserPushTokenRepository userPushTokenRepository;

    private final AuthService authService;

    @Builder
    public UserPushTokenServiceImpl(UserPushTokenRepository userPushTokenRepository, AuthService authService) {
        this.userPushTokenRepository = userPushTokenRepository;
        this.authService = authService;
    }

    @Override
    public UserPushToken registerToken(UserPushTokenCreate userPushTokenCreate) {
        User user = authService.getUserOutOfSecurityContextHolder();
        UserPushToken userPushToken = UserPushToken.builder()
                .userId(user.id())
                .fcmToken(userPushTokenCreate.fcmToken())
                .deviceInfo(userPushTokenCreate.deviceInfo())
                .build();
        return userPushTokenRepository.save(userPushToken);
    }
}
