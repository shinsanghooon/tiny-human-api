package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;
import java.util.List;

public record Sentence(Long id, String sentence, Diary diary) {

    @Builder
    public Sentence {
    }

    public static List<SentenceCreate> from(DiaryCreate diaryCreate) {
        return diaryCreate.sentences();
    }
}
