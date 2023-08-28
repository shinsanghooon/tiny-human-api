package com.tinyhuman.tinyhumanapi.auth.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.auth.domain.LoginRequest;
import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;

public class FakeAuthService implements AuthService {
    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        return TokenResponse.builder()
                .accessToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhYmNAdGlueWh1bWFuLmNvbSIsImlhdCI6MTY5MzIzMzE5OCwiZXhwIjoxNjkzMjQwMzk4LCJzdWIiOiJzaGluc2FuZ2hvb29uQGdtYWlsLmNvbSIsInVzZXJJZCI6M30.uDqklvbiSkfTbMLSZ8rm3OM4ec_4OrdV2kZjRwq5nfw")
                .refreshToken("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTMyNDAzOTh9.IamWQ4x3k5swTIEG0Ay2YYXFQW9H1WVKt-OQihOnC9E")
                .build();
    }

    @Override
    public User getUserOutOfSecurityContextHolder() {
        return User.builder()
                .id(1L)
                .name("홈버그")
                .email("homebug@tinyhuman.com")
                .status(UserStatus.ACTIVE)
                .build();
    }
}
