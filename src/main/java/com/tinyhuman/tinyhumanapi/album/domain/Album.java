package com.tinyhuman.tinyhumanapi.album.domain;

import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import lombok.Builder;

public record Album(Long id, Long babyId, ContentType contentType, String keyName, String preSignedUrl, boolean isDeleted) {

    @Builder
    public Album {
    }

    public Album deleteAlbum() {
        return Album.builder()
                .id(this.id)
                .babyId(this.babyId)
                .contentType(this.contentType)
                .keyName(this.keyName)
                .preSignedUrl(this.preSignedUrl)
                .isDeleted(true)
                .build();}
}
