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
                .id(checklistDetailCreate.id())
                .contents(checklistDetailCreate.contents())
                .reason(checklistDetailCreate.reason())
                .isChecked(checklistDetailCreate.isChecked() != null && checklistDetailCreate.isChecked())
                .build();
    }

    public ChecklistDetail toggleUpdate() {
        return ChecklistDetail.builder()
                .id(this.id)
                .contents(this.contents)
                .reason(this.reason)
                .isChecked(!this.isChecked)
                .checklistId(this.checklistId)
                .build();
    }

    public ChecklistDetail toggleAllUpdate(boolean isChecked) {
        return ChecklistDetail.builder()
                .id(this.id)
                .contents(this.contents)
                .reason(this.reason)
                .isChecked(isChecked)
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
