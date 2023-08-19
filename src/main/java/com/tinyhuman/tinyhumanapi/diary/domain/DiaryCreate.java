package com.tinyhuman.tinyhumanapi.diary.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;


public record DiaryCreate(
        @NotNull
        Long babyId,

        @NotNull
        int daysAfterBirth,

        @NotNull
        Long userId,

        @Min(0)
        int likeCount,

        @NotNull
        @Size(max = 3, message = "글을 하루에 3개까지 작성이 가능합니다.")
        List<SentenceCreate> sentences) {

    @Builder
    public DiaryCreate {
    }
}
