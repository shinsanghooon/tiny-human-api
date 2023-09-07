package com.tinyhuman.tinyhumanapi.album.controller.dto;

import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import lombok.Builder;

import java.time.LocalDateTime;

public record AlbumUploadResponse(Long id, Long babyId, ContentType contentType, String filename, String preSignedUrl, LocalDateTime originalCreatedAt, LocalDateTime createdAt) {

    @Builder
    public AlbumUploadResponse {
    }

    public static AlbumUploadResponse fromModel(Album album) {
        return AlbumUploadResponse.builder()
                .id(album.id())
                .babyId(album.babyId())
                .contentType(album.contentType())
                .originalCreatedAt(album.originalCreatedAt())
                .createdAt(album.createdAt())
                .build();
    }

    public AlbumUploadResponse with(String filename, String preSignedUrl) {
        return AlbumUploadResponse.builder()
                .id(this.id)
                .babyId(this.babyId)
                .filename(filename)
                .preSignedUrl(preSignedUrl)
                .contentType(this.contentType)
                .originalCreatedAt(this.originalCreatedAt)
                .createdAt(this.createdAt)
                .build();
    }
}
