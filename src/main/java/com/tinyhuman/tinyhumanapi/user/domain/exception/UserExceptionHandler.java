package com.tinyhuman.tinyhumanapi.user.domain.exception;

import com.tinyhuman.tinyhumanapi.common.exception.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice()
public class UserExceptionHandler {
    @ExceptionHandler(EmailDuplicateException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public ErrorResponse handleEmailDuplicateException(EmailDuplicateException ex) {
        return new ErrorResponse(BAD_REQUEST, BAD_REQUEST.value(), ex.getMessage());
    }

}
