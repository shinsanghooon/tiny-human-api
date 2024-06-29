package com.tinyhuman.tinyhumanapi.user.domain;

import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import lombok.Builder;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserStatus status,
        LocalDateTime lastLoginAt,

        boolean isAllowChatNotifications,

        boolean isAllowDiaryNotifications
) {

    @Builder
    public UserResponse {
    }

    public static UserResponse fromModel(User user) {
        return UserResponse.builder()
                .id(user.id())
                .email(user.email())
                .name(user.name())
                .status(user.status())
                .lastLoginAt(user.lastLoginAt())
                .isAllowChatNotifications(user.isAllowChatNotifications())
                .isAllowDiaryNotifications(user.isAllowDiaryNotifications())
                .build();
    }
}
