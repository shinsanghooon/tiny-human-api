package com.tinyhuman.tinyhumanapi.baby.controller.dto;

import lombok.Builder;

public record BabyImageUpdate(String fileName) {

    @Builder
    public BabyImageUpdate {
    }
}
