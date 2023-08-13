package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record DiaryResponse(Long id, int daysAfterBirth, String writer, Boolean isPublic, int likeCount,
                            LocalDateTime createdAt, List<Sentence> sentences) {

    @Builder
    public DiaryResponse {
    }

    public static DiaryResponse fromModel(Diary diary, List<Sentence> sentences) {
        return DiaryResponse.builder()
                .id(diary.id())
                .daysAfterBirth(diary.daysAfterBirth())
                .writer(diary.writer())
                .isPublic(diary.isPublic())
                .likeCount(diary.likeCount())
                .createdAt(diary.created_at())
                .sentences(sentences)
                .build();
    }
}
