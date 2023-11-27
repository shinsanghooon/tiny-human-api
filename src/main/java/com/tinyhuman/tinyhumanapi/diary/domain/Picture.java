package com.tinyhuman.tinyhumanapi.diary.domain;

import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import lombok.Builder;

public record Picture(Long id, boolean isMainPicture, ContentType contentType, String fileName, String keyName, String preSignedUrl, Long diaryId, boolean isDeleted) {

    @Builder
    public Picture {
    }

    public Picture assignToMain() {
        return Picture.builder()
                .id(this.id)
                .isMainPicture(true)
                .contentType(this.contentType)
                .fileName(this.fileName)
                .keyName(this.keyName)
                .preSignedUrl(this.preSignedUrl)
                .diaryId(this.diaryId)
                .isDeleted(this.isDeleted)
                .build();
    }

    public Picture assignToNormal() {
        return Picture.builder()
                .id(this.id)
                .isMainPicture(false)
                .contentType(this.contentType)
                .keyName(this.keyName)
                .fileName(this.fileName)
                .preSignedUrl(this.preSignedUrl)
                .diaryId(this.diaryId)
                .isDeleted(this.isDeleted)
                .build();
    }

    public Picture addPreSignedUrl(String preSignedUrl) {
        return Picture.builder()
                .id(this.id)
                .isMainPicture(this.isMainPicture)
                .contentType(this.contentType)
                .fileName(this.fileName)
                .keyName(this.keyName)
                .preSignedUrl(preSignedUrl)
                .diaryId(this.diaryId)
                .isDeleted(this.isDeleted)
                .build();
    }

    public Picture addFileName(String fileName) {
        return Picture.builder()
                .id(this.id)
                .isMainPicture(this.isMainPicture)
                .contentType(this.contentType)
                .fileName(fileName)
                .keyName(this.keyName)
                .preSignedUrl(this.preSignedUrl)
                .diaryId(this.diaryId)
                .isDeleted(this.isDeleted)
                .build();
    }

    public Picture delete() {
        return Picture.builder()
                .id(this.id)
                .isMainPicture(this.isMainPicture)
                .contentType(this.contentType)
                .fileName(this.fileName)
                .keyName(this.keyName)
                .preSignedUrl(this.preSignedUrl)
                .diaryId(this.diaryId)
                .isDeleted(true)
                .build();
    }


}
