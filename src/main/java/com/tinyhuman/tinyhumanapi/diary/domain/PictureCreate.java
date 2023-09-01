package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;

public record PictureCreate(String fileName) {

    @Builder
    public PictureCreate {
    }
}
