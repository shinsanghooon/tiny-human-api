package com.tinyhuman.tinyhumanapi.album.infrastructure;


import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.album.service.port.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumRepositoryImpl implements AlbumRepository {

    private final AlbumJpaRepository albumJpaRepository;

    @Override
    public List<Album> saveAll(List<Album> albums) {
        List<AlbumEntity> albumEntities = albums.stream().map(AlbumEntity::fromModel).toList();
        return albumJpaRepository.saveAll(albumEntities).stream().map(AlbumEntity::toModel).toList();
    }
}
