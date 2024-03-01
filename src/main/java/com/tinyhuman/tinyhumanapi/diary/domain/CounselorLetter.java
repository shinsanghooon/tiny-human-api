package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;

import java.time.LocalDateTime;

public record CounselorLetter(Long id, Long babyId, Long diaryId, String contents, LocalDateTime createdAt) {

    @Builder
    public CounselorLetter{}
}
