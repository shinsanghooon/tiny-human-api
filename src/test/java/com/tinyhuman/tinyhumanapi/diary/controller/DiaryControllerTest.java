package com.tinyhuman.tinyhumanapi.diary.controller;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.mock.TestContainer;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryResponse;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiaryControllerTest {

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
    @DisplayName("사용자는 일기를 조회할 수 있다.")
    class getDiary {
        @Test
        @DisplayName("사용자는 일기 상세 내용을 조회할 수 있다.")
        void getDiary() {

            ResponseEntity<DiaryResponse> result = testContainer.diaryController.getDiary(1L);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().id()).isEqualTo(1L);
            assertThat(result.getBody().sentences().size()).isEqualTo(3);
            assertThat(result.getBody().pictures().size()).isEqualTo(3);
            assertThat(result.getBody().likeCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("존재하지 않는 id를 요청하면 예외를 던진다.")
        void getDiaryWithInvalid() {
            assertThatThrownBy(() -> testContainer.diaryController.getDiary(9999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Diary");
        }

        @Test
        @DisplayName("커서 기반 페이징을 통해 일기를 조회할 수 있다. ")
        void getDiariesWithPaging() {
            // TODO: Pagination 적용
        }
    }

    @Nested
    @DisplayName("사용자는 일기를 삭제할 수 있다.")
    class deleteDiary {
        @Test
        @DisplayName("일기 id를 입력 받아 일기를 삭제한다.")
        void deleteDiary() {
            ResponseEntity<Void> result = testContainer.diaryController.deleteDiary(1L);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        }

        @Test
        @DisplayName("존재하지 않는 id를 요청하면 예외를 던진다.")
        void getDiaryWithInvalid() {
            assertThatThrownBy(() -> testContainer.diaryController.deleteDiary(9999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Diary");
        }
    }



}