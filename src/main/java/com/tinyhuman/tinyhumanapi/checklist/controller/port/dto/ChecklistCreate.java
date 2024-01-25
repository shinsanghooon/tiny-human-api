package com.tinyhuman.tinyhumanapi.checklist.controller.port.dto;

import lombok.Builder;

import java.util.List;

public record ChecklistCreate(String title, List<ChecklistDetailCreate> checklistDetailCreate)  {

    @Builder
    public ChecklistCreate{}
}
