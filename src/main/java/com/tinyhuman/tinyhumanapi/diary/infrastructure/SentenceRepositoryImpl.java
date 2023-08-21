package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SentenceRepositoryImpl implements SentenceRepository {

    private final SentenceJpaRepository sentenceJpaRepository;

    @Override
    public List<Sentence> saveAll(List<Sentence> sentences, Diary savedDiary) {

        List<SentenceEntity> sentenceEntities = sentenceJpaRepository.saveAll(
                sentences.stream()
                        .map(s -> SentenceEntity.fromModel(s, savedDiary))
                        .toList());

        return sentenceEntities.stream()
                .map(s -> Sentence.builder()
                        .id(s.getId())
                        .sentence(s.getSentence())
                        .diaryId(s.getDiary().getId())
                        .build()
                ).toList();
    }

    @Override
    public Sentence save(Sentence sentence) {
        return null;
    }

    @Override
    public Optional<Sentence> findById(Long id) {
        return Optional.empty();
    }

}
