package com.tinyhuman.tinyhumanapi.diary.controller.port.dto;

import lombok.Builder;

public record PictureCreate(String fileName) {

    @Builder
    public PictureCreate {
    }
}
