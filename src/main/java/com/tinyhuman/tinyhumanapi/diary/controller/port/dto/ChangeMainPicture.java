package com.tinyhuman.tinyhumanapi.diary.controller.port.dto;

import lombok.Builder;

public record ChangeMainPicture (Long currentPictureId, Long newPictureId){

    @Builder
    public ChangeMainPicture {
    }
}
