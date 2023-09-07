package com.tinyhuman.tinyhumanapi.album.domain;

import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import lombok.Builder;

import java.time.LocalDateTime;

public record Album(Long id, Long babyId, ContentType contentType, String keyName, LocalDateTime createdAt, LocalDateTime originalCreatedAt, Long gpsLat, Long gpsLon, boolean isDeleted) {

    @Builder
    public Album {
    }

    public Album deleteAlbum() {
        return Album.builder()
                .id(this.id)
                .babyId(this.babyId)
                .contentType(this.contentType)
                .keyName(this.keyName)
                .createdAt(this.createdAt)
                .originalCreatedAt(this.originalCreatedAt)
                .gpsLat(this.gpsLat)
                .gpsLon(this.gpsLon)
                .isDeleted(true)
                .build();}
}
