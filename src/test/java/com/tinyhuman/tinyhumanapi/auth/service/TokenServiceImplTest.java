package com.tinyhuman.tinyhumanapi.auth.service;

import com.tinyhuman.tinyhumanapi.auth.domain.CreateAccessTokenResponse;
import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.auth.mock.FakeCustomTokenProvider;
import com.tinyhuman.tinyhumanapi.auth.mock.FakeRefreshTokenService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenServiceImplTest {

    private TokenServiceImpl tokenServiceImpl;

    @BeforeEach
    void init() {
        FakeRefreshTokenService fakeRefreshTokenService = new FakeRefreshTokenService();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeCustomTokenProvider fakeCustomTokenProvider = new FakeCustomTokenProvider();

        this.tokenServiceImpl = TokenServiceImpl
                .builder()
                .customTokenProvider(fakeCustomTokenProvider)
                .refreshTokenService(fakeRefreshTokenService)
                .userRepository(fakeUserRepository)
                .build();

        User user = User.builder()
                .id(3L)
                .name("테스트")
                .status(UserStatus.ACTIVE)
                .email("test@abc.com")
                .build();

        fakeUserRepository.save(user);
    }


    @DisplayName("새로운 토큰을 발급 받을 수 있다.")
    @Test
    void createNewAccessToken() {
        User user = User.builder()
                .id(3L)
                .name("테스트")
                .status(UserStatus.ACTIVE)
                .email("test@abc.com")
                .build();

        FakeCustomTokenProvider fakeCustomTokenProvider = new FakeCustomTokenProvider();
        TokenResponse tokenResponse = fakeCustomTokenProvider.generationToken(user, Duration.ofHours(2));

        CreateAccessTokenResponse newAccessToken = tokenServiceImpl.createNewAccessToken(tokenResponse.refreshToken());

        assertThat(newAccessToken).isNotNull();
    }

    @DisplayName("refresh 토큰이 일치하지 않으면 예외를 던진다.")
    @Test
    void createNewAccessTokenWithInvalidRefreshToken() {
        assertThatThrownBy(() -> tokenServiceImpl.createNewAccessToken("random_token"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unexpected Token");
    }

}