package com.tinyhuman.tinyhumanapi.user.domain;

import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import lombok.Builder;

import java.time.LocalDateTime;

public record User(
        Long id,
        String name,
        String email,
        String password,
        UserStatus status,
        LocalDateTime lastLoginAt
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
                .build();
    }


}
