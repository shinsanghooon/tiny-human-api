package com.tinyhuman.tinyhumanapi.baby.domain;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyUpdate;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

public record Baby(Long id, String name, Gender gender, LocalDate dayOfBirth, int timeOfBirth, String nickName, String profileImgKeyName, boolean isDeleted) {

    @Builder
    public Baby {
    }

    public static Baby fromCreate(BabyCreate babyCreate) {
        return Baby.builder()
                .name(babyCreate.name())
                .gender(babyCreate.gender())
                .nickName(babyCreate.nickName())
                .dayOfBirth(babyCreate.dayOfBirth())
                .timeOfBirth(babyCreate.timeOfBirth())
                .isDeleted(false)
                .build();
    }

    public Baby with(String profileImgKeyName) {
        return Baby.builder()
                .id(this.id)
                .name(this.name)
                .gender(this.gender)
                .nickName(this.nickName)
                .dayOfBirth(this.dayOfBirth)
                .timeOfBirth(this.timeOfBirth)
                .profileImgKeyName(profileImgKeyName)
                .isDeleted(this.isDeleted)
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
                .profileImgKeyName(this.profileImgKeyName)
                .isDeleted(true)
                .build();
    }

    public Baby update(BabyUpdate babyUpdate) {
        return Baby.builder()
                .id(this.id)
                .name(babyUpdate.name())
                .gender(babyUpdate.gender())
                .nickName(babyUpdate.nickName())
                .dayOfBirth(babyUpdate.dayOfBirth())
                .timeOfBirth(babyUpdate.timeOfBirth())
                .profileImgKeyName(babyUpdate.keyName())
                .isDeleted(false)
                .build();
    }

    public Baby updateOnlyImage(String profileImgKeyName) {
        return Baby.builder()
                .id(this.id)
                .name(this.name)
                .gender(this.gender)
                .nickName(this.nickName)
                .dayOfBirth(this.dayOfBirth)
                .timeOfBirth(this.timeOfBirth)
                .profileImgKeyName(profileImgKeyName)
                .isDeleted(this.isDeleted)
                .build();
    }
}
