package com.tinyhuman.tinyhumanapi.checklist.controller.port.dto;

import lombok.Builder;

import java.util.List;

public record ChecklistCreate(Long id, String title, List<ChecklistDetailCreate> checklistDetailCreate)  {

    @Builder
    public ChecklistCreate{}
}
