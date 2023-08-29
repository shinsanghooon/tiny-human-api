package com.tinyhuman.tinyhumanapi.album.controller.dto;

import lombok.Builder;

import java.util.List;

public record AlbumDelete(List<Long> ids) {

    @Builder
    public AlbumDelete {
    }
}
