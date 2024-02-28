package com.tinyhuman.tinyhumanapi.user.infrastructure;

import com.tinyhuman.tinyhumanapi.user.domain.UserPushToken;
import com.tinyhuman.tinyhumanapi.user.service.port.UserPushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}
