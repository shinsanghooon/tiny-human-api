package com.tinyhuman.tinyhumanapi.user.service.port;

import com.tinyhuman.tinyhumanapi.user.domain.UserPushToken;

import java.util.List;

public interface UserPushTokenRepository {

    UserPushToken save(UserPushToken userPushToken);


    List<UserPushToken> findByUserId(Long userId);

}
