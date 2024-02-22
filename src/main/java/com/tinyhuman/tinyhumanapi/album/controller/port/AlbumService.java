package com.tinyhuman.tinyhumanapi.album.controller.port;

import com.tinyhuman.tinyhumanapi.album.controller.dto.*;
import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import com.tinyhuman.tinyhumanapi.common.utils.PageCursor;

import java.util.List;

public interface AlbumService {

    List<AlbumUploadResponse> uploadAlbums(Long babyId, List<AlbumCreate> files);

    PageCursor<AlbumResponse> getAlbumsByBaby(Long babyId, CursorRequest cursorRequest, String order);

    List<Album> delete(AlbumDelete albums);

    AlbumResponse findByIdAndBabyId(Long albumId, Long babyId);

    List<Album> updateOriginalDate(Long babyId, AlbumDateUpdate albumDateUpdate);
}
