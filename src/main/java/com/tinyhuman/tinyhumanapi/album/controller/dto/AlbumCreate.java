package com.tinyhuman.tinyhumanapi.album.controller.dto;

import lombok.Builder;

public record AlbumCreate(String fileName) {

    @Builder
    public AlbumCreate {
    }
}
