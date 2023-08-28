package com.tinyhuman.tinyhumanapi.auth.config.jwt;

import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class TokenProviderTest {

    private TokenProvider tokenProvider;
    private String TEST_JWT_SECRET_KEY = "testKey";
    private String TEST_JWT_ISSUER = "test";

    @BeforeEach
    void init() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setIssuer(TEST_JWT_ISSUER);
        jwtProperties.setSecretKey(TEST_JWT_SECRET_KEY);

        this.tokenProvider = new TokenProvider(jwtProperties);
    }

    @DisplayName("사용자 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        User user = User.builder()
                .id(1L)
                .name("테스트")
                .status(UserStatus.ACTIVE)
                .email("test@abc.com")
                .build();

        TokenResponse tokenResponse = tokenProvider.generationToken(user, Duration.ofHours(2));

        Long userId = Jwts.parser()
                .setSigningKey(TEST_JWT_SECRET_KEY)
                .parseClaimsJws(tokenResponse.accessToken())
                .getBody()
                .get("userId", Long.class);

        assertThat(tokenResponse.accessToken()).isNotNull();
        assertThat(tokenResponse.refreshToken()).isNotNull();
        assertThat(user.id()).isEqualTo(userId);
    }

    @DisplayName("만료되지 않은 토큰이면 유효성 검증에 성공한다.")
    @Test
    void validateSuccessToken() {
        User user = User.builder()
                .id(1L)
                .name("테스트")
                .status(UserStatus.ACTIVE)
                .email("test@abc.com")
                .build();

        TokenResponse tokenResponse = tokenProvider.generationToken(user, Duration.ofDays(10));

        boolean result = tokenProvider.checkValidToken(tokenResponse.accessToken());

        assertThat(result).isTrue();
    }

    @DisplayName("만료된 토큰이면 유효성 검증에 실패한다.")
    @Test
    void validateExpiredToken() {
        User user = User.builder()
                .id(1L)
                .name("테스트")
                .status(UserStatus.ACTIVE)
                .email("test@abc.com")
                .build();

        TokenResponse tokenResponse = tokenProvider.generationToken(user, Duration.ofDays(-10));

        boolean result = tokenProvider.checkValidToken(tokenResponse.accessToken());

        assertThat(result).isFalse();
    }

    @DisplayName("토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        User user = User.builder()
                .id(1L)
                .name("테스트")
                .status(UserStatus.ACTIVE)
                .email("test@abc.com")
                .build();

        TokenResponse tokenResponse = tokenProvider.generationToken(user, Duration.ofDays(10));
        Authentication authentication = tokenProvider.getAuthentication(tokenResponse.accessToken());

        assertThat(authentication.getName()).isEqualTo("test@abc.com");
    }

    @DisplayName("토큰으로 user id를 가져올 수 있다.")
    @Test
    void getUserIdByToken() {
        User user = User.builder()
                .id(1L)
                .name("테스트")
                .status(UserStatus.ACTIVE)
                .email("test@abc.com")
                .build();

        TokenResponse tokenResponse = tokenProvider.generationToken(user, Duration.ofDays(10));

        Long userId = tokenProvider.getUserId(tokenResponse.accessToken());

        assertThat(user.id()).isEqualTo(userId);

    }
}