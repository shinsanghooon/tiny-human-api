package com.tinyhuman.tinyhumanapi.checklist.controller.port.dto;

import lombok.Builder;

public record ChecklistDetailResponse(Long id, String contents, String reason, boolean isChecked) {

    @Builder
    public ChecklistDetailResponse{

    }
}
