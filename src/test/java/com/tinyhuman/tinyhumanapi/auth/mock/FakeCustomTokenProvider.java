package com.tinyhuman.tinyhumanapi.auth.mock;

import com.tinyhuman.tinyhumanapi.auth.config.jwt.CustomTokenProvider;
import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import org.springframework.security.core.Authentication;

import java.time.Duration;

public class FakeCustomTokenProvider implements CustomTokenProvider{
    @Override
    public TokenResponse generationToken(User user, Duration expiredAt) {
        return TokenResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }

    @Override
    public boolean checkValidToken(String token) {
        return false;
    }

    @Override
    public Authentication getAuthentication(String token) {
        return null;
    }

    @Override
    public Long getUserId(String token) {
        return null;
    }
}
