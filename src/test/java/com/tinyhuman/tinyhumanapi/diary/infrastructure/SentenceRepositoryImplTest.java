package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;
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
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
@Sql("classpath:sql/user-repository-test-data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SentenceRepositoryImplTest {

    @Autowired
    private SentenceRepositoryImpl sentenceRepository;

    @Test
    @DisplayName("일기에 대한 글을 복수 개 등록한다.")
    void registerSentences() {

        User user = User.builder().id(1L).build();
        Baby baby = Baby.builder().id(1L).build();
        Diary diary = Diary.builder()
                .id(2L)
                .user(user)
                .baby(baby)
                .build();

        List<Sentence> sentences = IntStream.range(0, 2)
                .mapToObj(i -> Sentence.builder()
                        .diaryId(2L)
                        .sentence("안녕하세요[" + i + "]")
                        .build())
                .toList();

        List<Sentence> savedSentences = sentenceRepository.saveAll(sentences, diary);

        assertThat(savedSentences.size()).isEqualTo(2);
        assertThat(savedSentences).extracting(Sentence::diaryId).containsOnly(2L);
        assertThat(savedSentences).allSatisfy(s -> {
            assertThat(s.isDeleted()).isFalse();
            assertThat(s.sentence()).contains("안녕하세요");
        });
    }

    @Test
    @DisplayName("일기에 대한 글을 1건 등록한다.")
    void registerOneSentence() {

        User user = User.builder().id(1L).build();
        Baby baby = Baby.builder().id(1L).build();
        Diary diary = Diary.builder()
                .id(2L)
                .user(user)
                .baby(baby)
                .build();

        Sentence sentence = Sentence.builder()
                .diaryId(2L)
                .sentence("맛있게 드세요.")
                .build();

        Sentence savedSentences = sentenceRepository.save(sentence, diary);

        assertThat(savedSentences).isNotNull();
        assertThat(savedSentences.id()).isNotNull();
        assertThat(savedSentences.sentence()).isEqualTo("맛있게 드세요.");
    }

    @Test
    @DisplayName("id를 이용하여 글에 대한 조회를 한다.")
    void findById() {
        Optional<Sentence> sentence = sentenceRepository.findById(1L);

        assertThat(sentence.isPresent()).isTrue();
        assertThat(sentence.get().id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("존재하지 않는 id를 이용하면 Optional.isEmpty()가 반환된다.")
    void findByIdWithNoData() {
        Optional<Sentence> sentence = sentenceRepository.findById(9999L);

        assertThat(sentence.isEmpty()).isTrue();
    }
}