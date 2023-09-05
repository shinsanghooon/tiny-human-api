package com.tinyhuman.tinyhumanapi.album.controller.dto;

import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import lombok.Builder;

public record AlbumResponse(Long id, Long babyId, ContentType contentType, String keyName, String originalCreatedAt) {

    @Builder
    public AlbumResponse {
    }

    public static AlbumResponse fromModel(Album album) {
        return AlbumResponse.builder()
                .id(album.id())
                .babyId(album.babyId())
                .keyName(album.keyName())
                .contentType(album.contentType())
                .build();
    }
}
