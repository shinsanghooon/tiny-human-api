package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.auth.mock.FakeAuthService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeBabyRepository;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeImageService;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeMultipartFile;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;
import com.tinyhuman.tinyhumanapi.diary.domain.SentenceCreate;
import com.tinyhuman.tinyhumanapi.diary.mock.FakeDiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.mock.FakePictureRepository;
import com.tinyhuman.tinyhumanapi.diary.mock.FakeSentenceRepository;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.UserBabyRole;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserBabyRelationRepository;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiaryServiceImplTest {

    private DiaryServiceImpl diaryServiceImpl;

    @BeforeEach
    void init() {

        FakeBabyRepository fakeBabyRepository = new FakeBabyRepository();
        FakeImageService fakeImageService = new FakeImageService();
        FakeDiaryRepository fakeDiaryRepository = new FakeDiaryRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeSentenceRepository fakeSentenceRepository = new FakeSentenceRepository();
        FakePictureRepository fakePictureRepository = new FakePictureRepository();
        FakeUserBabyRelationRepository fakeUserBabyRelationRepository = new FakeUserBabyRelationRepository();
        FakeAuthService fakeAuthService = new FakeAuthService();

        this.diaryServiceImpl = DiaryServiceImpl
                .builder()
                .babyRepository(fakeBabyRepository)
                .imageService(fakeImageService)
                .diaryRepository(fakeDiaryRepository)
                .sentenceRepository(fakeSentenceRepository)
                .pictureRepository(fakePictureRepository)
                .userRepository(fakeUserRepository)
                .userBabyRelationRepository(fakeUserBabyRelationRepository)
                .authService(fakeAuthService)
                .build();

        UserCreate userCreate1 = UserCreate.builder()
                .name("홈버그")
                .email("homebug@tinyhuman.com")
                .password("1234")
                .build();
        User user = fakeUserRepository.save(User.fromCreate(userCreate1));

        Baby baby = Baby.builder()
                .name("김가나")
                .gender(Gender.MALE)
                .nickName("초코")
                .timeOfBirth(20)
                .dayOfBirth(LocalDate.of(2022, 9, 20))
                .profileImgUrl("test_url")
                .isDeleted(false)
                .build();

        Baby savedBaby = fakeBabyRepository.save(baby);

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

        Diary diary = Diary.fromCreate(diaryCreate, savedBaby, user);
        fakeDiaryRepository.save(diary);

        fakeUserBabyRelationRepository.save(UserBabyRelation.builder()
                .user(User.builder().id(1L).build())
                .baby(Baby.builder().id(1L).build())
                .userBabyRole(UserBabyRole.ADMIN)
                .relation(FamilyRelation.FATHER)
                .build());

        fakeUserBabyRelationRepository.save(UserBabyRelation.builder()
                .user(User.builder().id(1L).build())
                .baby(Baby.builder().id(2L).build())
                .userBabyRole(UserBabyRole.UNKNOWN)
                .relation(FamilyRelation.MOTHER)
                .build());

    }

    @Nested
    @DisplayName("일기를 등록한다.")
    class RegisterDiary {
        @Test
        @DisplayName("DiaryCreate와 파일을 입력 받아 일기를 등록한다.")
        void registerDiary() {

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

            MultipartFile multipartFile = FakeMultipartFile.createMultipartFile(false);
            List<MultipartFile> diaryPictures = List.of(multipartFile);
            DiaryResponse diaryResponse = diaryServiceImpl.create(diaryCreate, diaryPictures);

            assertThat(diaryResponse.id()).isNotNull();
            assertThat(diaryResponse.daysAfterBirth()).isEqualTo(diaryCreate.daysAfterBirth());
            assertThat(diaryResponse.likeCount()).isEqualTo(diaryCreate.likeCount());
            assertThat(diaryResponse.sentences().size()).isEqualTo(diaryCreate.sentences().size());

            assertThat(diaryResponse.pictures().size()).isEqualTo(diaryPictures.size());
        }

        @Test
        @DisplayName("DiaryCreate의 BabyId에 대한 Baby가 없는 경우, 예외를 던진다.")
        void registerDiaryInvalidBaby() {
            List<SentenceCreate> sentenceCreateArrayList = IntStream.range(0, 5)
                    .mapToObj(i -> SentenceCreate.builder().sentence("안녕하세요[" + i + "]").build())
                    .toList();

            DiaryCreate diaryCreate = DiaryCreate.builder()
                    .babyId(9999L)
                    .daysAfterBirth(10)
                    .likeCount(0)
                    .userId(1L)
                    .sentences(sentenceCreateArrayList)
                    .build();

            MultipartFile multipartFile = FakeMultipartFile.createMultipartFile(false);
            List<MultipartFile> diaryPictures = List.of(multipartFile);

            assertThatThrownBy(() -> diaryServiceImpl.create(diaryCreate, diaryPictures))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }


    @Nested
    @DisplayName("일기를 조회한다.")
    class GetDiary {

        @BeforeEach
        void setUpDiary() {

            // Diary Id: 2L
            List<SentenceCreate> sentenceCreateArrayList = IntStream.range(0, 5)
                    .mapToObj(i -> SentenceCreate.builder().sentence("안녕하세요[" + i + "]").build())
                    .toList();

            DiaryCreate diaryCreate = DiaryCreate.builder()
                    .babyId(1L)
                    .daysAfterBirth(10)
                    .likeCount(100)
                    .userId(1L)
                    .sentences(sentenceCreateArrayList)
                    .build();

            diaryServiceImpl.create(diaryCreate, new ArrayList<MultipartFile>());

        }

        @Test
        @DisplayName("Diary Id를 입력 받아 일기를 조회한다.")
        void getDiary() {
            Long diaryId = 2L;
            DiaryResponse diary = diaryServiceImpl.findById(diaryId);

            assertThat(diary.id()).isEqualTo(diaryId);
            assertThat(diary.likeCount()).isEqualTo(100);
            assertThat(diary.writer()).isEqualTo("홈버그");
            assertThat(diary.sentences().size()).isEqualTo(5);
            assertThat(diary.pictures().size()).isEqualTo(0);

        }

        @Test
        @DisplayName("아기를 입력 받아 일기를 모두 조회한다.")
        void getDiariesByBaby() {
            Long babyId = 1L;

            List<DiaryResponse> myDiaries = diaryServiceImpl.getMyDiariesByBaby(babyId);
            assertThat(myDiaries.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("사용자와 아기 사이의 관계가 없는 경우 예외를 던진다.")
        void getInvalidRelationBetweenUserAndBaby() {
            Long babyId = 999L;
            assertThatThrownBy(() -> diaryServiceImpl.getMyDiariesByBaby(babyId))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("아기에 대한 일기 조회 권한이 없는 경우 예외를 던진다.")
        void unAuthorizedRelation() {
            Long babyId = 2L;
            assertThatThrownBy(() -> diaryServiceImpl.getMyDiariesByBaby(babyId))
                    .isInstanceOf(UnauthorizedAccessException.class);
        }


    }


    @Nested
    @DisplayName("일기를 삭제한다.")
    class DeleteDiary {

        @Test
        @DisplayName("DiaryId를 입력 받아 일기를 삭제한다.")
        void deleteDiary() {
            Long id = 1L;
            DiaryResponse diary = diaryServiceImpl.findById(id);

            Diary deletedDiary = diaryServiceImpl.delete(id);

            // 조회해서 결과가 없는 것으로 확인
            assertThat(deletedDiary.id()).isEqualTo(id);
            assertThat(diary.isDeleted()).isFalse();
            assertThat(deletedDiary.isDeleted()).isTrue();
        }

    }



}