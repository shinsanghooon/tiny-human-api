package com.tinyhuman.tinyhumanapi.auth.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record SocialLoginRequest(@Email @NotBlank String email, @NotBlank String socialAccessToken, String name, String photoUrl) {

    @Builder
    public SocialLoginRequest {
    }
}
