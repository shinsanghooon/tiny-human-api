package com.tinyhuman.tinyhumanapi.checklist.domain;

import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistDetailCreate;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistDetailResponse;
import lombok.Builder;

public record ChecklistDetail(Long id, String contents, String reason, boolean isChecked, Long checklistId) {

    @Builder
    public ChecklistDetail {

    }

    public static ChecklistDetail fromCreate(ChecklistDetailCreate checklistDetailCreate) {
        return ChecklistDetail.builder()
                .contents(checklistDetailCreate.content())
                .reason(checklistDetailCreate.reason())
                .isChecked(false)
                .build();
    }

    public ChecklistDetail updateCheck() {
        return ChecklistDetail.builder()
                .id(this.id)
                .contents(this.contents)
                .reason(this.reason)
                .isChecked(!this.isChecked)
                .checklistId(this.checklistId)
                .build();
    }

    public ChecklistDetailResponse toResponseModel() {
        return ChecklistDetailResponse.builder()
                .id(this.id)
                .contents(this.contents)
                .reason(this.reason)
                .isChecked(this.isChecked)
                .build();
    }
}
