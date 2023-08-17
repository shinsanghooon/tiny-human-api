package com.tinyhuman.tinyhumanapi.baby.domain;

import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

public record Baby(Long id, String name, Gender gender, LocalDate dayOfBirth, int timeOfBirth, String nickName, String profileImgUrl,  boolean isDeleted) {

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
                .isDeleted(false)
                .build();
    }

    public Baby delete() {
        return Baby.builder()
                .id(this.id)
                .name(this.name)
                .gender(this.gender)
                .nickName(this.nickName)
                .dayOfBirth(this.dayOfBirth)
                .timeOfBirth(this.timeOfBirth)
                .profileImgUrl(this.profileImgUrl)
                .isDeleted(true)
                .build();
    }

    public Baby update(BabyUpdate babyUpdate, String s3ImgUrl) {
        return Baby.builder()
                .id(this.id)
                .name(babyUpdate.name())
                .gender(babyUpdate.gender())
                .nickName(babyUpdate.nickName())
                .dayOfBirth(babyUpdate.dayOfBirth())
                .timeOfBirth(babyUpdate.timeOfBirth())
                .profileImgUrl(s3ImgUrl)
                .isDeleted(false)
                .build();
    }
}
