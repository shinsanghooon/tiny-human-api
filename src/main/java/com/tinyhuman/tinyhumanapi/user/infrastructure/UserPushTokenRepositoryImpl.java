package com.tinyhuman.tinyhumanapi.user.infrastructure;

import com.tinyhuman.tinyhumanapi.user.domain.UserPushToken;
import com.tinyhuman.tinyhumanapi.user.service.port.UserPushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserPushTokenRepositoryImpl implements UserPushTokenRepository {

    private final UserPushTokenJpaRepository userPushTokenJpaRepository;

    @Override
    public UserPushToken save(UserPushToken userPushToken) {
        return userPushTokenJpaRepository.save(UserPushTokenEntity.fromModel(userPushToken)).toModel();
    }

    @Override
    public Optional<UserPushToken> findByUserIdAndAndDeviceInfo(Long userId, String deviceInfo) {
        return userPushTokenJpaRepository.findByUserIdAndAndDeviceInfo(userId, deviceInfo).map(UserPushTokenEntity::toModel);
    }

    @Override
    public List<UserPushToken> findByUserId(Long userId) {
        return userPushTokenJpaRepository.findByUserId(userId).stream().map(UserPushTokenEntity::toModel).toList();
    }

    @Override
    public List<UserPushToken> findByUserIds(List<Long> userIds) {
        return userPushTokenJpaRepository.findByUserIdIn(userIds).stream().map(UserPushTokenEntity::toModel).toList();
    }

}
