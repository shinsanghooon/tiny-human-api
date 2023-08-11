package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Diary {

    private Long id;

    private int daysAfterBirth;

    private String writer;

    private Boolean isPublic;

    private int likeCount;

    private LocalDateTime created_at;

    @Builder
    public Diary(Long id, int daysAfterBirth, String writer, Boolean isPublic, int likeCount, LocalDateTime created_at) {
        this.id = id;
        this.daysAfterBirth = daysAfterBirth;
        this.writer = writer;
        this.isPublic = isPublic;
        this.likeCount = likeCount;
        this.created_at = created_at;
    }

    public static Diary from(DiaryCreate diaryCreate) {
        return Diary.builder()
                .daysAfterBirth(0)
                .writer("d")
                .isPublic(true)
                .likeCount(1)
                .build();
    }

}
