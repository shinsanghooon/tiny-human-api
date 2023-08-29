package com.tinyhuman.tinyhumanapi.album.service.port;

import com.tinyhuman.tinyhumanapi.album.domain.Album;

import java.util.List;

public interface AlbumRepository {
    List<Album> saveAll(List<Album> albums);

    List<Album> findAllByIds(List<Long> ids);
}
