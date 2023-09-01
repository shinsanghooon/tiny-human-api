package com.tinyhuman.tinyhumanapi.album.controller.port;

import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumCreate;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumDelete;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumResponse;
import com.tinyhuman.tinyhumanapi.album.domain.Album;

import java.util.List;

public interface AlbumService {

    List<AlbumResponse> getAlbumsByBaby(Long babyId);

    List<AlbumResponse> uploadAlbums(Long babyId, List<AlbumCreate> files);

    List<Album> delete(AlbumDelete albums);

    AlbumResponse findByIdAndBabyId(Long albumId, Long babyId);
}
