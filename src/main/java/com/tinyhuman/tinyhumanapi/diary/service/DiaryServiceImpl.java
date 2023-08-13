package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.domain.*;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Builder
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    private final SentenceRepository sentenceRepository;

    // 베이비 정보도 미리 있으면 좋을 것 같음
    // TODO 사실 diary, sentences, pictures 데이터가 한 번에 와야함
    @Transactional
    public DiaryResponse create(DiaryCreate diaryCreate) {
        Diary newDiary = Diary.from(diaryCreate);
        Diary savedDiary = diaryRepository.save(newDiary);

        List<SentenceCreate> sentences = Sentence.from(diaryCreate);
        List<Sentence> sentenceModels = sentences.stream()
                .map(s -> Sentence.builder()
                        .sentence(s.sentence())
                        .diary(savedDiary)
                        .build())
                .toList();

        List<Sentence> savedSentences = sentenceRepository.saveAll(sentenceModels);

        return DiaryResponse.fromModel(savedDiary, savedSentences);
    }
}
