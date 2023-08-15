package com.tinyhuman.tinyhumanapi.baby.service;

import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeBabyRepository;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeImageService;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeMultipartFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BabyServiceImplTest {

    private BabyServiceImpl babyServiceImpl;
    @BeforeEach
    void init() {

        FakeBabyRepository fakeBabyRepository = new FakeBabyRepository();
        FakeImageService fakeImageService = new FakeImageService();

        this.babyServiceImpl = new BabyServiceImpl(fakeBabyRepository, fakeImageService);
    }
    @Test
    @DisplayName("BabyCreate을 이용하여 아기를 등록할 수 있다.")
    void registerBaby(){
        BabyCreate babyCreate = BabyCreate.builder()
                .name("김가나")
                .gender(Gender.MALE)
                .nickName("초코")
                .timeOfBirth(20)
                .dayOfBirth(LocalDate.of(2022, 9, 20))
                .build();

        MultipartFile multipartFile = FakeMultipartFile.createMultipartFile();
        BabyResponse response = babyServiceImpl.register(babyCreate, multipartFile);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo(babyCreate.name());
        assertThat(response.gender()).isEqualTo(babyCreate.gender());
        assertThat(response.nickName()).isEqualTo(babyCreate.nickName());
        assertThat(response.timeOfBirth()).isEqualTo(babyCreate.timeOfBirth());
        assertThat(response.dayOfBirth()).isEqualTo(babyCreate.dayOfBirth());
        assertThat(response.profileImgUrl()).isNotNull();
    }

}