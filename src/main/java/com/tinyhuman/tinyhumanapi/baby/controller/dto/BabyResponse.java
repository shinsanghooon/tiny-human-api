package com.tinyhuman.tinyhumanapi.baby.controller.dto;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import lombok.Builder;

import java.time.LocalDate;

public record BabyResponse(Long id, String name, Gender gender, LocalDate dayOfBirth, int timeOfBirth, String nickName, String profileImgKeyName) {

    @Builder
    public BabyResponse {
    }

    public static BabyResponse fromModel(Baby baby) {

        if (baby.isDeleted()) {
            throw new ResourceNotFoundException("Baby", baby.id());
        }

        return BabyResponse.builder()
                .id(baby.id())
                .name(baby.name())
                .gender(baby.gender())
                .dayOfBirth(baby.dayOfBirth())
                .timeOfBirth(baby.timeOfBirth())
                .nickName(baby.nickName())
                .profileImgKeyName(baby.profileImgKeyName())
                .build();
    }
}
