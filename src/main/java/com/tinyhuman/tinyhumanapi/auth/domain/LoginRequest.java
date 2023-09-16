package com.tinyhuman.tinyhumanapi.auth.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {

    @Builder
    public LoginRequest {
    }
}
