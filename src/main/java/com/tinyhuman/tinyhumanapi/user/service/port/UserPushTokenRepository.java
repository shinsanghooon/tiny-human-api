package com.tinyhuman.tinyhumanapi.user.service.port;

import com.tinyhuman.tinyhumanapi.user.domain.UserPushToken;

import java.util.Optional;

public interface UserPushTokenRepository {

    UserPushToken save(UserPushToken userPushToken);


    Optional<UserPushToken> findByUserIdAndAndDeviceInfo(Long userId, String deviceInfo);

}
