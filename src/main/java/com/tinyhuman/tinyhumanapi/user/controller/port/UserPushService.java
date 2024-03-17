package com.tinyhuman.tinyhumanapi.user.controller.port;

import com.tinyhuman.tinyhumanapi.user.domain.UserPushToken;
import com.tinyhuman.tinyhumanapi.user.domain.UserPushTokenCreate;

public interface UserPushService {

    UserPushToken registerToken(UserPushTokenCreate userPushTokenCreate);


}
