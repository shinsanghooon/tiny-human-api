package com.tinyhuman.tinyhumanapi.auth.infrastructure;

import com.tinyhuman.tinyhumanapi.auth.domain.RefreshToken;
import com.tinyhuman.tinyhumanapi.auth.domain.RefreshTokenEntity;
import com.tinyhuman.tinyhumanapi.auth.service.port.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        return refreshTokenJpaRepository.findByUserId(userId).map(RefreshTokenEntity::toModel);
    }

    @Override
    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return refreshTokenJpaRepository.findByRefreshToken(refreshToken).map(RefreshTokenEntity::toModel);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJpaRepository.save(RefreshTokenEntity.fromModel(refreshToken)).toModel();
    }
}
