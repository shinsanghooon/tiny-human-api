package com.tinyhuman.tinyhumanapi.diary.domain;

import com.tinyhuman.tinyhumanapi.diary.enums.ContentType;
import lombok.Builder;

public record Picture(Long id, boolean isMainPicture, ContentType contentType, String originalS3Url, Long diaryId) {

    @Builder
    public Picture {
    }
}
