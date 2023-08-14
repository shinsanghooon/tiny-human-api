package com.tinyhuman.tinyhumanapi.baby.domain;

import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record BabyCreate(
        @NotBlank(message = "이름을 입력해주세요.")
        @Size(max = 20, message = "이름의 최대 길이는 20자 입니다.")
        String name,

        @NotNull
        @Enumerated
        Gender gender,

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate dayOfBirth,

        @NotNull
        @Min(0)
        @Max(24)
        int timeOfBirth,

        @NotBlank(message = "애칭을 입력해주세요.")
        @Size(max = 20, message = "애칭의 최대 길이는 20자 입니다.")
        String nickName,

        String profileImgUrl) {
}