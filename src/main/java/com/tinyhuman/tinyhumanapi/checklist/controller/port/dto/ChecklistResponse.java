package com.tinyhuman.tinyhumanapi.checklist.controller.port.dto;

import lombok.Builder;

import java.util.List;

public record ChecklistResponse(Long id, String title, List<ChecklistDetailResponse> checklistDetail) {

    @Builder
    public ChecklistResponse{}

}
