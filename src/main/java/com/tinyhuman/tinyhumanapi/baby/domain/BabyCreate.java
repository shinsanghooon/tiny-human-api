package com.tinyhuman.tinyhumanapi.baby.domain;

import com.tinyhuman.tinyhumanapi.baby.enums.Gender;

import java.time.LocalDate;

public record BabyCreate(String name, Gender gender, LocalDate dayOfBirth, int timeOfBirth,
                         String nickName, String profileImgUrl) {
}
