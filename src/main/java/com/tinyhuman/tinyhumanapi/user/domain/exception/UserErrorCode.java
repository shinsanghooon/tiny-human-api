package com.tinyhuman.tinyhumanapi.user.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public enum UserErrorCode {

    USER_NOT_FOUND(BAD_REQUEST, "사용자를 찾지 못했습니다."),
    EMAIL_DUPLICATE(BAD_REQUEST, "중복된 이메일이 존재합니다."),
    EMAIL_INVALID_FORMAT(BAD_REQUEST, "이메일 형식이 잘못되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    UserErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

