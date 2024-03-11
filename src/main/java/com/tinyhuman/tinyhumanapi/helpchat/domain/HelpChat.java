package com.tinyhuman.tinyhumanapi.helpchat.domain;

import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatCreate;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatResponse;
import lombok.Builder;

import java.time.LocalDateTime;

public record HelpChat(Long id, Long helpRequestId, Long helpRequestUserId, Long helpAnswerUserId, String latestMessage, LocalDateTime latestMessageTime, LocalDateTime createdAt) {

    @Builder
    public HelpChat {

    }

    public static HelpChat fromCreate(HelpChatCreate helpChatCreate) {
        return HelpChat.builder()
                .helpRequestId(helpChatCreate.helpRequestId())
                .helpRequestUserId(helpChatCreate.helpRequestUserId())
                .helpAnswerUserId(helpChatCreate.helpAnswerUserId())
                .build();
    }
    public HelpChatResponse toResponse() {
        return HelpChatResponse.builder()
                .id(this.id)
                .helpRequestId(this.helpRequestId)
                .helpRequestUserId(this.helpRequestUserId)
                .helpAnswerUserId(this.helpAnswerUserId)
                .latestMessage(this.latestMessage)
                .latestMessageTime(this.latestMessageTime)
                .createdAt(this.createdAt)
                .build();
    }
}
