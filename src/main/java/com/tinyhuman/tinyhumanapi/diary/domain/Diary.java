package com.tinyhuman.tinyhumanapi.diary.domain;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import lombok.Builder;

import java.time.LocalDateTime;

public record Diary(Long id, int daysAfterBirth, String writer, int likeCount,
                    LocalDateTime created_at, Baby baby, boolean isDeleted) {

    @Builder
    public Diary {
    }

    public static Diary from(DiaryCreate diaryCreate, Baby baby) {
        return Diary.builder()
                .daysAfterBirth(diaryCreate.daysAfterBirth())
                .writer(diaryCreate.writer())
                .likeCount(diaryCreate.likeCount())
                .baby(baby)
                .build();
    }
}
