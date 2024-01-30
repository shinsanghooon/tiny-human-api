package com.tinyhuman.tinyhumanapi.checklist.controller.port.dto;

import lombok.Builder;

public record ToggleAllUpdateRequest(boolean targetChecked) {

    @Builder
    public ToggleAllUpdateRequest {}
}
