package com.tinyhuman.tinyhumanapi.diary.mock;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeSentenceRepository implements SentenceRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0L);

    private final List<Sentence> data = new ArrayList<>();
    @Override
    public List<Sentence> saveAll(List<Sentence> sentences, Diary savedDiary) {

        for (Sentence sentence : sentences) {
            data.removeIf(s -> Objects.equals(s.id(), sentence.id()));
            Sentence newSentence = Sentence.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .sentence(sentence.sentence())
                    .diaryId(sentence.diaryId())
                    .build();
            data.add(newSentence);
        }
        return data;
    }

    @Override
    public Sentence save(Sentence sentence, Diary diary) {
        Sentence currentSentence = data.stream().filter(s -> Objects.equals(s.id(), sentence.id())).findAny().get();

        Sentence newSentence = Sentence.builder()
                .id(currentSentence.id())
                .sentence(sentence.sentence())
                .diaryId(currentSentence.diaryId())
                .build();

        data.removeIf(s -> Objects.equals(s.id(), sentence.id()));
        data.add(newSentence);

        return sentence;
    }

    @Override
    public Optional<Sentence> findById(Long id) {
        return  data.stream()
                .filter(d -> d.id().equals(id))
                .findAny();
    }
}
