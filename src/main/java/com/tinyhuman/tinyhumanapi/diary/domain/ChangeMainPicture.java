package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Builder;

public record ChangeMainPicture (Long currentPictureId, Long newPictureId){

    @Builder
    public ChangeMainPicture {
    }
}
