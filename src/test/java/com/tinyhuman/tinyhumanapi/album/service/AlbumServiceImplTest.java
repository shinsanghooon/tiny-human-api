package com.tinyhuman.tinyhumanapi.album.service;

import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumResponse;
import com.tinyhuman.tinyhumanapi.album.mock.FakeAlbumRepository;
import com.tinyhuman.tinyhumanapi.auth.mock.FakeAuthService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeImageService;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeMultipartFile;
import com.tinyhuman.tinyhumanapi.common.domain.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.UserBabyRole;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserBabyRelationRepository;
import org.junit.jupiter.api.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlbumServiceImplTest {

    private AlbumServiceImpl albumServiceImpl;

    @BeforeEach
    void init() {
        FakeUserBabyRelationRepository fakeUserBabyRelationRepository = new FakeUserBabyRelationRepository();
        FakeImageService fakeImageService = new FakeImageService();
        FakeAuthService fakeAuthService = new FakeAuthService();
        FakeAlbumRepository fakeAlbumRepository = new FakeAlbumRepository();

        this.albumServiceImpl = AlbumServiceImpl.builder()
                .albumRepository(fakeAlbumRepository)
                .authService(fakeAuthService)
                .imageService(fakeImageService)
                .userBabyRelationRepository(fakeUserBabyRelationRepository)
                .build();

        User user = User.builder()
                .id(1L)
                .name("홈버그")
                .email("homebug@tinyhuman.com")
                .password("1234")
                .status(UserStatus.ACTIVE)
                .build();

        Baby baby = Baby.builder()
                .id(1L)
                .name("김가나")
                .gender(Gender.MALE)
                .nickName("초코")
                .timeOfBirth(20)
                .dayOfBirth(LocalDate.of(2022, 9, 20))
                .profileImgUrl("test_url")
                .isDeleted(false)
                .build();

        Baby baby2 = Baby.builder()
                .id(2L)
                .name("딸기")
                .gender(Gender.FEMALE)
                .nickName("딸기")
                .timeOfBirth(12)
                .dayOfBirth(LocalDate.of(2022, 5, 20))
                .profileImgUrl("baby")
                .isDeleted(false)
                .build();


        UserBabyRelation relation = UserBabyRelation.builder()
                .user(user)
                .baby(baby)
                .relation(FamilyRelation.FATHER)
                .userBabyRole(UserBabyRole.ADMIN)
                .build();

        fakeUserBabyRelationRepository.save(relation);

        UserBabyRelation relation2 = UserBabyRelation.builder()
                .user(user)
                .baby(baby2)
                .relation(FamilyRelation.BROTHER)
                .userBabyRole(UserBabyRole.ADMIN)
                .build();

        fakeUserBabyRelationRepository.save(relation);
        fakeUserBabyRelationRepository.save(relation2);

    }

    @Nested
    class UploadAlbums{
        List<MultipartFile> files = new ArrayList<>();

        @BeforeEach
        void fileSetup() {

            for (int i = 0; i < 5; i++) {
                String name = "image_" + i;
                String originalFileName = "original_" + i;
                String contentType = "image/png";
                int byteLength = 2000000 * (i + 1); // 2MB
                MultipartFile file = FakeMultipartFile.creatMockMultipartFile(name, originalFileName, contentType, byteLength);
                files.add(file);
            }
        }

        @AfterEach
        void fileClear() {
            files.clear();
        }

        @DisplayName("파일을 입력 받아 사진 및 영상을 등록한다.")
        @Test
        void uploadAlbums() {
            List<AlbumResponse> albumResponses = albumServiceImpl.uploadAlbums(1L, files);

            assertThat(albumResponses.size()).isEqualTo(files.size());
            assertThat(albumResponses).extracting("babyId").containsOnly(1L);
        }

        @DisplayName("엄마 아빠가 아니라면, 사진 및 영상을 등록 시 예외를 던진다.")
        @Test
        void canUploadWhenMotherOrFather() {
            assertThatThrownBy(() -> albumServiceImpl.uploadAlbums(2L, files))
                    .isInstanceOf(UnauthorizedAccessException.class)
                    .hasMessageContaining("접근 권한이 없습니다");
        }

        @DisplayName("사진 용량이 15MB를 넘으면 예외를 던진다.")
        @Test
        void over15MB() {

            String name = "image_over" ;
            String originalFileName = "original_over";
            String contentType = "image/png";
            int byteLength = 2000000 * 100; // 2MB
            MultipartFile file = FakeMultipartFile.creatMockMultipartFile(name, originalFileName, contentType, byteLength);
            files.add(file);

            assertThatThrownBy(() -> albumServiceImpl.uploadAlbums(1L, files))
                    .isInstanceOf(MaxUploadSizeExceededException.class)
                    .hasMessageContaining("bytes exceeded");

        }
    }







}