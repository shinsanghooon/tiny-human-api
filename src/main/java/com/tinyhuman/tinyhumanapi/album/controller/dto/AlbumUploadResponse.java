package com.tinyhuman.tinyhumanapi.album.controller.dto;

import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import lombok.Builder;

public record AlbumUploadResponse(Long id, Long babyId, ContentType contentType, String filename, String preSignedUrl, String originalCreatedAt) {

    @Builder
    public AlbumUploadResponse {
    }

    public static AlbumUploadResponse fromModel(Album album) {
        return AlbumUploadResponse.builder()
                .id(album.id())
                .babyId(album.babyId())
                .contentType(album.contentType())
                .build();
    }

    public AlbumUploadResponse with(String filename, String preSignedUrl, String originalCreatedAt) {
        return AlbumUploadResponse.builder()
                .id(this.id)
                .babyId(this.babyId)
                .filename(filename)
                .preSignedUrl(preSignedUrl)
                .contentType(this.contentType)
                .originalCreatedAt(originalCreatedAt)
                .build();
    }
}
