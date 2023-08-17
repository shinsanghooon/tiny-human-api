package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SentenceRepositoryImpl implements SentenceRepository {

    private final SentenceJpaRepository sentenceJpaRepository;

    @Override
    public List<Sentence> saveAll(List<Sentence> sentences) {
        List<SentenceEntity> sentenceEntities = sentenceJpaRepository.saveAll(
                sentences.stream()
                        .map(SentenceEntity::fromModel)
                        .toList());

        return sentenceEntities.stream()
                .map(s -> Sentence.builder()
                        .id(s.getId())
                        .sentence(s.getSentence())
                        .diary(s.getDiary().toModel())
                        .build()
                ).toList();
    }

}
