package com.tinyhuman.tinyhumanapi.auth.mock;

import com.tinyhuman.tinyhumanapi.auth.controller.port.RefreshTokenService;
import com.tinyhuman.tinyhumanapi.auth.domain.RefreshToken;

public class FakeRefreshTokenService implements RefreshTokenService {
    @Override
    public RefreshToken findByRefreshToken(String refreshToken) {
        return RefreshToken.builder()
                .id(1L)
                .userId(3L)
                .refreshToken("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTMyNDAzOTh9.IamWQ4x3k5swTIEG0Ay2YYXFQW9H1WVKt-OQihOnC9E")
                .build();
    }
}
