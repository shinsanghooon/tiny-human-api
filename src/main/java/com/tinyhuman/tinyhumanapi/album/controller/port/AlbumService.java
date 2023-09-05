package com.tinyhuman.tinyhumanapi.album.controller.port;

import com.tinyhuman.tinyhumanapi.album.controller.dto.*;
import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.integration.controller.dto.LastEvaluatedKey;

import java.util.List;

public interface AlbumService {

    List<AlbumResponse> getAlbumsByBaby(Long babyId);

    CursorAlbumResponse getAlbumsByBaby(Long babyId, LastEvaluatedKey lastEvaluatedKey);

    List<AlbumUploadResponse> uploadAlbums(Long babyId, List<AlbumCreate> files);

    List<Album> delete(AlbumDelete albums);

    AlbumResponse findByIdAndBabyId(Long albumId, Long babyId);
}
