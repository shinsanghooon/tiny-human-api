package com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto;

import com.tinyhuman.tinyhumanapi.helpchat.enums.RequestType;
import lombok.Builder;

import java.time.LocalDateTime;

public record HelpRequestResponse(Long id, Long userId, RequestType requestType, String contents, LocalDateTime createdAt) {

    @Builder
    public HelpRequestResponse {

    }
}
