package com.tinyhuman.tinyhumanapi.diary.controller.port.dto;

import lombok.Builder;

import java.time.LocalDate;

public record ChangeDate(LocalDate updatedDate) {

    @Builder
    public ChangeDate {

    }
}
