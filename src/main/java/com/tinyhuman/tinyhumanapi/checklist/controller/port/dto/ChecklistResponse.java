package com.tinyhuman.tinyhumanapi.checklist.controller.port.dto;

import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;
import lombok.Builder;

import java.util.List;

public record ChecklistResponse(Long id, String title, List<ChecklistDetail> checklistDetail) {

    @Builder
    public ChecklistResponse{}

}
