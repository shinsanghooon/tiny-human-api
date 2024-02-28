package com.tinyhuman.tinyhumanapi.user.domain;

import lombok.Builder;

public record UserPushTokenCreate(String fcmToken, String deviceInfo) {

    @Builder
    public UserPushTokenCreate {

    }

}
