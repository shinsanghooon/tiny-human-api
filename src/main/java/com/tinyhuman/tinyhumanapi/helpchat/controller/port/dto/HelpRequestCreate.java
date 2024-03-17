package com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto;

import com.tinyhuman.tinyhumanapi.helpchat.enums.RequestType;
import lombok.Builder;

public record HelpRequestCreate(Long userId, RequestType requestType, String contents) {

    @Builder
    public HelpRequestCreate {

    }
}
