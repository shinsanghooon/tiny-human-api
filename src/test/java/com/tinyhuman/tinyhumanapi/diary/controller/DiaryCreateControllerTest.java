package com.tinyhuman.tinyhumanapi.diary.controller;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.common.mock.TestContainer;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryPreSignedUrlResponse;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.PictureCreate;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.SentenceCreate;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DiaryCreateControllerTest {

    @Nested
    @DisplayName("사용자는 일기를 등록할 수 있다.")
    class createDiary {

        TestContainer testContainer;

        @BeforeEach
        void setUp() {
            testContainer = TestContainer.builder()
                    .uuidHolder(() -> "test-uuid-code")
                    .build();

            testContainer.userRepository.save(User.builder()
                    .name("유닛")
                    .email("unit@unit.com")
                    .password("unit")
                    .build()
            );

            testContainer.babyController.register(BabyCreate.builder()
                    .name("아기")
                    .gender(Gender.MALE)
                    .nickName("아기별명")
                    .relation(FamilyRelation.FATHER)
                    .timeOfBirth(23)
                    .dayOfBirth(LocalDate.of(2022, 9, 27))
                    .fileName("profile.jpg")
                    .build());
        }

        @Test
        @DisplayName("글, 사진을 입력하여 일기를 작성한다.")
        void createDiary() {
            DiaryCreate diaryCreate = DiaryCreate.builder()
                    .babyId(1L)
                    .daysAfterBirth(20)
                    .userId(1L)
                    .likeCount(0)
                    .sentences(List.of(SentenceCreate.builder().sentence("안녕하세요.").build(),
                            SentenceCreate.builder().sentence("반갑습니다.").build(),
                            SentenceCreate.builder().sentence("감사합니다.").build()))
                    .files(List.of(PictureCreate.builder().fileName("hello.jpg").build(),
                            PictureCreate.builder().fileName("nicetomeetyou.jpg").build(),
                            PictureCreate.builder().fileName("thankyou.jpg").build()))
                    .build();

            ResponseEntity<DiaryPreSignedUrlResponse> result = testContainer.diaryCreateController.createDiary(diaryCreate);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().id()).isEqualTo(1L);
            assertThat(result.getBody().sentences().size()).isEqualTo(3);
            assertThat(result.getBody().pictures().size()).isEqualTo(3);
            assertThat(result.getBody().likeCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("글을 입력하여 일기를 작성한다. 사진은 필수가 아니다.")
        void createDiaryWithoutPicture() {
            DiaryCreate diaryCreate = DiaryCreate.builder()
                    .babyId(1L)
                    .daysAfterBirth(20)
                    .userId(1L)
                    .likeCount(0)
                    .sentences(List.of(SentenceCreate.builder().sentence("안녕하세요.").build(),
                            SentenceCreate.builder().sentence("반갑습니다.").build(),
                            SentenceCreate.builder().sentence("감사합니다.").build()))
                    .files(null)
                    .build();

            ResponseEntity<DiaryPreSignedUrlResponse> result = testContainer.diaryCreateController.createDiary(diaryCreate);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().id()).isEqualTo(1L);
            assertThat(result.getBody().sentences().size()).isEqualTo(3);
            assertThat(result.getBody().pictures()).isNull();
            assertThat(result.getBody().likeCount()).isEqualTo(0);
        }

    }


}