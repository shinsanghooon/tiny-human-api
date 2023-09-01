package com.tinyhuman.tinyhumanapi.album.controller.dto;

import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import lombok.Builder;

public record AlbumResponse(Long id, Long babyId, ContentType contentType, String preSignedUrl) {

    @Builder
    public AlbumResponse {
    }

    public static AlbumResponse fromModel(Album album) {
        return AlbumResponse.builder()
                .id(album.id())
                .babyId(album.babyId())
                .preSignedUrl(album.preSignedUrl())
                .contentType(album.contentType())
                .build();
    }

    public static AlbumResponse fromModel(Album album, String preSignedUrl) {
        return AlbumResponse.builder()
                .id(album.id())
                .babyId(album.babyId())
                .preSignedUrl(preSignedUrl)
                .contentType(album.contentType())
                .build();
    }
}
