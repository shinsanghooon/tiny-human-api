package com.tinyhuman.tinyhumanapi.auth.infrastructure;

import com.tinyhuman.tinyhumanapi.auth.domain.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUserId(Long userId);

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    RefreshTokenEntity save(RefreshTokenEntity refreshToken);
}
