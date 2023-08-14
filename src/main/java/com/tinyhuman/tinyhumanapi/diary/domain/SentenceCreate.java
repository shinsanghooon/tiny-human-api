package com.tinyhuman.tinyhumanapi.diary.domain;

import jakarta.validation.constraints.Size;
import lombok.Builder;

public record SentenceCreate(@Size(max = 300) String sentence) {

    @Builder
    public SentenceCreate {
    }
}
