package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;
import java.util.List;

public record Sentence(Long id, String sentence) {

    @Builder
    public Sentence {
    }

    public static List<Sentence> from(DiaryCreate diaryCreate) {
        return diaryCreate.sentences()
                .stream()
                .map(s -> Sentence.builder()
                        .id(s.id)
                        .sentence(s.sentence)
                        .build())
                .toList();
    }
}
