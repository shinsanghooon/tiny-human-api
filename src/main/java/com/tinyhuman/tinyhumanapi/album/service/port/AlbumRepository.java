package com.tinyhuman.tinyhumanapi.album.service.port;

import com.tinyhuman.tinyhumanapi.album.domain.Album;

import java.util.List;
import java.util.Set;

public interface AlbumRepository {

    Album findByIdAndBabyId(Long id, Long babyId);

    List<Album> saveAll(List<Album> albums);

    List<Album> findAllByIds(List<Long> ids);

    List<Album> findByBabyId(Long babyId);

    List<Album> findByBabyIdAndKeyNameIn(Long babyId, Set<String> keyName);


}
