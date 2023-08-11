package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;

import java.time.LocalDateTime;

public class DiaryResponse {

    private Long id;

    private int daysAfterBirth;

    private String writer;

    private Boolean isPublic;

    private int likeCount;

    private LocalDateTime createdAt;

    @Builder
    public DiaryResponse(Long id, int daysAfterBirth, String writer, Boolean isPublic, int likeCount, LocalDateTime createdAt) {
        this.id = id;
        this.daysAfterBirth = daysAfterBirth;
        this.writer = writer;
        this.isPublic = isPublic;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }

    public static DiaryResponse fromModel(Diary diary) {
        return DiaryResponse.builder()
                .id(diary.getId())
                .daysAfterBirth(diary.getDaysAfterBirth())
                .writer(diary.getWriter())
                .isPublic(diary.getIsPublic())
                .likeCount(diary.getLikeCount())
                .createdAt(diary.getCreated_at())
                .build();
    }
}
