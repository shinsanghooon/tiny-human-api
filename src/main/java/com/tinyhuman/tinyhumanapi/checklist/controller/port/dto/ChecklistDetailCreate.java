package com.tinyhuman.tinyhumanapi.checklist.controller.port.dto;

import lombok.Builder;

public record ChecklistDetailCreate(Long id, String contents, String reason, Boolean isChecked)  {

    @Builder
    public ChecklistDetailCreate {

    }
}
