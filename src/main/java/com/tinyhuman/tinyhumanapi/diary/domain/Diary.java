package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;

import java.time.LocalDateTime;

public record Diary(Long id, int daysAfterBirth, String writer, int likeCount,
                    LocalDateTime created_at) {

    @Builder
    public Diary {
    }

    public static Diary from(DiaryCreate diaryCreate) {
        return Diary.builder()
                .daysAfterBirth(diaryCreate.daysAfterBirth())
                .writer(diaryCreate.writer())
                .likeCount(diaryCreate.likeCount())
                .build();
    }
}
