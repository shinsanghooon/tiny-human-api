package com.tinyhuman.tinyhumanapi.diary.service.port;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;

import java.util.List;
import java.util.Optional;

public interface SentenceRepository {

    List<Sentence> saveAll(List<Sentence> Sentences, Diary savedDiary);

    Sentence save(Sentence sentence);

    Optional<Sentence> findById(Long id);

}
