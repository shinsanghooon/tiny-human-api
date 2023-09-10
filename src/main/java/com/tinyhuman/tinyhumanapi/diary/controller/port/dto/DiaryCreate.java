package com.tinyhuman.tinyhumanapi.diary.controller.port.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
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
        LocalDate date,

        @NotNull
        @Size(max = 3, message = "글을 하루에 3개까지 작성이 가능합니다.")
        List<SentenceCreate> sentences,

        @Size(max = 5, message="사진 및 동영상은 최대 업로드 개수는 5개입니다.")
        List<PictureCreate> files
        ) {

    @Builder
    public DiaryCreate {
    }
}
