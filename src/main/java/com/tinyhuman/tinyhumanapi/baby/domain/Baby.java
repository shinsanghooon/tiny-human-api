package com.tinyhuman.tinyhumanapi.baby.domain;

import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

public record Baby(Long id, String name, Gender gender, LocalDate dayOfBirth, int timeOfBirth, String nickName, String profileImgUrl) {

    @Builder
    public Baby {
    }
}
