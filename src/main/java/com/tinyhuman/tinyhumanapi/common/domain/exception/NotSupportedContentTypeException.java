package com.tinyhuman.tinyhumanapi.common.domain.exception;

public class NotSupportedContentTypeException extends RuntimeException {

    public NotSupportedContentTypeException(String contentType) {
        super(contentType + "은 지원되지 않는 타입입니다.");
    }

}

