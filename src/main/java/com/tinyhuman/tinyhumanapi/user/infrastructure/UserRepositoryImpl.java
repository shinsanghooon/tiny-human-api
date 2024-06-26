package com.tinyhuman.tinyhumanapi.user.infrastructure;

import com.tinyhuman.tinyhumanapi.auth.eum.SocialMedia;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.fromModel(user)).toModel();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByEmailAndSocialMedia(String email, SocialMedia socialMedia) {
        return userJpaRepository.findByEmailAndSocialMedia(email, socialMedia).map(UserEntity::toModel);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailAndSocialMedia(String email, SocialMedia socialMedia) {
        return userJpaRepository.existsByEmailAndSocialMedia(email, socialMedia);
    }

    @Override
    public List<User> findRandomUser(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by("lastLoginAt").descending());
        return userJpaRepository.findAllByOrderByLastLoginAtDesc(pageRequest).getContent()
                .stream()
                .map(UserEntity::toModel
                ).toList();
    }

    @Override
    public List<User> findByAllowDiaryNotificationsIsTrue() {
        return userJpaRepository.findByAllowDiaryNotificationsIsTrue().stream().map(UserEntity::toModel).toList();
    }

}
