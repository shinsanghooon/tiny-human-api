package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
@Sql("classpath:sql/user-repository-test-data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PictureRepositoryImplTest {

    @Autowired
    private PictureRepositoryImpl pictureRepository;

    @Test
    @DisplayName("일기의 사진 및 영상을 저장합니다.")
    void registerPictures() {
        User user = User.builder().id(1L).build();
        Baby baby = Baby.builder().id(1L).build();
        Diary diary = Diary.builder()
                .id(2L)
                .user(user)
                .baby(baby)
                .build();

        List<Picture> pictures = List.of(
                Picture.builder()
                        .diaryId(2L)
                        .fileName("1.jpg")
                        .keyName("baby/diary/2/1.jpg")
                        .isMainPicture(true)
                        .build(),
                Picture.builder()
                        .diaryId(2L)
                        .fileName("2.jpg")
                        .keyName("baby/diary/2/1.jpg")
                        .isMainPicture(false)
                        .build()
        );

        List<Picture> savedPictures = pictureRepository.saveAll(pictures, diary);

        assertThat(savedPictures.size()).isEqualTo(2);
        assertThat(savedPictures).extracting(Picture::diaryId).containsOnly(2L);

        long countOfTrueMainPictures = pictures.stream()
                .filter(Picture::isMainPicture)
                .count();

        assertThat(countOfTrueMainPictures)
                .as("한 장의 사진만 Main Picture 여야 함")
                .isEqualTo(1);
    }

    @Test
    @DisplayName("id를 이용하여 사진 조회를 한다.")
    void findById() {
        Optional<Picture> picture = pictureRepository.findById(1L);

        assertThat(picture.isPresent()).isTrue();
        assertThat(picture.get().id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("존재하지 않는 id를 이용하면 Optional.isEmpty()가 반환된다.")
    void findByIdWithNoData() {
        Optional<Picture> picture = pictureRepository.findById(9999L);

        assertThat(picture.isEmpty()).isTrue();
    }
}