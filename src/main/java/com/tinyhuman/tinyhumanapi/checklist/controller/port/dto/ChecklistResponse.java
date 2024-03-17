package com.tinyhuman.tinyhumanapi.checklist.controller.port.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record ChecklistResponse(Long id, String title, LocalDateTime createdAt, List<ChecklistDetailResponse> checklistDetail) {

    @Builder
    public ChecklistResponse{}

}
