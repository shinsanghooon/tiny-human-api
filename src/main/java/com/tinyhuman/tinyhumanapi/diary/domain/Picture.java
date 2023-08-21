package com.tinyhuman.tinyhumanapi.diary.domain;

import com.tinyhuman.tinyhumanapi.diary.enums.ContentType;
import lombok.Builder;

public record Picture(Long id, boolean isMainPicture, ContentType contentType, String originalS3Url, Long diaryId) {

    @Builder
    public Picture {
    }

    public Picture assignToMain() {
        return Picture.builder()
                .id(this.id)
                .isMainPicture(true)
                .contentType(this.contentType)
                .originalS3Url(this.originalS3Url)
                .diaryId(this.diaryId)
                .build();
    }

    public Picture assignToNormal() {
        return Picture.builder()
                .id(this.id)
                .isMainPicture(false)
                .contentType(this.contentType)
                .originalS3Url(this.originalS3Url)
                .diaryId(this.diaryId)
                .build();
    }


}
