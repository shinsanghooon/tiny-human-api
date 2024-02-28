package com.tinyhuman.tinyhumanapi.user.infrastructure;

import com.tinyhuman.tinyhumanapi.user.domain.UserPushToken;
import com.tinyhuman.tinyhumanapi.user.service.port.UserPushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserPushTokenImpl implements UserPushTokenRepository {

    private final UserPushTokenJpaRepository userPushTokenJpaRepository;

    @Override
    public UserPushToken save(UserPushToken userPushToken) {
        return userPushTokenJpaRepository.save(UserPushTokenEntity.fromModel(userPushToken)).toModel();
    }

    @Override
    public List<UserPushToken> findByUserId(Long userId) {
        return userPushTokenJpaRepository.findByUserId(userId).stream().map(UserPushTokenEntity::toModel).toList();
    }
}
