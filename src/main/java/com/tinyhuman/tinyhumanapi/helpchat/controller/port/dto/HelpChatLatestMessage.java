package com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record HelpChatLatestMessage(Long helpRequestUserId, Long helpAnswerUserId,
                                    String message, LocalDateTime messageTime) {

    @Builder
    public HelpChatLatestMessage{}
}
