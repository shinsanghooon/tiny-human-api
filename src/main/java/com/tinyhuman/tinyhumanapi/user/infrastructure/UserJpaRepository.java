package com.tinyhuman.tinyhumanapi.user.infrastructure;

import com.tinyhuman.tinyhumanapi.auth.eum.SocialMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndSocialMedia(String email, SocialMedia socialMedia);

    boolean existsByEmail(String email);

    boolean existsByEmailAndSocialMedia(String email, SocialMedia socialMedia);

    Page<UserEntity> findAllByOrderByLastLoginAtDesc(Pageable pageable);
}
