package com.tinyhuman.tinyhumanapi.auth.service.port;

import com.tinyhuman.tinyhumanapi.auth.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    Optional<RefreshToken> findByUserId(Long userId);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    RefreshToken save(RefreshToken refreshToken);
}
