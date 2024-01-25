package com.tinyhuman.tinyhumanapi.checklist.controller.port.dto;

import lombok.Builder;

public record ChecklistDetailCreate(String content, String reason)  {

    @Builder
    public ChecklistDetailCreate {

    }
}
