package com.tinyhuman.tinyhumanapi.auth.domain;


import lombok.Builder;

public record RefreshToken(Long id, Long userId, String refreshToken) {


    @Builder
    public RefreshToken {
    }

    public RefreshToken update(String newRefreshToken) {
        return RefreshToken.builder()
                .id(this.id)
                .userId(this.userId)
                .refreshToken(newRefreshToken)
                .build();
    }

}
