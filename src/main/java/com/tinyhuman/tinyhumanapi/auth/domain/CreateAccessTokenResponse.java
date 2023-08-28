package com.tinyhuman.tinyhumanapi.auth.domain;

import lombok.Builder;

public record CreateAccessTokenResponse(String accessToken) {

    @Builder
    public CreateAccessTokenResponse {
    }
}
