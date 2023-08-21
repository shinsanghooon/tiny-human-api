package com.tinyhuman.tinyhumanapi.diary.domain;

import jakarta.validation.constraints.Size;
import lombok.Builder;

public record SentenceCreate(@Size(max = 900, message="하나의 글을 300자까지 작성이 가능합니다.") String sentence) {

    @Builder
    public SentenceCreate {
    }
}
