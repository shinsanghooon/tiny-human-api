package com.tinyhuman.tinyhumanapi.diary.controller.port.dto;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public record DiaryResponse(Long id, int daysAfterBirth, String writer, int likeCount,
                            LocalDate date, boolean isDeleted, List<SentenceResponse> sentences,
                            List<PictureResponse> pictures, String letter) {

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
                .date(diary.date())
                .sentences(sentenceResponses)
                .pictures(pictureResponses)
                .isDeleted(diary.isDeleted())
                .build();
    }

    public DiaryResponse addLetter(String letter) {
        return DiaryResponse.builder()
                .id(this.id())
                .daysAfterBirth(this.daysAfterBirth())
                .writer(this.writer())
                .likeCount(this.likeCount())
                .date(this.date())
                .sentences(this.sentences())
                .pictures(this.pictures())
                .letter(letter)
                .isDeleted(this.isDeleted())
                .build();
    }

}
