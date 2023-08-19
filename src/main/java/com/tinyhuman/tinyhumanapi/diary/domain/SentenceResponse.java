package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;

public record SentenceResponse(Long id, String sentence, Long diaryId ) {

    @Builder
    public SentenceResponse {
    }

}
