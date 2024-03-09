package com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record HelpChatResponse(Long id, Long helpRequestId,Long helpRequestUserId, Long helpAnswerUserId, LocalDateTime createdAt) {

    @Builder
    public HelpChatResponse {

    }
}
