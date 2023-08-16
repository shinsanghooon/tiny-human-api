package com.tinyhuman.tinyhumanapi.baby.domain;

import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

public record BabyResponse(Long id, String name, Gender gender, LocalDate dayOfBirth, int timeOfBirth, String nickName, String profileImgUrl) {

    @Builder
    public BabyResponse {
    }

    public static BabyResponse fromModel(Baby baby) {
        return BabyResponse.builder()
                .id(baby.id())
                .name(baby.name())
                .gender(baby.gender())
                .dayOfBirth(baby.dayOfBirth())
                .timeOfBirth(baby.timeOfBirth())
                .nickName(baby.nickName())
                .profileImgUrl(baby.profileImgUrl())
                .build();
    }
}
