package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
@Sql("classpath:sql/user-repository-test-data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DiaryRepositoryImplTest {

    @Autowired
    private DiaryRepositoryImpl diaryRepository;

    @Test
    @DisplayName("사용자는 일기를 등록할 수 있다.")
    void registerDiary() {
        Diary diary = Diary.builder()
                .user(User.builder().id(1L).build())
                .baby(Baby.builder().id(1L).build())
                .daysAfterBirth(10)
                .likeCount(0)
                .isDeleted(false)
                .build();

        Diary savedDiary = diaryRepository.save(diary);

        assertThat(savedDiary).isNotNull();
        assertThat(savedDiary.id()).isEqualTo(2L);
        assertThat(savedDiary.user().id()).isEqualTo(1L);
        assertThat(savedDiary.baby().id()).isEqualTo(1L);

    }

    @Test
    @DisplayName("사용자는 id를 이용해서 상세 조회를 할 수 있다.")
    void findById() {
        Diary savedDiary = diaryRepository.findById(1L).get();

        assertThat(savedDiary.id()).isEqualTo(1L);
        assertThat(savedDiary.baby().id()).isEqualTo(1L);
        assertThat(savedDiary.user().id()).isEqualTo(1L);
        assertThat(savedDiary.sentences().size()).isEqualTo(3);
        assertThat(savedDiary.pictures().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("날짜를 이용해서 상세 조회할 수 있다.")
    void findByDate() {
        List<Diary> diaries = diaryRepository.findByDate(LocalDate.of(2023, 9, 11), 1L, 1L);

        assertThat(diaries.size()).isGreaterThan(0);
        assertThat(diaries)
                .extracting(Diary::baby)
                .extracting(Baby::id)
                .containsOnly(1L);
        assertThat(diaries.get(0).baby().id()).isEqualTo(1L);
        assertThat(diaries.get(0).user().id()).isEqualTo(1L);
        assertThat(diaries.get(0).sentences().size()).isEqualTo(3);
        assertThat(diaries.get(0).pictures().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("해당 날짜에 일기가 없으면 빈 리스트를 반환한다.")
    void findByDateWithNoData() {
        List<Diary> diary = diaryRepository.findByDate(LocalDate.of(2023, 7, 11), 1L, 1L);

        assertThat(diary.size()).isZero();
    }

    @Test
    @DisplayName("아기의 전체 일기를 조회할 수 있다.")
    void findAllByBabyId() {
        List<Diary> diaries = diaryRepository.findByBabyId(1L);

        assertThat(diaries.size()).isGreaterThan(0);
        assertThat(diaries)
                .extracting(Diary::baby)
                .extracting(Baby::id)
                .containsOnly(1L);
    }

    @Test
    @DisplayName("아기의 일기가 없으면 빈 리스트를 반환한다.")
    void findAllByBabyIdWithNoData() {
        List<Diary> diaries = diaryRepository.findByBabyId(99L);

        assertThat(diaries.size()).isZero();
    }

    @Test
    @DisplayName("아기의 일기를 커서 기반 페이징을 적용하여 조회한다.")
    void findAllByBabyWithCursor() {
        // 현재 일기는 1개만 등록된 상태
        List<Diary> diaries = diaryRepository.findByBabyId(1L, new CursorRequest(null, 5));

        assertThat(diaries.size()).isOne();
        assertThat(diaries)
                .extracting(Diary::baby)
                .extracting(Baby::id)
                .containsOnly(1L);
    }
}