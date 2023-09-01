package com.tinyhuman.tinyhumanapi.baby.domain;

import lombok.Builder;

public record BabyImageUpdate(String fileName) {

    @Builder
    public BabyImageUpdate {
    }
}
