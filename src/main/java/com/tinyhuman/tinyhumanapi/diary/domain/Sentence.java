package com.tinyhuman.tinyhumanapi.diary.domain;

import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.SentenceCreate;
import lombok.Builder;
import java.util.List;

public record Sentence(Long id, String sentence, Long diaryId, boolean isDeleted) {

    @Builder
    public Sentence {
    }

    public static List<SentenceCreate> from(DiaryCreate diaryCreate) {
        return diaryCreate.sentences();
    }

    public Sentence update(SentenceCreate newSentence) {
        return Sentence.builder()
                .id(this.id)
                .sentence(newSentence.sentence())
                .diaryId(this.diaryId)
                .build();
    }

    public Sentence delete() {
        return Sentence.builder()
                .id(this.id)
                .sentence(this.sentence)
                .diaryId(this.diaryId)
                .isDeleted(true)
                .build();
    }
}
