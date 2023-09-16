package com.tinyhuman.tinyhumanapi.auth.service;

import com.tinyhuman.tinyhumanapi.auth.config.jwt.CustomTokenProvider;
import com.tinyhuman.tinyhumanapi.auth.controller.port.RefreshTokenService;
import com.tinyhuman.tinyhumanapi.auth.controller.port.TokenService;
import com.tinyhuman.tinyhumanapi.auth.domain.CreateAccessTokenResponse;
import com.tinyhuman.tinyhumanapi.auth.domain.RefreshToken;
import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final CustomTokenProvider customTokenProvider;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    @Builder
    public TokenServiceImpl(CustomTokenProvider customTokenProvider, RefreshTokenService refreshTokenService, UserRepository userRepository) {
        this.customTokenProvider = customTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    @Override
    public CreateAccessTokenResponse createNewAccessToken(String refreshToken) {
        if (!customTokenProvider.checkValidToken(refreshToken)) {
            log.error("IllegalArgumentException - Invalid RefreshToken, You need to login with email and password");
            throw new IllegalArgumentException("Invalid Token");
        }

        RefreshToken refreshTokenInfo = refreshTokenService.findByRefreshToken(refreshToken);
        Long userId = refreshTokenInfo.userId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Users", userId));

        TokenResponse tokenResponse = customTokenProvider.generationToken(user, Duration.ofHours(2));

        return CreateAccessTokenResponse.builder()
                .accessToken(tokenResponse.accessToken())
                .build();
    }

}
