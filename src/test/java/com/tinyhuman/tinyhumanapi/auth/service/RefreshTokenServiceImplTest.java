package com.tinyhuman.tinyhumanapi.auth.service;

import com.tinyhuman.tinyhumanapi.auth.domain.RefreshToken;
import com.tinyhuman.tinyhumanapi.auth.mock.FakeRefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RefreshTokenServiceImplTest {

    private RefreshTokenServiceImpl refreshTokenService;

    @BeforeEach
    void setUp() {
        FakeRefreshTokenRepository fakeRefreshTokenRepository = new FakeRefreshTokenRepository();

        this.refreshTokenService = RefreshTokenServiceImpl.builder()
                .refreshTokenRepository(fakeRefreshTokenRepository)
                .build();

        fakeRefreshTokenRepository.save(RefreshToken.builder()
                .userId(1L)
                .refreshToken("thisisRefreshToken")
                .build());
    }

    @Test
    @DisplayName("리프레시 토큰을 조회할 수 있다.")
    void getRefreshToken() {
        RefreshToken refreshToken = refreshTokenService.findByRefreshToken("thisisRefreshToken");

        assertThat(refreshToken.userId()).isEqualTo(1L);
        assertThat(refreshToken.refreshToken()).isEqualTo("thisisRefreshToken");
    }

    @Test
    @DisplayName("리프레시 토큰이 없으면 예외를 던진다.")
    void getRefreshTokenWihNotExists() {
        assertThatThrownBy(() -> refreshTokenService.findByRefreshToken("notvalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Token");
    }
}