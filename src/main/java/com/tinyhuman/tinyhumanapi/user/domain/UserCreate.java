package com.tinyhuman.tinyhumanapi.user.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record UserCreate(
        @NotBlank(message = "이름을 입력해주세요.")
        @Size(max = 20, message = "이름의 최대 길이는 20자 입니다.")
        String name,

        @NotBlank(message = "메일을 입력해주세요.")
        @Email(message = "이메일 형식이 잘못되었습니다.")
        String email,

        @NotBlank(message = "패스워드를 입력해주세요.")
        String password
) {

    @Builder
    public UserCreate {
    }
}
