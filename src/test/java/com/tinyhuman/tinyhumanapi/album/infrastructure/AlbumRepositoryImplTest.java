package com.tinyhuman.tinyhumanapi.album.infrastructure;

import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional(readOnly = true)
@Sql("classpath:sql/user-repository-test-data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AlbumRepositoryImplTest {

    @Autowired
    AlbumRepositoryImpl albumRepository;

    @Test
    @DisplayName("앨범을 저장한다.")
    void saveAlbums() {

        List<Album> albums = IntStream.range(1, 15)
                .mapToObj(i ->
                        Album.builder()
                                .babyId(1L)
                                .contentType(ContentType.PHOTO)
                                .keyName("keyName" + i*10)
                                .isDeleted(false)
                                .build()).toList();

        List<Album> savedAlbums = albumRepository.saveAll(albums);


        assertThat(savedAlbums).isNotNull();
        assertThat(savedAlbums.size()).isEqualTo(albums.size());
        assertThat(savedAlbums).extracting("babyId").containsOnly(1L);
        assertThat(savedAlbums).extracting("id").isNotNull();
        assertThat(savedAlbums).extracting("keyName").isNotNull();

    }

    @Test
    @DisplayName("앨범 id와 아기 id로 앨범을 조회한다.")
    void findByIdAndBabyId() {
        Album album = albumRepository.findByIdAndBabyId(3L, 1L);

        assertThat(album.id()).isEqualTo(3L);
        assertThat(album.babyId()).isEqualTo(1L);
        assertThat(album.keyName()).isEqualTo("keyName3");
        assertThat(album.isDeleted()).isEqualTo(false);
    }

    @Test
    @DisplayName("앨범 id와 아기 id로 조회 시 데이터가 존재하지 않으면 예외를 던진다.")
    void findByIdAndBabyIdWhenException() {
        assertThatThrownBy(() -> albumRepository.findByIdAndBabyId(999L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Album");
    }

    @Test
    @DisplayName("여러 앨범을 조회한다.")
    void findAllByIds() {
        List<Long> ids = LongStream.range(5, 10).boxed().toList();
        List<Album> albums = albumRepository.findAllByIds(ids);

        assertThat(albums.size()).isEqualTo(ids.size());
        assertThat(albums).extracting("id").contains(5L, 6L, 7L, 8L, 9L);
    }

    @Test
    @DisplayName("존재하지 않은 앨범 리스트를 조회하면 빈 리스트를 반환한다.")
    void findAllByIdsNotExisted() {
        List<Long> ids = LongStream.range(990, 1000).boxed().toList();
        List<Album> albums = albumRepository.findAllByIds(ids);

        assertThat(albums).isEmpty();
    }

    @Test
    @DisplayName("아기 id로 앨범을 조회한다.(커서 쿼리 최초 조회)")
    void findByBabyIdFirstTime() {
        List<Album> albums = albumRepository.findByBabyId(1L);

        assertThat(albums.size()).isEqualTo(10);
        assertThat(albums).extracting("id").containsExactly(30L, 29L, 28L, 27L, 26L, 25L, 24L, 23L, 22L, 21L);
    }

    @Test
    @DisplayName("아기 id로 앨범을 조회한다.")
    void findByBabyIdSecondTime() {
        List<Album> albums = albumRepository.findByBabyId(1L, new CursorRequest(21L, 5), "uploadedAt");

        assertThat(albums.size()).isEqualTo(5);
        assertThat(albums).extracting("id").containsExactly(20L, 19L, 18L, 17L, 16L);
    }

}