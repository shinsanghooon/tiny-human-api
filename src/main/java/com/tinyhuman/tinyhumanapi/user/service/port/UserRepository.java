package com.tinyhuman.tinyhumanapi.user.service.port;

import com.tinyhuman.tinyhumanapi.auth.eum.SocialMedia;
import com.tinyhuman.tinyhumanapi.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndSocialMedia(String email, SocialMedia socialMedia);

    boolean existsByEmail(String email);

    boolean existsByEmailAndSocialMedia(String email, SocialMedia socialMedia);
}
