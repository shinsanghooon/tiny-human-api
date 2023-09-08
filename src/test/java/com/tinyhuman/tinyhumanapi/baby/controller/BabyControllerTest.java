package com.tinyhuman.tinyhumanapi.baby.controller;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.*;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.mock.TestContainer;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BabyControllerTest {

    @Nested
    @DisplayName("사용자는 아기를 등록할 수 있다.")
    class registerBaby {
        @Test
        @DisplayName("BabyCreate을 요청하여 아기를 등록할 수 있다.")
        void registerBaby() {
            String profileImagePrefix = "baby/1/profile/";
            String testUuid = "test-uuid-code";
            String testFileName = "test.jpg";

            TestContainer testContainer = TestContainer.builder()
                    .uuidHolder(() -> testUuid)
                    .build();

            BabyCreate babyCreate = BabyCreate.builder()
                    .name("아기")
                    .gender(Gender.MALE)
                    .nickName("아기별명")
                    .relation(FamilyRelation.FATHER)
                    .timeOfBirth(23)
                    .dayOfBirth(LocalDate.of(2022, 9, 27))
                    .fileName(testFileName)
                    .build();

            ResponseEntity<BabyPreSignedUrlResponse> result = testContainer.babyController.register(babyCreate);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
            assertThat(Objects.requireNonNull(result.getBody()).name()).isEqualTo("아기");
            assertThat(result.getBody().gender()).isEqualTo(Gender.MALE);
            assertThat(result.getBody().nickName()).isEqualTo("아기별명");
            assertThat(result.getBody().timeOfBirth()).isEqualTo(23);
            assertThat(result.getBody().dayOfBirth()).isEqualTo(LocalDate.of(2022, 9, 27));
            assertThat(result.getBody().preSignedUrl()).isEqualTo(profileImagePrefix + testUuid + "_" + testFileName);
        }

    }


    @Nested
    @DisplayName("사용자는 나에게 등록된 아기를 조회할 수 있다.")
    class getMyBaby {
        @Test
        @DisplayName("등록된 아기가 없는 경우 빈 리스트를 조회한다.")
        void hasNoBaby() {
            String testUuid = "test-uuid-code";

            TestContainer testContainer = TestContainer.builder()
                    .uuidHolder(() -> testUuid)
                    .build();

            ResponseEntity<List<BabyResponse>> result = testContainer.babyController.getMyBabies();

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(Objects.requireNonNull(result.getBody()).size()).isZero();
        }

        @Test
        @DisplayName("등록된 아기 수만큼 조회할 수 있다.")
        void hasBabies() {
            String testUuid = "test-uuid-code";
            String testFileName = "test.jpg";
            String testFileName2 = "test2.jpg";

            TestContainer testContainer = TestContainer.builder()
                    .uuidHolder(() -> testUuid)
                    .build();

            BabyCreate babyCreate1 = BabyCreate.builder()
                    .name("아기")
                    .gender(Gender.MALE)
                    .nickName("아기별명")
                    .relation(FamilyRelation.FATHER)
                    .timeOfBirth(23)
                    .dayOfBirth(LocalDate.of(2022, 9, 27))
                    .fileName(testFileName)
                    .build();

            BabyCreate babyCreate2 = BabyCreate.builder()
                    .name("어린이")
                    .gender(Gender.FEMALE)
                    .nickName("뜀이")
                    .relation(FamilyRelation.FATHER)
                    .timeOfBirth(5)
                    .dayOfBirth(LocalDate.of(2022, 12, 12))
                    .fileName(testFileName2)
                    .build();

            testContainer.babyService.register(babyCreate1);
            testContainer.babyService.register(babyCreate2);

            ResponseEntity<List<BabyResponse>> result = testContainer.babyController.getMyBabies();

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(Objects.requireNonNull(result.getBody()).size()).isEqualTo(2);
            assertThat(result.getBody())
                    .extracting("name")
                    .containsOnly("아기", "어린이");
            assertThat(result.getBody())
                    .extracting("nickName")
                    .containsOnly("아기별명", "뜀이");
            assertThat(result.getBody())
                    .extracting("timeOfBirth")
                    .containsOnly(23, 5);
            assertThat(result.getBody())
                    .extracting("profileImgKeyName")
                    .containsOnly("baby/1/profile/test-uuid-code_test.jpg", "baby/2/profile/test-uuid-code_test2.jpg");
        }
    }


    @Nested
    @DisplayName("사용자는 아기를 삭제할 수 있다.")
    class deleteBaby {
        @Test
        @DisplayName("아기 id를 요청하여 데이터를 삭제할 수 있다.")
        void deleteBabies() {
            TestContainer testContainer = TestContainer.builder()
                    .uuidHolder(() -> "test-uuid-code")
                    .build();

            testContainer.babyService.register(BabyCreate.builder()
                    .name("아기")
                    .gender(Gender.MALE)
                    .nickName("아기별명")
                    .relation(FamilyRelation.FATHER)
                    .timeOfBirth(23)
                    .dayOfBirth(LocalDate.of(2022, 9, 27))
                    .fileName("test.jpg")
                    .build());

            ResponseEntity<Void> result = testContainer.babyController.delete(1L);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
            assertThat(result.getBody()).isNull();

            assertThatThrownBy(() -> testContainer.babyController.delete(1L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("존재하지 않은 id에 삭제 요청하는 경우 400 응답을 한다.")
        void deleteInvalidBabies() {
            TestContainer testContainer = TestContainer.builder()
                    .uuidHolder(() -> "test-uuid-code")
                    .build();

            assertThatThrownBy(() -> testContainer.babyController.delete(9999L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("사용자는 아기 정보를 수정할 수 있다.")
    class updateBaby{

        TestContainer testContainer;
        @BeforeEach
        void babySetUp() {
            testContainer = TestContainer.builder()
                    .uuidHolder(() -> "test-uuid-code")
                    .build();

            testContainer.babyService.register(BabyCreate.builder()
                    .name("아기")
                    .gender(Gender.MALE)
                    .nickName("아기별명")
                    .relation(FamilyRelation.FATHER)
                    .timeOfBirth(23)
                    .dayOfBirth(LocalDate.of(2022, 9, 27))
                    .fileName("test.jpg")
                    .build());
        }

        @Test
        @DisplayName("이미지를 제외한 아기 정보를 수정할 수 있다.")
        void updateBaby() {

            BabyUpdate babyUpdate = BabyUpdate.builder()
                    .name("아기")
                    .gender(Gender.MALE)
                    .nickName("빵구")
                    .relation(FamilyRelation.FATHER)
                    .timeOfBirth(23)
                    .dayOfBirth(LocalDate.of(2022, 9, 27))
                    .build();

            ResponseEntity<BabyResponse> result = testContainer.babyController.update(1L, babyUpdate);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(Objects.requireNonNull(result.getBody()).id()).isEqualTo(1L);
            assertThat(Objects.requireNonNull(result.getBody()).name()).isEqualTo("아기");
            assertThat(result.getBody().gender()).isEqualTo(Gender.MALE);
            assertThat(result.getBody().nickName()).isEqualTo("빵구");
            assertThat(result.getBody().timeOfBirth()).isEqualTo(23);
            assertThat(result.getBody().dayOfBirth()).isEqualTo(LocalDate.of(2022, 9, 27));
        }

        @Test
        @DisplayName("아기 프로필 이미지를 수정할 수 있다.")
        void updateBabyProfileImage() {
            BabyImageUpdate babyImageUpdate = BabyImageUpdate.builder()
                    .fileName("newface.png")
                    .build();

            ResponseEntity<BabyPreSignedUrlResponse> result = testContainer.babyController.updateProfileImage(1L, babyImageUpdate);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(Objects.requireNonNull(result.getBody()).id()).isEqualTo(1L);
            assertThat(result.getBody().preSignedUrl()).contains("newface.png", "test-uuid-code");
        }

        @Test
        @DisplayName("존재하지 않은 id에 수정 요청하는 경우 400 응답을 한다.")
        void updateInvalidBabies() {
            assertThatThrownBy(() -> testContainer.babyController.update(9999L, BabyUpdate.builder().build()))
                    .isInstanceOf(ResourceNotFoundException.class);

            assertThatThrownBy(() -> testContainer.babyController.updateProfileImage(9999L, BabyImageUpdate.builder().build()))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}