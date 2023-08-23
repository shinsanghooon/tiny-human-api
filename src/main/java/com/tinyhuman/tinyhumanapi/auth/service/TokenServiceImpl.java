package com.tinyhuman.tinyhumanapi.auth.service;

import com.tinyhuman.tinyhumanapi.auth.config.jwt.TokenProvider;
import com.tinyhuman.tinyhumanapi.auth.controller.port.RefreshTokenService;
import com.tinyhuman.tinyhumanapi.auth.controller.port.TokenService;
import com.tinyhuman.tinyhumanapi.auth.domain.CreateAccessTokenResponse;
import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.common.domain.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @Override
    public CreateAccessTokenResponse createNewAccessToken(String refreshToken) {
        if (!tokenProvider.checkValidToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected Token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).userId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Users", userId));

        TokenResponse tokenResponse = tokenProvider.generationToken(user, Duration.ofHours(2));

        return CreateAccessTokenResponse.builder()
                .accessToken(tokenResponse.accessToken())
                .build();
    }

}
