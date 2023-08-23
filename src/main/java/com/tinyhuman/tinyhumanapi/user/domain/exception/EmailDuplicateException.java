package com.tinyhuman.tinyhumanapi.user.domain.exception;


/**
 * 중복된 이메일로 등록하려는 경우 던집니다.
 */
public class EmailDuplicateException extends RuntimeException{
    public EmailDuplicateException() {
        super(UserErrorCode.EMAIL_DUPLICATE.getMessage());
    }
}

