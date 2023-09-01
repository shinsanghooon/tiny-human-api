package com.tinyhuman.tinyhumanapi.baby.service;

import com.tinyhuman.tinyhumanapi.auth.mock.FakeAuthService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyUpdate;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeBabyRepository;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeImageService;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.diary.mock.FakeDiaryRepository;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserBabyRelationRepository;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserRepository;
import com.tinyhuman.tinyhumanapi.user.service.UserBabyRelationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeUserBabyRelationRepository fakeUserBabyRelationRepository = new FakeUserBabyRelationRepository();
        UserBabyRelationServiceImpl userBabyRelationService = new UserBabyRelationServiceImpl(fakeUserBabyRelationRepository);
        FakeAuthService fakeAuthService = new FakeAuthService();

        this.babyServiceImpl = BabyServiceImpl
                .builder()
                .babyRepository(fakeBabyRepository)
                .imageService(fakeImageService)
                .diaryRepository(fakeDiaryRepository)
                .userRepository(fakeUserRepository)
                .userBabyRelationService(userBabyRelationService)
                .authService(fakeAuthService)
                .build();

        UserCreate userCreate1 = UserCreate.builder()
                .name("홈버그")
                .email("homebug@tinyhuman.com")
                .password("1234")
                .build();

        UserCreate userCreate2 = UserCreate.builder()
                .name("겐지")
                .email("genzi@tinyhuman.com")
                .password("1234")
                .build();

        fakeUserRepository.save(User.fromCreate(userCreate1));
        fakeUserRepository.save(User.fromCreate(userCreate2));

        Baby baby = Baby.builder()
                .name("김가나")
                .gender(Gender.MALE)
                .nickName("초코")
                .timeOfBirth(20)
                .dayOfBirth(LocalDate.of(2022, 9, 20))
                .profileImgKeyName("test.png")
                .isDeleted(false)
                .build();

        fakeBabyRepository.save(baby);
    }

    @Nested
    @DisplayName("아기를 등록할 수 있다.")
    class RegisterBaby {


        @Test
        @DisplayName("BabyCreate을 이용하여 아기를 등록할 수 있다.")
        void registerBaby() {
            BabyCreate babyCreate = BabyCreate.builder()
                    .name("김등록")
                    .gender(Gender.MALE)
                    .nickName("등록")
                    .timeOfBirth(14)
                    .fileName("test.png")
                    .dayOfBirth(LocalDate.of(2022, 9, 30))
                    .build();

            BabyResponse response = babyServiceImpl.register(babyCreate);

            assertThat(response.id()).isNotNull();
            assertThat(response.name()).isEqualTo(babyCreate.name());
            assertThat(response.gender()).isEqualTo(babyCreate.gender());
            assertThat(response.nickName()).isEqualTo(babyCreate.nickName());
            assertThat(response.timeOfBirth()).isEqualTo(babyCreate.timeOfBirth());
            assertThat(response.dayOfBirth()).isEqualTo(babyCreate.dayOfBirth());
            assertThat(response.preSignedUrl()).contains("images/1/baby/profile/" + babyCreate.fileName());
        }
    }

    @Nested
    @DisplayName("아기를 조회할 수 있다.")
    class GetBaby {
        @Test
        @DisplayName("BabyId를 입력 받아 아기를 조회할 수 있다.")
        void findBaby() {
            Long babyId = 1L;
            BabyResponse baby = babyServiceImpl.findById(babyId);

            assertThat(baby.id()).isEqualTo(babyId);
            assertThat(baby.name()).isEqualTo("김가나");
            assertThat(baby.gender()).isEqualTo(Gender.MALE);
            assertThat(baby.dayOfBirth()).isEqualTo(LocalDate.of(2022, 9, 20));
        }
    }

    @Nested
    @DisplayName("아기 정보를 수정할 수 있다.")
    class UpdateBaby {

        @Test
        @DisplayName("아기 정보를 수정할 수 있다.")
        void updateBabyWithoutImage() {
            BabyUpdate babyUpdate = BabyUpdate.builder()
                    .name("김수정")
                    .gender(Gender.MALE)
                    .nickName("김진짜")
                    .dayOfBirth(LocalDate.of(2022, 10, 4))
                    .timeOfBirth(18)
                    .build();

            BabyResponse updatedUser = babyServiceImpl.update(1L, babyUpdate);

            assertThat(updatedUser.id()).isNotNull();
            assertThat(updatedUser.name()).isEqualTo(babyUpdate.name());
            assertThat(updatedUser.gender()).isEqualTo(babyUpdate.gender());
            assertThat(updatedUser.nickName()).isEqualTo(babyUpdate.nickName());
            assertThat(updatedUser.dayOfBirth()).isEqualTo(babyUpdate.dayOfBirth());
            assertThat(updatedUser.timeOfBirth()).isEqualTo(babyUpdate.timeOfBirth());

        }
        @Test
        @DisplayName("아기 정보와 프로필 사진을 수정할 수 있다.")
        void updateBabyWithFile() {
            String newFile = "update.png";
            BabyResponse originalUser = babyServiceImpl.findById(1L);
            BabyResponse updatedUser = babyServiceImpl.updateProfileImage(1L, newFile);

            assertThat(updatedUser.id()).isNotNull();
            assertThat(updatedUser.name()).isEqualTo(originalUser.name());
            assertThat(updatedUser.gender()).isEqualTo(originalUser.gender());
            assertThat(updatedUser.nickName()).isEqualTo(originalUser.nickName());
            assertThat(updatedUser.dayOfBirth()).isEqualTo(originalUser.dayOfBirth());
            assertThat(updatedUser.timeOfBirth()).isEqualTo(originalUser.timeOfBirth());
            assertThat(updatedUser.preSignedUrl()).contains(newFile);
        }
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