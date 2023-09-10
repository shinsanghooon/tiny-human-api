package com.tinyhuman.tinyhumanapi.album.service;

import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumCreate;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumDelete;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumResponse;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumUploadResponse;
import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.album.mock.FakeAlbumRepository;
import com.tinyhuman.tinyhumanapi.auth.mock.FakeAuthService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeImageService;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.common.mock.TestUuidHolder;
import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import com.tinyhuman.tinyhumanapi.common.utils.PageCursor;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.UserBabyRole;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserBabyRelationRepository;
import org.junit.jupiter.api.*;

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
        TestUuidHolder testUuidHolder = new TestUuidHolder("test-uuid");

        this.albumServiceImpl = AlbumServiceImpl.builder()
                .albumRepository(fakeAlbumRepository)
                .authService(fakeAuthService)
                .imageService(fakeImageService)
                .userBabyRelationRepository(fakeUserBabyRelationRepository)
                .uuidHolder(testUuidHolder)
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
                .profileImgKeyName("test_url")
                .isDeleted(false)
                .build();

        Baby baby2 = Baby.builder()
                .id(2L)
                .name("딸기")
                .gender(Gender.FEMALE)
                .nickName("딸기")
                .timeOfBirth(12)
                .dayOfBirth(LocalDate.of(2022, 5, 20))
                .profileImgKeyName("baby")
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
    @DisplayName("사진 및 영상을 업로드 한다.")
    class UploadAlbums {
        List<AlbumCreate> files = new ArrayList<>();

        @BeforeEach
        void fileSetup() {
            for (int i = 0; i < 10; i++) {
                String originalFileName = "original_" + i + ".png";
                files.add(AlbumCreate.builder().fileName(originalFileName).build());
            }
        }

        @AfterEach
        void fileClear() {
            files.clear();
        }

        @DisplayName("파일을 입력 받아 사진 및 영상을 등록한다.")
        @Test
        void uploadAlbums() {
            List<AlbumUploadResponse> albumUploadResponses = albumServiceImpl.uploadAlbums(1L, files);

            assertThat(albumUploadResponses.size()).isEqualTo(files.size());
            assertThat(albumUploadResponses).extracting("babyId").containsOnly(1L);
        }

        @DisplayName("엄마 아빠가 아니라면, 사진 및 영상을 등록 시 예외를 던진다.")
        @Test
        void canUploadWhenMotherOrFather() {
            assertThatThrownBy(() -> albumServiceImpl.uploadAlbums(2L, files))
                    .isInstanceOf(UnauthorizedAccessException.class)
                    .hasMessageContaining("접근 권한이 없습니다");
        }
    }


    @Nested
    @DisplayName("앨범을 삭제한다.")
    class DeleteAlbums {

        List<AlbumCreate> files = new ArrayList<>();

        @BeforeEach
        @DisplayName("임시 이미지 파일 5개를 미리 저장해둔다.")
        void fileSetup() {
            for (int i = 0; i < 5; i++) {
                String originalFileName = "original_" + i + ".png";
                files.add(AlbumCreate.builder().fileName(originalFileName).build());
            }

            albumServiceImpl.uploadAlbums(1L, files);
        }

        @Test
        @DisplayName("삭제할 앨범 id를 입력받아 앨범을 삭제한다.")
        void deleteAlbums() {

            List<Long> deleteIds = List.of(1L, 2L);

            AlbumDelete albumDelete = AlbumDelete.builder()
                    .ids(deleteIds)
                    .build();

            List<Album> deletedAlbums = albumServiceImpl.delete(albumDelete);

            assertThat(deletedAlbums)
                    .extracting("id").contains(1L, 2L);
        }
    }


    @Nested
    @DisplayName("앨범을 상세 조회한다.")
    class GetAlbums {

        List<AlbumCreate> files = new ArrayList<>();
        Long babyId = 1L;

        @BeforeEach
        @DisplayName("임시 이미지 파일 10개를 미리 저장해둔다.")
        void fileSetup() {
            for (int i = 0; i < 20; i++) {
                String originalFileName = "original_" + i + ".png";
                files.add(AlbumCreate.builder().fileName(originalFileName).build());
            }

            albumServiceImpl.uploadAlbums(babyId, files);
        }

        @Test
        @DisplayName("아기 id와 앨범 id를 입력 받아 상세 정보를 조회한다.")
        void getAlbumDetail() {
            Long albumId = 4L;
            AlbumResponse album = albumServiceImpl.findByIdAndBabyId(albumId, babyId);

            assertThat(album.id()).isEqualTo(albumId);
            assertThat(album.babyId()).isEqualTo(babyId);
            assertThat(album.contentType()).isEqualTo(ContentType.PHOTO);
            assertThat(album.keyName()).contains("original_3");

        }
    }

    @Nested
    @DisplayName("앨범 전체를 조회한다. 아기 id와 CursorRequest를 입력 받아 페이징 방식으로 조회한다.")
    class GetAllAlbums {

        List<AlbumCreate> files = new ArrayList<>();
        Long babyId = 1L;

        @BeforeEach
        @DisplayName("임시 이미지 파일 10개를 미리 저장해둔다.")
        void fileSetup() {
            for (int i = 0; i < 20; i++) {
                String originalFileName = "original_" + i + ".png";
                files.add(AlbumCreate.builder().fileName(originalFileName).build());
            }

            albumServiceImpl.uploadAlbums(babyId, files);
        }

        @Test
        @DisplayName("첫 페이지 조회 시, 입력받은 size만큼 id 내림차순으로 조회한다.")
        void getAllAlbum() {

            PageCursor<AlbumResponse> albumCursor = albumServiceImpl.getAlbumsByBaby(babyId, new CursorRequest(null, 5));
            List<AlbumResponse> albums = albumCursor.body();

            assertThat(albums.size()).isEqualTo(5);
            assertThat(albums).extracting("babyId").containsOnly(babyId);
            assertThat(albumCursor.nextCursorRequest().hasKey()).isTrue();
            assertThat(albums.stream().mapToLong(AlbumResponse::id).max().orElse(CursorRequest.NONE_KEY)).isEqualTo(20L);
            assertThat(albums.stream().mapToLong(AlbumResponse::id).min().orElse(CursorRequest.NONE_KEY)).isEqualTo(16L);
            assertThat(albumCursor.nextCursorRequest().key()).isEqualTo(16);

        }

        @Test
        @DisplayName("두번째 조회부터, 입력 받은 key값부터 size만큼 id 내림차순으로 조회한다.")
        void getAllAlbumWithCursor() {

            PageCursor<AlbumResponse> albumCursor = albumServiceImpl.getAlbumsByBaby(babyId, new CursorRequest(16L, 7));
            List<AlbumResponse> albums = albumCursor.body();

            assertThat(albums.size()).isEqualTo(7);
            assertThat(albums).extracting("babyId").containsOnly(babyId);
            assertThat(albumCursor.nextCursorRequest().hasKey()).isTrue();
            assertThat(albums.stream().mapToLong(AlbumResponse::id).max().orElse(CursorRequest.NONE_KEY)).isEqualTo(15L);
            assertThat(albums.stream().mapToLong(AlbumResponse::id).min().orElse(CursorRequest.NONE_KEY)).isEqualTo(9L);
            assertThat(albumCursor.nextCursorRequest().key()).isEqualTo(9);
        }
    }
}
