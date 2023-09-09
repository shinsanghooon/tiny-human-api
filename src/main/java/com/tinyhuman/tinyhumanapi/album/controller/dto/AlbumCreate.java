package com.tinyhuman.tinyhumanapi.album.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record AlbumCreate(
        @NotBlank
        String fileName,

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime originalCreatedAt,

        Double gpsLat,

        Double gptLon

        ) {

    @Builder
    public AlbumCreate {
    }
}
