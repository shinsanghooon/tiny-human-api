package com.tinyhuman.tinyhumanapi.auth.domain;

import lombok.Builder;

public record TokenResponse(String accessToken, String refreshToken) {

    @Builder
    public TokenResponse {
    }
}
