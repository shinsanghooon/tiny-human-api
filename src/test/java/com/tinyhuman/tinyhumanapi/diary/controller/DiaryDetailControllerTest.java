package com.tinyhuman.tinyhumanapi.diary.controller;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.mock.TestContainer;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.*;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
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

import static org.assertj.core.api.Assertions.*;

class DiaryDetailControllerTest {

    TestContainer testContainer;

    @BeforeEach
    void setUp() {
        testContainer = TestContainer.builder()
                .uuidHolder(() -> "test-uuid-code")
                .build();

        // user id: 1
        testContainer.userRepository.save(User.builder()
                .name("유닛")
                .email("unit@unit.com")
                .password("unit")
                .build()
        );

        // baby id: 1
        testContainer.babyController.register(BabyCreate.builder()
                .name("아기")
                .gender(Gender.MALE)
                .nickName("아기별명")
                .relation(FamilyRelation.FATHER)
                .timeOfBirth(23)
                .dayOfBirth(LocalDate.of(2022, 9, 27))
                .fileName("profile.jpg")
                .build());

        // diary id: 1
        testContainer.diaryCreateController.createDiary(DiaryCreate.builder()
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
                .build());
    }

    @Nested
    @DisplayName("사용자는 일기의 글을 수정할 수 있다.")
    class UpdateSentence {

        @Test
        @DisplayName("글을 수정하고 200을 응답한다.")
        void updateSentence() {
            SentenceCreate updateSentence = SentenceCreate.builder()
                    .sentence("수정한 글입니다.")
                    .build();

            ResponseEntity<DiaryResponse> result =
                    testContainer.diaryDetailController.updateDiarySentence(1L, 1L, updateSentence);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(result.getBody().id()).isEqualTo(1L);
            assertThat(result.getBody().sentences())
                    .extracting("sentence")
                    .contains("수정한 글입니다.", "반갑습니다.", "감사합니다.");
            assertThat(result.getBody().sentences().stream()
                    .filter(s -> s.id().equals(1L))
                    .findFirst()
                    .get().sentence()).isEqualTo("수정한 글입니다.");
            assertThat(result.getBody().sentences().size()).isEqualTo(3);
        }

        @Test
        @DisplayName("존재하지 않는 diary id를 요청하면 예외를 던진다.")
        void updateSentenceWithInvalidDiaryId() {
            SentenceCreate updateSentence = SentenceCreate.builder()
                    .sentence("수정한 글입니다.")
                    .build();

            assertThatThrownBy(() -> testContainer.diaryDetailController.updateDiarySentence(9999L, 1L, updateSentence))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Diary");
        }

        @Test
        @DisplayName("존재하지 않는 sentence id를 요청하면 예외를 던진다.")
        void updateSentenceWithInvalidId() {
            SentenceCreate updateSentence = SentenceCreate.builder()
                    .sentence("수정한 글입니다.")
                    .build();

            assertThatThrownBy(() -> testContainer.diaryDetailController.updateDiarySentence(1L, 9999L, updateSentence))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Sentence");
        }
    }

    @Nested
    @DisplayName("사용자는 일기의 글을 삭제할 수 있다.")
    class DeleteSentence {

        @Test
        @DisplayName("일기 id를 입력 받아 일기를 삭제한다.")
        void deleteDiary() {
            ResponseEntity<Void> result = testContainer.diaryDetailController.deleteDiarySentence(1L, 1L);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        }

        @Test
        @DisplayName("존재하지 않는 diary id를 요청하면 예외를 던진다.")
        void deleteWithInvalidDiaryId() {
            assertThatThrownBy(() -> testContainer.diaryDetailController.deleteDiarySentence(9999L, 1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Diary");
        }

        @Test
        @DisplayName("존재하지 않는 sentence id를 요청하면 예외를 던진다.")
        void deleteWithInvalidId() {
            assertThatThrownBy(() -> testContainer.diaryDetailController.deleteDiarySentence(1L, 9999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Sentence");
        }
    }

    @Nested
    @DisplayName("사용자는 일기의 메인 사진을 변경할 수 있다.")
    class ChangeMainPictures {

        @Test
        @DisplayName("현재 메인 사진과 바꿀 메인 사진을 입력 받아 메인 사진을 변경한다.")
        void changeMainPicture() {

            Long currentMainPictureId = 1L;
            Long targetMainPictureId = 3L;

            Picture currentMain = testContainer.pictureRepository.findById(currentMainPictureId).get();
            Picture targetMain = testContainer.pictureRepository.findById(targetMainPictureId).get();

            ChangeMainPicture changeMainPicture = ChangeMainPicture.builder()
                    .currentPictureId(currentMainPictureId)
                    .newPictureId(targetMainPictureId)
                    .build();

            assertThat(currentMain.isMainPicture()).isTrue();
            assertThat(targetMain.isMainPicture()).isFalse();

            ResponseEntity<List<Picture>> result = testContainer.diaryDetailController.changeMainPicture(1L, changeMainPicture);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(result.getBody().size()).isEqualTo(2);
            assertThat(result.getBody())
                    .extracting("id", "isMainPicture")
                    .contains(tuple(1L, false),  tuple(3L, true));
        }

    }

}