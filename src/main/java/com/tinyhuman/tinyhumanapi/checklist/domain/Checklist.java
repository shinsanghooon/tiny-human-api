package com.tinyhuman.tinyhumanapi.checklist.domain;

import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistCreate;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistResponse;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import lombok.Builder;

import java.util.List;

public record Checklist(Long id, String title, User user, List<ChecklistDetail> checklistDetails, boolean isDeleted) {

    @Builder
    public Checklist {

    }

    public static Checklist fromCreate(ChecklistCreate checklistCreate, User user) {
        return Checklist.builder()
                .id(checklistCreate.id())
                .title(checklistCreate.title())
                .user(user)
                .isDeleted(false)
                .build();
    }

    public Checklist addChecklistDetail(List<ChecklistDetail> checklistDetails) {
        return Checklist.builder()
                .id(this.id)
                .title(this.title)
                .user(this.user)
                .checklistDetails(checklistDetails)
                .build();
    }

    public Checklist delete() {
        return Checklist.builder()
                .id(this.id)
                .title(this.title)
                .user(this.user)
                .checklistDetails(this.checklistDetails)
                .isDeleted(true)
                .build();
    }

    public ChecklistResponse toResponseModel() {
        return ChecklistResponse.builder()
                .id(this.id)
                .title(this.title)
                .checklistDetail(this.checklistDetails().stream().map(ChecklistDetail::toResponseModel).toList())
                .build();
    }
}
