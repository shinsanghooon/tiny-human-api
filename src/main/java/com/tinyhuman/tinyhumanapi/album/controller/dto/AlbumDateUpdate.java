package com.tinyhuman.tinyhumanapi.album.controller.dto;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record AlbumDateUpdate(List<Long> ids, @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss") LocalDateTime originalCreatedAt) {

    @Builder
    public AlbumDateUpdate {
    }
}
