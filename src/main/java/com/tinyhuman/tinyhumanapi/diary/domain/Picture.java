package com.tinyhuman.tinyhumanapi.diary.domain;

import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import lombok.Builder;

public record Picture(Long id, boolean isMainPicture, ContentType contentType, String keyName, String preSignedUrl, Long diaryId) {

    @Builder
    public Picture {
    }

    public Picture assignToMain() {
        return Picture.builder()
                .id(this.id)
                .isMainPicture(true)
                .contentType(this.contentType)
                .keyName(this.keyName)
                .preSignedUrl(this.preSignedUrl)
                .diaryId(this.diaryId)
                .build();
    }

    public Picture assignToNormal() {
        return Picture.builder()
                .id(this.id)
                .isMainPicture(false)
                .contentType(this.contentType)
                .keyName(this.keyName)
                .preSignedUrl(this.preSignedUrl)
                .diaryId(this.diaryId)
                .build();
    }

    public Picture addPreSignedUrl(String preSignedUrl) {
        return Picture.builder()
                .id(this.id)
                .isMainPicture(false)
                .contentType(this.contentType)
                .keyName(this.keyName)
                .preSignedUrl(preSignedUrl)
                .diaryId(this.diaryId)
                .build();
    }


}
