package com.tinyhuman.tinyhumanapi.diary.controller.port.dto;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record DiaryResponse(Long id, int daysAfterBirth, String writer, int likeCount,
                            LocalDateTime createdAt, boolean isDeleted, List<SentenceResponse> sentences,
                            List<PictureResponse> pictures) {

    @Builder
    public DiaryResponse {
    }

    public static DiaryResponse fromModel(Diary diary) {

        List<SentenceResponse> sentenceResponses = diary.sentences() == null
                ? null
                : diary.sentences().stream()
                    .map(SentenceResponse::fromModel)
                    .toList();

        List<PictureResponse> pictureResponses = diary.pictures() == null
                ? null
                : diary.pictures().stream()
                    .map(PictureResponse::fromModel)
                    .toList();

        return DiaryResponse.builder()
                .id(diary.id())
                .daysAfterBirth(diary.daysAfterBirth())
                .writer(diary.user().name())
                .likeCount(diary.likeCount())
                .createdAt(diary.created_at())
                .sentences(sentenceResponses)
                .pictures(pictureResponses)
                .isDeleted(diary.isDeleted())
                .build();
    }

}
