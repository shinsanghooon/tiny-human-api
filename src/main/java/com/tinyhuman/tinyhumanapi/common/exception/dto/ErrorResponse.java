package com.tinyhuman.tinyhumanapi.common.exception.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus httpStatus, int httpStatusCode, String errorMessage) {
    public ErrorResponse {
    }
}
