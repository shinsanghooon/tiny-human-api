package com.tinyhuman.tinyhumanapi.baby.controller.dto;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import lombok.Builder;

import java.time.LocalDate;

public record BabyPreSignedUrlResponse(Long id, String name, Gender gender, LocalDate dayOfBirth, int timeOfBirth, String nickName, String profileImgKeyName, String description, String preSignedUrl) {

    @Builder
    public BabyPreSignedUrlResponse {
    }

    public static BabyPreSignedUrlResponse fromModel(Baby baby, String preSignedUrl) {

        if (baby.isDeleted()) {
            throw new ResourceNotFoundException("Baby", baby.id());
        }

        return BabyPreSignedUrlResponse.builder()
                .id(baby.id())
                .name(baby.name())
                .gender(baby.gender())
                .dayOfBirth(baby.dayOfBirth())
                .timeOfBirth(baby.timeOfBirth())
                .nickName(baby.nickName())
                .profileImgKeyName(baby.profileImgKeyName())
                .description(baby.description())
                .preSignedUrl(preSignedUrl)
                .build();
    }
}
