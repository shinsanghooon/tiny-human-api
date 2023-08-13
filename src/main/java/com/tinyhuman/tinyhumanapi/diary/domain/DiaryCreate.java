package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;
import java.util.List;


public record DiaryCreate(int daysAfterBirth, String writer, Boolean isPublic, int likeCount,
                          List<SentenceCreate> sentences) {

    @Builder
    public DiaryCreate {
    }
}
