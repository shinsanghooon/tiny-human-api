package com.tinyhuman.tinyhumanapi.user.domain;

import lombok.Builder;

public record UserPushToken(Long id, Long userId, String fcmToken, String deviceInfo) {

    @Builder
    public UserPushToken {

    }

}
