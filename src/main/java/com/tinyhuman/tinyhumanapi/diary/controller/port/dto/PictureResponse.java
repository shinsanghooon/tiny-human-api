package com.tinyhuman.tinyhumanapi.diary.controller.port.dto;

import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import lombok.Builder;

public record PictureResponse(Long id, boolean isMainPicture, ContentType contentType, String keyName) {

    @Builder
    public PictureResponse {
    }

    public static PictureResponse fromModel(Picture picture) {
        return PictureResponse.builder()
                .id(picture.id())
                .isMainPicture(picture.isMainPicture())
                .contentType(picture.contentType())
                .keyName(picture.keyName())
                .build();
    }
}
