package com.tinyhuman.tinyhumanapi.baby.domain;

import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record BabyUpdate(
        @NotBlank(message = "이름을 입력해주세요.")
        @Size(max = 20, message = "이름의 최대 길이는 20자 입니다.")
        String name,

        @NotNull
        @Enumerated
        Gender gender,

        String keyName,

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

        @NotNull(message = "아기와의 관계를 입력해주세요.")
        @Enumerated
        FamilyRelation relation) {

    @Builder
    public BabyUpdate {
    }
}
