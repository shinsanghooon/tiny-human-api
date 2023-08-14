package com.tinyhuman.tinyhumanapi.diary.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;


public record DiaryCreate(

        @NotBlank
        int daysAfterBirth,

        @NotBlank
        String writer,
        @Min(0)
        int likeCount,

        @NotNull
        List<SentenceCreate> sentences) {

    @Builder
    public DiaryCreate {
    }
}
