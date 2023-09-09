package com.tinyhuman.tinyhumanapi.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tinyhuman.tinyhumanapi.common.exception.dto.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.tinyhuman.tinyhumanapi.common.exception.ErrorCode.INVALID_VALUE_REQUEST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ErrorResponse(BAD_REQUEST, BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ErrorResponse(BAD_REQUEST, BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public ErrorResponse handleIllegalStatesException(IllegalStateException ex) {
        return new ErrorResponse(BAD_REQUEST, BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * 입력값이 ENUM에 포함되지 않는 경우, 예외를 던집니다.
     *
     * @return 메시지가 담긴 응답
     */
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public ErrorResponse handleInvalidFormatException() {
        return new ErrorResponse(INVALID_VALUE_REQUEST.getHttpStatus(),
                INVALID_VALUE_REQUEST.getHttpStatus().value(), INVALID_VALUE_REQUEST.getMessage());
    }

    /**
     * 권한이 없는 작업을 진행할 경우, 예외를 던집니다.
     *
     * @param ex 권한이 없는 작업에 대한 예외
     * @return 메시지가 담긴 응답 메시지
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(code = FORBIDDEN)
    public ErrorResponse handleForbiddenException(UnauthorizedAccessException ex) {
        return new ErrorResponse(FORBIDDEN, FORBIDDEN.value(), ex.getMessage());
    }

    /**
     * 요청된 리소스가 없는 경우, 예외를 던집니다.
     *
     * @param ex 찾을 수 없는 요청에 대한 예외
     * @return 예외 메시지가 담긴 응답 메시지
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ErrorResponse(BAD_REQUEST, BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * 업로드 파일이 허용되지 않은 타입인 경우, 예외를 던집니다.
     *
     * @param ex 허용되지 않는 파일 요청에 대한 예외
     * @return 예외 메시지가 담긴 응답 메시지
     */
    @ExceptionHandler(NotSupportedContentTypeException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public ErrorResponse handleResourceNotFoundException(NotSupportedContentTypeException ex) {
        return new ErrorResponse(BAD_REQUEST, BAD_REQUEST.value(), ex.getMessage());
    }


}