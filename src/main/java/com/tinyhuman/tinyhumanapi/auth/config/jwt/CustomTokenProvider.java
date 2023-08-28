package com.tinyhuman.tinyhumanapi.auth.config.jwt;

import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import org.springframework.security.core.Authentication;

import java.time.Duration;

public interface CustomTokenProvider {

    TokenResponse generationToken(User user, Duration expiredAt);

    boolean checkValidToken(String token);

    Authentication getAuthentication(String token);

    Long getUserId(String token);

}
