package com.tinyhuman.tinyhumanapi.diary.service.port;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;

import java.util.List;

public interface SentenceRepository {

    List<Sentence> saveAll(List<Sentence> Sentences, Diary savedDiary);
}
