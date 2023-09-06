package com.tinyhuman.tinyhumanapi.integration.controller.dto;

import lombok.Builder;

public record LastEvaluatedKey(String babyId, String originalCreatedAt) {

    @Builder
    public LastEvaluatedKey {
    }
}
