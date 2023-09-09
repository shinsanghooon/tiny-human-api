package com.tinyhuman.tinyhumanapi.album.controller;

import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumCreate;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumUploadResponse;
import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.mock.TestContainer;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
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

            testContainer.userRepository.save(User.builder()
                    .name("유닛2")
                    .email("unit2@unit.com")
                    .password("unit2")
                    .build()
            );

            BabyCreate babyCreate = BabyCreate.builder()
                    .name("아기")
                    .gender(Gender.MALE)
                    .nickName("아기별명")
                    .relation(FamilyRelation.FATHER)
                    .timeOfBirth(23)
                    .dayOfBirth(LocalDate.of(2022, 9, 27))
                    .fileName("profile.jpg")
                    .build();

            testContainer.babyController.register(babyCreate);
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
        @DisplayName("사용자가 부모가 아니라면 업로드가 실패하면 40을 응답한다.")
        void unAuthorizedRelationToBaby() {


        }
    }

    @Nested
    @DisplayName("사용자는 앨범을 조회 할 수 있다.")
    class getAlbum {

        @Test
        @DisplayName("앨범에 대한 상세 정보를 조회할 수 있다.")
        void getAlbumDetail() {

        }

        @Test
        @DisplayName("전체를 조회할 수 있다.")
        void getAllAlbum() {

        }


        @Test
        @DisplayName("존재하지 않은 id에 조회 요청하는 경우 400 응답을 한다.")
        void getAlbumWithInvalidId() {

        }

    }

    @Nested
    @DisplayName("사용자는 앨범을 삭제할 수 있다.")
    class deleteAlbum {

        @Test
        @DisplayName("존재하지 않은 id에 삭제 요청하는 경우 400 응답을 한다.")
        void getAlbumWithInvalidId() {

        }
    }
}