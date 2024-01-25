package com.tinyhuman.tinyhumanapi.checklist.domain;

import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistDetailCreate;
import lombok.Builder;

public record ChecklistDetail(Long id, String contents, String reason, boolean isChecked) {

    @Builder
    public ChecklistDetail {

    }

    public static ChecklistDetail fromCreate(ChecklistDetailCreate checklistDetailCreate) {
        return ChecklistDetail.builder()
                .contents(checklistDetailCreate.content())
                .reason("")
                .isChecked(false)
                .build();
    }
}
