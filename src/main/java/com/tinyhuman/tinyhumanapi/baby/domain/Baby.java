package com.tinyhuman.tinyhumanapi.baby.domain;

import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

public record Baby(Long id, String name, Gender gender, LocalDate dayOfBirth, int timeOfBirth, String nickName, String profileImgUrl) {

    @Builder
    public Baby {
    }

    public static Baby fromCreate(BabyCreate babyCreate, String s3ImgUrl) {
        return Baby.builder()
                .name(babyCreate.name())
                .gender(babyCreate.gender())
                .nickName(babyCreate.nickName())
                .dayOfBirth(babyCreate.dayOfBirth())
                .timeOfBirth(babyCreate.timeOfBirth())
                .profileImgUrl(s3ImgUrl)
                .build();
    }
}
