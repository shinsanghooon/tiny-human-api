package com.tinyhuman.tinyhumanapi.diary.controller.port.dto;

import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;
import lombok.Builder;

public record SentenceResponse(Long id, String sentence) {

    @Builder
    public SentenceResponse {
    }

    public static SentenceResponse fromModel(Sentence sentence) {
        return SentenceResponse.builder()
                .id(sentence.id())
                .sentence(sentence.sentence())
                .build();
    }
}
