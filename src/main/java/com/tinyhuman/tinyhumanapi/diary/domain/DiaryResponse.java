package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;

import java.time.LocalDateTime;

public record DiaryResponse(Long id, int daysAfterBirth, String writer, Boolean isPublic, int likeCount,
                            LocalDateTime createdAt) {

    @Builder
    public DiaryResponse {
    }

    public static DiaryResponse fromModel(Diary diary) {
        return DiaryResponse.builder()
                .id(diary.id())
                .daysAfterBirth(diary.daysAfterBirth())
                .writer(diary.writer())
                .isPublic(diary.isPublic())
                .likeCount(diary.likeCount())
                .createdAt(diary.created_at())
                .build();
    }
}
