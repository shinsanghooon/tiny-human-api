package com.tinyhuman.tinyhumanapi.album.domain;

import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import lombok.Builder;

public record Album(Long id, Long babyId, ContentType contentType, String originalS3Url, boolean isDeleted) {

    @Builder
    public Album {
    }
}
