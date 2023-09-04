package com.tinyhuman.tinyhumanapi.album.controller.dto;

import com.tinyhuman.tinyhumanapi.integration.controller.dto.LastEvaluatedKey;
import lombok.Builder;

import java.util.List;

public record CursorAlbumResponse(List<AlbumResponse> albumResponses, LastEvaluatedKey lastEvaluatedKey) {

    @Builder
    public CursorAlbumResponse {
    }
}

