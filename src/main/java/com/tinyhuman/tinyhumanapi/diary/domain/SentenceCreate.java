package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;

public record SentenceCreate(String sentence) {

    @Builder
    public SentenceCreate {
    }
}
