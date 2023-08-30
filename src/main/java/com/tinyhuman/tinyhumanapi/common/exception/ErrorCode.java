package com.tinyhuman.tinyhumanapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public enum ErrorCode {

    NOT_BLANK(BAD_REQUEST, "입력 값이 필요합니다."),
    INVALID_VALUE_REQUEST(BAD_REQUEST, "입력 값이 유효하지 않습니다.");

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final HttpStatus httpStatus;

    private final String message;
}
