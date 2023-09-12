package com.tinyhuman.tinyhumanapi.album.controller;

import com.tinyhuman.tinyhumanapi.album.controller.dto.*;
import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyPreSignedUrlResponse;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.common.mock.TestContainer;
import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import com.tinyhuman.tinyhumanapi.common.utils.PageCursor;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.UserBabyRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class AlbumControllerTest {

    @Nested
    @DisplayName("사용자는 앨범을 추가할 수 있다.")
    class uploadAlbums {

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
        @DisplayName("사진 또는 영상을 추가할 수 있다.")
        void uploadAlbums() {
            Long babyId = 1L;
            List<AlbumCreate> files = IntStream.range(1, 5)
                    .mapToObj(i -> AlbumCreate.builder()
                            .fileName(i + ".jpg")
                            .originalCreatedAt(LocalDateTime.of(2023, 8, i, i, i, i))
                            .gpsLat(37.413294)
                            .gptLon(127.0016985)
                            .build())
                    .toList();

            ResponseEntity<List<AlbumUploadResponse>> result = testContainer.albumController.uploadAlbums(babyId, files);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
            assertThat(Objects.requireNonNull(result.getBody()).size()).isEqualTo(files.size());
            assertThat(result.getBody())
                    .extracting("babyId", "contentType")
                    .containsOnly(tuple(1L, ContentType.PHOTO));
            assertThat(result.getBody())
                    .extracting("filename")
                    .containsExactly("test-uuid-code_1.jpg", "test-uuid-code_2.jpg", "test-uuid-code_3.jpg", "test-uuid-code_4.jpg");
            assertThat(result.getBody())
                    .extracting("originalCreatedAt")
                    .containsExactlyElementsOf(
                            IntStream.range(1, 5)
                                    .mapToObj(i -> LocalDateTime.of(2023, 8, i, i, i, i))
                                    .toList()
                    );
        }

        @Test
        @DisplayName("업로드 사진 또는 영상의 수가 20개를 초과하면 예외를 던진다.")
        void uploadAlbumsOver20() {
            Long babyId = 1L;
            List<AlbumCreate> files = IntStream.range(1, 25)
                    .mapToObj(i -> AlbumCreate.builder()
                            .fileName(i + ".jpg")
                            .build())
                    .toList();

            assertThatThrownBy(() -> testContainer.albumController.uploadAlbums(babyId, files))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("사진 및 동영상은 최대 업로드 개수는 20개입니다.");
        }

        @Test
        @DisplayName("아기에 대한 권한이 없다면 업로드가 실패하면 400을 응답한다.")
        void unAuthorizedToBaby() {
            User user2 = testContainer.userRepository.save(User.builder()
                    .name("유닛2")
                    .email("unit2@unit.com")
                    .password("unit2")
                    .build()
            );

            Baby baby2 = testContainer.babyRepository.save(Baby.builder()
                    .name("아기2")
                    .gender(Gender.MALE)
                    .nickName("아기별명2")
                    .timeOfBirth(23)
                    .dayOfBirth(LocalDate.of(2022, 9, 27))
                    .build());

            testContainer.userBabyRelationRepository.save(
                    UserBabyRelation.builder()
                            .user(user2)
                            .baby(baby2)
                            .relation(FamilyRelation.FATHER)
                            .userBabyRole(UserBabyRole.ADMIN)
                            .isDeleted(false)
                            .build());

            List<AlbumCreate> files = IntStream.range(1, 3)
                    .mapToObj(i -> AlbumCreate.builder()
                            .fileName(i + ".jpg")
                            .build())
                    .toList();

            assertThatThrownBy(() -> testContainer.albumController.uploadAlbums(baby2.id(), files))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("UserBabyRelation");
        }

        @Test
        @DisplayName("부모가 아니라 친척이라면 앨범 업로드는 할 수 없으며 예외를 던진다.")
        void unAuthorizedRelationToBaby() {
            User user = testContainer.authService.getUserOutOfSecurityContextHolder();
            Baby baby2 = testContainer.babyRepository.save(Baby.builder()
                    .name("아기2")
                    .gender(Gender.MALE)
                    .nickName("아기별명2")
                    .timeOfBirth(23)
                    .dayOfBirth(LocalDate.of(2022, 9, 27))
                    .build());

            testContainer.userBabyRelationRepository.save(
                    UserBabyRelation.builder()
                            .user(user) // id: 1
                            .baby(baby2) // id: 2
                            .relation(FamilyRelation.UNCLE)
                            .userBabyRole(UserBabyRole.ADMIN)
                            .isDeleted(false)
                            .build());

            List<AlbumCreate> files = IntStream.range(1, 3)
                    .mapToObj(i -> AlbumCreate.builder()
                            .fileName(i + ".jpg")
                            .build())
                    .toList();

            assertThatThrownBy(() -> testContainer.albumController.uploadAlbums(baby2.id(), files))
                    .isInstanceOf(UnauthorizedAccessException.class)
                    .hasMessageContaining("Album");
        }
    }

    @Nested
    @DisplayName("사용자는 앨범을 조회 할 수 있다.")
    class getAlbum {

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

            BabyPreSignedUrlResponse baby = testContainer.babyService.register(BabyCreate.builder()
                    .name("아기")
                    .gender(Gender.MALE)
                    .nickName("아기별명")
                    .relation(FamilyRelation.FATHER)
                    .timeOfBirth(23)
                    .dayOfBirth(LocalDate.of(2022, 9, 27))
                    .fileName("profile.jpg")
                    .build());

            List<AlbumCreate> files = IntStream.range(1, 20)
                    .mapToObj(i -> AlbumCreate.builder()
                            .fileName(i + ".jpg")
                            .originalCreatedAt(LocalDateTime.of(2023, 8, i, i, i, i))
                            .gpsLat(37.413294)
                            .gptLon(127.0016985)
                            .build())
                    .toList();

            testContainer.albumController.uploadAlbums(baby.id(), files);
        }

        @Test
        @DisplayName("앨범에 대한 상세 정보를 조회할 수 있다.")
        void getAlbumDetail() {
            ResponseEntity<AlbumResponse> album = testContainer.albumController.getAlbum(1L, 1L);

            assertThat(album.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(album.getBody().id()).isEqualTo(1L);
            assertThat(album.getBody().babyId()).isEqualTo(1L);
            assertThat(album.getBody().contentType()).isEqualTo(ContentType.PHOTO);
            assertThat(album.getBody().originalCreatedAt()).isEqualTo(LocalDateTime.of(2023, 8, 1, 1, 1, 1));
            assertThat(album.getBody().keyName()).contains("test-uuid-code_1.jpg");
        }

        @Test
        @DisplayName("커서 기반 페이징으로 처음 N개의 데이터를 조회할 수 있다.")
        void getAllAlbumWithFirstCursor() {

            CursorRequest cursorRequest = new CursorRequest(null, 5);

            ResponseEntity<PageCursor<AlbumResponse>> result = testContainer.albumController.getAllAlbums(1L, "created_at", cursorRequest);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(result.getBody().body().size()).isEqualTo(cursorRequest.size());
            assertThat(result.getBody().body())
                    .extracting("id")
                    .containsOnly(19L, 18L, 17L, 16L, 15L);
            assertThat(result.getBody().nextCursorRequest().key()).isEqualTo(15);
            assertThat(result.getBody().nextCursorRequest().hasKey()).isEqualTo(true);
        }

        @Test
        @DisplayName("커서 기반 페이징으로 마지막 데이터를 조회할 수 있다.")
        void getAllAlbumWithLastCursor() {

            CursorRequest cursorRequest = new CursorRequest(5L, 5);

            ResponseEntity<PageCursor<AlbumResponse>> result = testContainer.albumController.getAllAlbums(1L, "created_at", cursorRequest);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(result.getBody().body().size()).isEqualTo(4); // id: 1L, 2L, 3L, 4L
            assertThat(result.getBody().body())
                    .extracting("id")
                    .containsOnly(1L, 2L, 3L, 4L);
            assertThat(result.getBody().nextCursorRequest().key()).isEqualTo(1);
        }

        @Test
        @DisplayName("존재하지 않은 id에 조회 요청하는 경우 예외를 던진다.")
        void getAlbumWithInvalidId() {
            assertThatThrownBy(() -> testContainer.albumController.getAlbum(1L, 999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Album");
        }
    }

    @Nested
    @DisplayName("사용자는 앨범을 삭제할 수 있다.")
    class deleteAlbum {

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

            List<AlbumCreate> files = IntStream.range(1, 5)
                    .mapToObj(i -> AlbumCreate.builder()
                            .fileName(i + ".jpg")
                            .build())
                    .toList();

            testContainer.albumController.uploadAlbums(1L, files);

        }

        @Test
        @DisplayName("앨범을 삭제한다.")
        void deleteAlbums() {

            AlbumDelete albumDelete = AlbumDelete.builder()
                    .ids(List.of(1L, 2L))
                    .build();

            testContainer.albumController.deleteAlbums(1L, albumDelete);

            assertThatThrownBy(() -> testContainer.albumController.getAlbum(1L, 1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                            .hasMessageContaining("Album");
        }
    }

    @Nested
    @DisplayName("사용자는 앨범을 업데이트 할 수 있다.")
    class updateAlbum {

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

            BabyPreSignedUrlResponse baby = testContainer.babyService.register(BabyCreate.builder()
                    .name("아기")
                    .gender(Gender.MALE)
                    .nickName("아기별명")
                    .relation(FamilyRelation.FATHER)
                    .timeOfBirth(23)
                    .dayOfBirth(LocalDate.of(2022, 9, 27))
                    .fileName("profile.jpg")
                    .build());

            List<AlbumCreate> files = IntStream.range(1, 20)
                    .mapToObj(i -> AlbumCreate.builder()
                            .fileName(i + ".jpg")
                            .gpsLat(37.413294)
                            .gptLon(127.0016985)
                            .build())
                    .toList();

            testContainer.albumController.uploadAlbums(baby.id(), files);
        }

        @Test
        @DisplayName("앨범의 exif 정보를 업데이트 할 수 있다.")
        void updateAlbumCreatedAt() {

            List<Long> albums = List.of(1L, 2L, 3L);
            AlbumDateUpdate albumDateUpdate = AlbumDateUpdate.builder()
                    .ids(albums)
                    .originalCreatedAt(LocalDateTime.of(2023, 9, 12, 15, 29, 00))
                    .build();

            ResponseEntity<Void> result = testContainer.albumController.updateOriginalCreatedAt(1L, albumDateUpdate);

            assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        }
    }
}