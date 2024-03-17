package com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto;

import lombok.Builder;

public record HelpChatCreate(Long helpRequestId, Long helpRequestUserId, Long helpAnswerUserId) {

    @Builder
    public HelpChatCreate {

    }
}
