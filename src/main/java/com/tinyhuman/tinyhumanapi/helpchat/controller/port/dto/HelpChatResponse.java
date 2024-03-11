package com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record HelpChatResponse(Long id, Long helpRequestId, Long helpRequestUserId, Long helpAnswerUserId, LocalDateTime createdAt, HelpRequestResponse helpRequest) {

    @Builder
    public HelpChatResponse {

    }

    public HelpChatResponse addHelpRequest(HelpRequestResponse helpRequestResponse) {
        return HelpChatResponse.builder()
                .id(this.id)
                .helpRequestId(this.helpRequestId)
                .helpRequestUserId(this.helpRequestUserId)
                .helpAnswerUserId(this.helpAnswerUserId)
                .createdAt(this.createdAt)
                .helpRequest(helpRequestResponse)
                .build();

    }
}
