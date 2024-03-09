package com.tinyhuman.tinyhumanapi.helpchat.domain;

import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatResponse;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpRequestCreate;
import com.tinyhuman.tinyhumanapi.helpchat.enums.RequestType;
import lombok.Builder;

import java.time.LocalDateTime;

public record HelpRequest(Long id, Long userId, RequestType requestType, String contents, LocalDateTime createdAt) {

    @Builder
    public HelpRequest {

    }

    public static HelpRequest fromCreate(HelpRequestCreate helpRequestCreate) {
        return HelpRequest.builder()
                .userId(helpRequestCreate.userId())
                .requestType(helpRequestCreate.requestType())
                .contents(helpRequestCreate.contents())
                .build();
    }
    public HelpChatResponse toResponse() {
        return HelpChatResponse.builder()
                .id(this.id)
                .userId(this.userId)
                .requestType(this.requestType)
                .contents(this.contents)
                .createdAt(this.createdAt)
                .build();
    }
}
