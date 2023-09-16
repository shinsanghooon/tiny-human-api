package com.tinyhuman.tinyhumanapi.auth.mock;

import com.tinyhuman.tinyhumanapi.auth.config.jwt.CustomTokenProvider;
import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.util.Date;

public class FakeCustomTokenProvider implements CustomTokenProvider {

    private String TEST_JWT_SECRET_KEY = "testKey";
    private String TEST_JWT_ISSUER = "test";
    @Override
    public TokenResponse generationToken(User user, Duration expiredAt) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiredAt.toMillis());

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(TEST_JWT_ISSUER)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.email())
                .claim("userId", user.id())
                .signWith(SignatureAlgorithm.HS256, TEST_JWT_SECRET_KEY)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, TEST_JWT_SECRET_KEY)
                .compact();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public boolean checkValidToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(TEST_JWT_SECRET_KEY)
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Authentication getAuthentication(String token) {
        // TODO 보류, 스프링 시큐리티 기능을 어떻게 테스트 할 것인가?
        return null;
    }

    @Override
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("userId", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(TEST_JWT_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

}