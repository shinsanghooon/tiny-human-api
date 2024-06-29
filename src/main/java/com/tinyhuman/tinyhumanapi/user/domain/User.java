package com.tinyhuman.tinyhumanapi.user.domain;

import com.tinyhuman.tinyhumanapi.auth.eum.SocialMedia;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import lombok.Builder;

import java.time.LocalDateTime;

public record User(
        Long id,
        String name,
        String email,
        String password,
        UserStatus status,
        SocialMedia socialMedia,
        LocalDateTime lastLoginAt,
        boolean isDeleted,

        boolean isAllowChatNotifications,

        boolean isAllowDiaryNotifications
) {

    @Builder
    public User {
    }

    public static User fromCreate(UserCreate userCreate) {
        return User.builder()
                .name(userCreate.name())
                .email(userCreate.email())
                .password(userCreate.password())
                .status(UserStatus.ACTIVE)
                .socialMedia(userCreate.socialMedia())
                .isDeleted(false)
                .isAllowDiaryNotifications(false)
                .isAllowChatNotifications(false)
                .build();
    }

    public User addLastLoginAt(){
        return User.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .status(this.status)
                .socialMedia(this.socialMedia)
                .lastLoginAt(LocalDateTime.now())
                .isDeleted(this.isDeleted)
                .isAllowDiaryNotifications(this.isAllowDiaryNotifications)
                .isAllowChatNotifications(this.isAllowChatNotifications)
                .build();
    }


}
