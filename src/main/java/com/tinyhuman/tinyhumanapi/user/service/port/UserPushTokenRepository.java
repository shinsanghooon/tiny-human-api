package com.tinyhuman.tinyhumanapi.user.service.port;

import com.tinyhuman.tinyhumanapi.user.domain.UserPushToken;

import java.util.List;
import java.util.Optional;

public interface UserPushTokenRepository {

    UserPushToken save(UserPushToken userPushToken);
    Optional<UserPushToken> findByUserIdAndAndDeviceInfo(Long userId, String deviceInfo);

    List<UserPushToken> findByUserId(Long userId);
    List<UserPushToken> findByUserIds(List<Long> userIds);

}
