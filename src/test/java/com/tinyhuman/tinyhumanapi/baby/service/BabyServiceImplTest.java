package com.tinyhuman.tinyhumanapi.baby.service;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeBabyRepository;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeImageService;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeMultipartFile;
import com.tinyhuman.tinyhumanapi.common.domain.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.diary.mock.FakeDiaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BabyServiceImplTest {

    private BabyServiceImpl babyServiceImpl;

    @BeforeEach
    void init() {

        FakeBabyRepository fakeBabyRepository = new FakeBabyRepository();
        FakeImageService fakeImageService = new FakeImageService();
        FakeDiaryRepository fakeDiaryRepository = new FakeDiaryRepository();

        this.babyServiceImpl = new BabyServiceImpl(fakeBabyRepository, fakeImageService, fakeDiaryRepository);

        Baby baby = Baby.builder()
                .name("김가나")
                .gender(Gender.MALE)
                .nickName("초코")
                .timeOfBirth(20)
                .dayOfBirth(LocalDate.of(2022, 9, 20))
                .profileImgUrl("test_url")
                .isDeleted(false)
                .build();

        fakeBabyRepository.save(baby);
    }
    @Test
    @DisplayName("BabyCreate을 이용하여 아기를 등록할 수 있다.")
    void registerBaby(){
        BabyCreate babyCreate = BabyCreate.builder()
                .name("김등록")
                .gender(Gender.MALE)
                .nickName("등록")
                .timeOfBirth(14)
                .dayOfBirth(LocalDate.of(2022, 9, 30))
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

    @Nested
    @DisplayName("아기를 삭제할 수 있다.")
    class BabyDelete {
        @Test
        @DisplayName("babyId를 입력 받아 아기를 삭제할 수 있다.")
        void deleteBaby() {
            Long babyId = 1L;
            babyServiceImpl.delete(babyId);

            assertThatThrownBy(() -> babyServiceImpl.findById(babyId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Baby에서 ID "+ babyId + "를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("아기가 삭제되면 아기의 일기도 모두 삭제된다.")
        void deleteDiariesOfBaby() {
            Long babyId = 1L;
            babyServiceImpl.delete(babyId);

            // TODO diary service에서 일기 조회 기능 추가
        }
    }
}