package com.tinyhuman.tinyhumanapi.common.exception;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String datasource, long id) {
        super(datasource + "의 ID " + id + "에 접근 권한이 없습니다.");
    }

    public UnauthorizedAccessException(String datasource, String id) {
        super(datasource + "의 ID " + id + "에 접근 권한이 없습니다.");
    }
}

