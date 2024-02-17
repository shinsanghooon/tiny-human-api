package com.tinyhuman.tinyhumanapi.album.service;

import com.tinyhuman.tinyhumanapi.album.controller.dto.*;
import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.album.mock.FakeAlbumRepository;
import com.tinyhuman.tinyhumanapi.auth.mock.FakeAuthService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeImageService;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.NotSupportedContentTypeException;
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
import java.time.LocalDateTime;
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

        fakeAlbumRepository.saveAll(List.of(
                Album.builder()
                        .babyId(2L)
                        .contentType(ContentType.PHOTO)
                        .id(101L)
                        .isDeleted(false)
                        .build(),
                Album.builder()
                        .babyId(2L)
                        .contentType(ContentType.PHOTO)
                        .id(102L)
                        .isDeleted(false)
                        .build(),
                Album.builder()
                        .babyId(2L)
                        .contentType(ContentType.PHOTO)
                        .id(103L)
                        .isDeleted(false)
                        .build()
        ));
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

        @DisplayName("사진, 동영상 타입만 존재하면 정상적으로 업로드가 된다.")
        @Test
        void onlyImageAndVideoAllowed() {
            List<String> newFiles = List.of("test.avi", "test.png", "test.jpg");
            List<AlbumCreate> albumCreates = newFiles.stream()
                    .map(f -> AlbumCreate.builder().fileName(f).build())
                    .toList();

            List<AlbumUploadResponse> albumUploadResponses = albumServiceImpl.uploadAlbums(1L, albumCreates);
            assertThat(albumUploadResponses.size()).isEqualTo(3);

            assertThat(albumUploadResponses)
                    .extracting(AlbumUploadResponse::filename)
                    .containsOnly("test-uuid_test.avi", "test-uuid_test.png", "test-uuid_test.jpg");

            assertThat(albumUploadResponses)
                    .extracting(AlbumUploadResponse::contentType)
                    .contains(ContentType.PHOTO, ContentType.VIDEO);
        }

        @DisplayName("사진, 동영상 타입이 아니라면 예외를 던진다.")
        @Test
        void onlyImageAndVideoAllowed2() {
            List<String> newFiles = List.of("test.avi", "test.pdf", "test.jpg");
            List<AlbumCreate> albumCreates = newFiles.stream()
                    .map(f -> AlbumCreate.builder().fileName(f).build())
                    .toList();

            assertThatThrownBy(() -> albumServiceImpl.uploadAlbums(1L, albumCreates))
                    .isInstanceOf(NotSupportedContentTypeException.class)
                    .hasMessageContaining("지원하지 않는");
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

            PageCursor<AlbumResponse> albumCursor = albumServiceImpl.getAlbumsByBaby(babyId, new CursorRequest(null, 5), "uploadedAt");
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
            PageCursor<AlbumResponse> albumCursor = albumServiceImpl.getAlbumsByBaby(babyId, new CursorRequest(16L, 7), "uploadedAt");
            List<AlbumResponse> albums = albumCursor.body();

            assertThat(albums.size()).isEqualTo(7);
            assertThat(albums).extracting("babyId").containsOnly(babyId);
            assertThat(albumCursor.nextCursorRequest().hasKey()).isTrue();
            assertThat(albums.stream().mapToLong(AlbumResponse::id).max().orElse(CursorRequest.NONE_KEY)).isEqualTo(15L);
            assertThat(albums.stream().mapToLong(AlbumResponse::id).min().orElse(CursorRequest.NONE_KEY)).isEqualTo(9L);
            assertThat(albumCursor.nextCursorRequest().key()).isEqualTo(9);
        }
    }

    @Nested
    @DisplayName("사용자는 사진 찍은 날짜 정보를 업데이트 한다.")
    class UpdatedAlbum {
        List<AlbumCreate> files = new ArrayList<>();
        Long babyId = 1L;

        @BeforeEach
        @DisplayName("임시 이미지 파일 10개를 미리 저장해둔다.")
        void fileSetup() {
            for (int i = 0; i < 20; i++) {
                String originalFileName = "original_" + i + ".png";
                files.add(AlbumCreate.builder().fileName(originalFileName).build());
            }
            albumServiceImpl.uploadAlbums(1L, files);
        }

        @Test
        @DisplayName("앨범 id 리스트와 업데이트 날짜를 입력 받아 데이터를 업데이트 한다.")
        void update() {
            List<Long> albums = List.of(1L, 2L, 3L);
            AlbumDateUpdate albumDateUpdate = AlbumDateUpdate.builder()
                    .ids(albums)
                    .originalCreatedAt(LocalDateTime.of(2023, 9, 12, 15, 29, 00))
                    .build();

            List<Album> updatedAlbums = albumServiceImpl.updateOriginalDate(babyId, albumDateUpdate);

            assertThat(updatedAlbums.size()).isEqualTo(3);
            assertThat(updatedAlbums).extracting(Album::id).contains(1L, 2L, 3L);
            assertThat(updatedAlbums).extracting(Album::originalCreatedAt).containsOnly(LocalDateTime.of(2023, 9, 12, 15, 29, 00));
        }

        @Test
        @DisplayName("존재하지 않는 앨범 id에 대해 요청을 하면 어떤 데이터도 업데이트 되지 않는다.")
        void updateInvalid() {
            List<Long> albums = List.of(109L, 3332L, 3333L);
            AlbumDateUpdate albumDateUpdate = AlbumDateUpdate.builder()
                    .ids(albums)
                    .originalCreatedAt(LocalDateTime.of(2023, 9, 12, 15, 29, 00))
                    .build();

            List<Album> updatedAlbums = albumServiceImpl.updateOriginalDate(babyId, albumDateUpdate);

            assertThat(updatedAlbums.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("부모가 아닌 사람이 업데이트를 하는 경우 예외를 던진다.")
        void updateWithFamilyWithNoAuthority() {
            Long babyIdWithNoAuthority = 2L;
            List<Long> albums = List.of(101L, 102L, 103L);

            AlbumDateUpdate albumDateUpdate = AlbumDateUpdate.builder()
                    .ids(albums)
                    .originalCreatedAt(LocalDateTime.of(2023, 9, 12, 15, 29, 00))
                    .build();

            assertThatThrownBy(() -> albumServiceImpl.updateOriginalDate(babyIdWithNoAuthority, albumDateUpdate))
                    .isInstanceOf(UnauthorizedAccessException.class)
                    .hasMessageContaining("접근 권한이 없습니다");
        }
    }
}
