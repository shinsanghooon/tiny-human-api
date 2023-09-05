package com.tinyhuman.tinyhumanapi.diary.controller.port.dto;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record DiaryResponse(Long id, int daysAfterBirth, String writer, int likeCount,
                            LocalDateTime createdAt, boolean isDeleted, List<Sentence> sentences, List<Picture> pictures) {

    @Builder
    public DiaryResponse {
    }

    public static DiaryResponse fromModel(Diary diary) {
        return DiaryResponse.builder()
                .id(diary.id())
                .daysAfterBirth(diary.daysAfterBirth())
                .writer(diary.user().name())
                .likeCount(diary.likeCount())
                .createdAt(diary.created_at())
                .sentences(diary.sentences())
                .pictures(diary.pictures())
                .isDeleted(diary.isDeleted())
                .build();
    }

}
