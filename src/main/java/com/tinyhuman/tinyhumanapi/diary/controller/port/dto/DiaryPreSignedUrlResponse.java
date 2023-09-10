package com.tinyhuman.tinyhumanapi.diary.controller.port.dto;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public record DiaryPreSignedUrlResponse(Long id, int daysAfterBirth, String writer, int likeCount, LocalDate date,
                                        List<Sentence> sentences, List<Picture> pictures) {

    @Builder
    public DiaryPreSignedUrlResponse {
    }

    public static DiaryPreSignedUrlResponse fromModel(Diary diary) {
        return DiaryPreSignedUrlResponse.builder()
                .id(diary.id())
                .daysAfterBirth(diary.daysAfterBirth())
                .writer(diary.user().name())
                .likeCount(diary.likeCount())
                .date(diary.date())
                .sentences(diary.sentences())
                .pictures(diary.pictures())
                .build();
    }

}
