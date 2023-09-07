package com.tinyhuman.tinyhumanapi.album.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

public record AlbumCreate(
        @NotBlank
        String fileName,

        LocalDateTime originalCreatedAt,

        Long gpsLat,

        Long gptLon

        ) {

    @Builder
    public AlbumCreate {
    }
}
