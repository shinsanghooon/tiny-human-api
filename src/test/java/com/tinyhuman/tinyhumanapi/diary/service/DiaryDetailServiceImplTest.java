package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryResponse;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.SentenceCreate;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.SentenceResponse;
import com.tinyhuman.tinyhumanapi.diary.domain.*;
import com.tinyhuman.tinyhumanapi.diary.mock.FakeDiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.mock.FakePictureRepository;
import com.tinyhuman.tinyhumanapi.diary.mock.FakeSentenceRepository;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiaryDetailServiceImplTest {

    private DiaryDetailServiceImpl diaryDetailServiceImpl;

    @BeforeEach
    void init() {
        FakeDiaryRepository fakeDiaryRepository = new FakeDiaryRepository();
        FakeSentenceRepository fakeSentenceRepository = new FakeSentenceRepository();
        FakePictureRepository fakePictureRepository = new FakePictureRepository();

        diaryDetailServiceImpl = diaryDetailServiceImpl.builder()
                .diaryRepository(fakeDiaryRepository)
                .sentenceRepository(fakeSentenceRepository)
                .pictureRepository(fakePictureRepository)
                .build();

        User user = User.builder()
                .id(1L)
                .name("홈버그")
                .email("homebug@tinyhuman.com")
                .password("1234")
                .status(UserStatus.ACTIVE)
                .build();

        Baby baby = Baby.builder()
                .name("김가나")
                .gender(Gender.MALE)
                .nickName("초코")
                .timeOfBirth(20)
                .dayOfBirth(LocalDate.of(2022, 9, 20))
                .profileImgKeyName("test_url")
                .isDeleted(false)
                .build();

        List<SentenceCreate> sentenceCreateArrayList = IntStream.range(0, 5)
                .mapToObj(i -> SentenceCreate.builder().sentence("안녕하세요[" + i + "]").build())
                .toList();

        DiaryCreate diaryCreate = DiaryCreate.builder()
                .babyId(1L)
                .daysAfterBirth(10)
                .likeCount(0)
                .userId(1L)
                .sentences(sentenceCreateArrayList)
                .build();

        Diary diary = Diary.fromCreate(diaryCreate, baby, user);
        Diary savedDiary = fakeDiaryRepository.save(diary);

        List<Sentence> sentences = Sentence.from(diaryCreate)
                .stream()
                .map(s -> Sentence.builder()
                        .sentence(s.sentence())
                        .diaryId(savedDiary.id())
                        .build())
                .toList();
        List<Sentence> savedSentences = fakeSentenceRepository.saveAll(sentences, savedDiary);
        Diary diaryWithSentence = savedDiary.addSentences(savedSentences);
        fakeSentenceRepository.saveAll(sentences, diaryWithSentence);

        Picture picture1 = Picture.builder()
                .isMainPicture(true)
                .diaryId(1L)
                .build();

        Picture picture2 = Picture.builder()
                .isMainPicture(false)
                .diaryId(1L)
                .build();

        Picture picture3 = Picture.builder()
                .isMainPicture(false)
                .diaryId(1L)
                .build();


        List<Picture> pictures = List.of(picture1, picture2, picture3);

        List<Picture> savedPictures = fakePictureRepository.saveAll(pictures, diaryWithSentence);
        Diary diaryWithSentenceAndPicture = diaryWithSentence.addPictures(savedPictures);

        fakeDiaryRepository.save(diaryWithSentenceAndPicture);
        fakePictureRepository.saveAll(savedPictures, diaryWithSentenceAndPicture);

    }

    @Nested
    @DisplayName("글 정보를 수정할 수 있다.")
    class UpdateSentence {

        @Test
        @DisplayName("diaryId와 sentenceId를 입력 받아 글을 수정할 수 있다.")
        void updateSentence() {
            Long updateSentenceId = 1L;
            DiaryResponse diaryResponse = diaryDetailServiceImpl.updateSentence(1L, updateSentenceId, new SentenceCreate("수정된 글 입니다."));

            List<SentenceResponse> sentences = diaryResponse.sentences();
            SentenceResponse sentenceResponse = sentences.stream()
                    .filter(s -> s.id().equals(updateSentenceId)).findAny().get();

            assertThat(sentenceResponse.sentence()).isEqualTo("수정된 글 입니다.");
        }

        @Test
        @DisplayName("sentenceId에 해당하는 글이 없다면 예외를 던진다.")
        void updateSentenceButException() {
            Long updateSentenceId = 999L;

            assertThatThrownBy(() -> {
                diaryDetailServiceImpl.updateSentence(1L, updateSentenceId, new SentenceCreate("수정된 글 입니다."));
            }).isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Sentence");
        }
    }

    @Nested
    @DisplayName("글 정보를 삭제한다.")
    class DeleteSentence {
        @Test
        @DisplayName("diaryId와 sentenceId를 입력 받아 글 정보를 삭제한다.")
        void deleteSentence() {
            Long deleteSentenceId = 1L;
            Sentence sentence = diaryDetailServiceImpl.deleteSentence(1L, deleteSentenceId);

            assertThat(sentence.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("sentenceId에 해당하는 글이 없다면 예외를 던진다.")
        void updateSentenceButException() {
            Long deleteSentenceId = 999L;

            assertThatThrownBy(() -> {
                diaryDetailServiceImpl.deleteSentence(1L, deleteSentenceId);
            }).isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Sentence");
        }
    }

    @Nested
    @DisplayName("메인 사진을 변경한다.")
    class ChangeMainPicture {

        @Test
        @DisplayName("기존 메인 Picture Id와 변경할 메인 Picture Id를 입력 받아 메인 사진을 변경한다.")
        void changeMainPicture() {

            Long diaryId = 1L;
            Long currentMainPictureId = 1L;
            Long newMainPictureId = 2L;

            List<Picture> pictures = diaryDetailServiceImpl.changeMainPicture(diaryId, currentMainPictureId, newMainPictureId);
            Picture mainToNormalPicture = pictures.get(0);
            Picture normalToMainPicture = pictures.get(1);

            assertThat(mainToNormalPicture.isMainPicture()).isFalse();
            assertThat(normalToMainPicture.isMainPicture()).isTrue();
        }

        @Test
        @DisplayName("사진을 삭제한다.")
        void deletePicture() {
            Long deletePictureId = 1L;
            Picture picture = diaryDetailServiceImpl.deletePicture(1L, deletePictureId);

            assertThat(picture.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("메인 사진을 삭제하면 id값이 가장 작은 사진이 메인 사진이 된다.")
        void deleteMainPicture() {
            Long deletePictureId = 1L;
            diaryDetailServiceImpl.deletePicture(1L, deletePictureId);

            Picture picture2 = diaryDetailServiceImpl.findPictureById(2L);
            Picture picture3 = diaryDetailServiceImpl.findPictureById(3L);

            assertThat(picture2.isMainPicture()).isTrue();
            assertThat(picture3.isMainPicture()).isFalse();
        }
    }




}