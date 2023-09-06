package com.tinyhuman.tinyhumanapi.baby.domain;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyPreSignedUrlResponse;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BabyResponseTest {

    @Test
    @DisplayName("Baby를 통해서 BabyResponse를 생성할 수 있다.")
    public void createBabyResponseFromBaby() {
        Baby baby = Baby.builder()
                .id(1L)
                .name("김가나")
                .gender(Gender.MALE)
                .nickName("초코")
                .timeOfBirth(10)
                .dayOfBirth(LocalDate.of(2022, 9, 20))
                .profileImgKeyName("https://image.com/1234")
                .build();

        BabyPreSignedUrlResponse babyResponse = BabyPreSignedUrlResponse.fromModel(baby, "presignedUrl");

        assertThat(babyResponse.id()).isEqualTo(baby.id());
        assertThat(babyResponse.name()).isEqualTo(baby.name());
        assertThat(babyResponse.gender()).isEqualTo(baby.gender());
        assertThat(babyResponse.nickName()).isEqualTo(baby.nickName());
        assertThat(babyResponse.timeOfBirth()).isEqualTo(baby.timeOfBirth());
        assertThat(babyResponse.dayOfBirth()).isEqualTo(baby.dayOfBirth());
        assertThat(babyResponse.preSignedUrl()).isEqualTo("presignedUrl");
    }

}