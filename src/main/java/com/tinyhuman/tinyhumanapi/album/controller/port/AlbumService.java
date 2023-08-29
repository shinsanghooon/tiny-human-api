package com.tinyhuman.tinyhumanapi.album.controller.port;

import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumDelete;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumResponse;
import com.tinyhuman.tinyhumanapi.album.domain.Album;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AlbumService {

    List<AlbumResponse> getAlbumsByBaby(Long babyId);

    List<AlbumResponse> uploadAlbums(Long babyId, List<MultipartFile> files);

    List<Album> delete(AlbumDelete albums);

    AlbumResponse findById(Long albumId, Long babyId);
}
