package com.tinyhuman.tinyhumanapi.baby.domain;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BabyTest {

    @Test
    @DisplayName("BabyCreate로 아기를 등록할 수 있다.")
    public void registerBaby() {
        BabyCreate babyCreate = BabyCreate.builder()
                .name("김가나")
                .gender(Gender.MALE)
                .nickName("초코")
                .timeOfBirth(10)
                .dayOfBirth(LocalDate.of(2022, 9, 20))
                .build();

        String s3ImgUrl = "https://image.com/1234";
        Baby baby = Baby.fromCreate(babyCreate, s3ImgUrl);
        
        assertThat(baby.name()).isEqualTo(babyCreate.name());
        assertThat(baby.gender()).isEqualTo(babyCreate.gender());
        assertThat(baby.nickName()).isEqualTo(babyCreate.nickName());
        assertThat(baby.timeOfBirth()).isEqualTo(babyCreate.timeOfBirth());
        assertThat(baby.dayOfBirth()).isEqualTo(babyCreate.dayOfBirth());
        assertThat(baby.profileImgKeyName()).isEqualTo(s3ImgUrl);

    }

}