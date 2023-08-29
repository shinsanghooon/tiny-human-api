package com.tinyhuman.tinyhumanapi.album.infrastructure;


import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.album.service.port.AlbumRepository;
import com.tinyhuman.tinyhumanapi.common.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumRepositoryImpl implements AlbumRepository {

    private final AlbumJpaRepository albumJpaRepository;

    @Override
    public Album findByIdAndBabyId(Long id, Long babyId) {
        return albumJpaRepository.findByIdAndBabyId(id, babyId)
                .orElseThrow(() -> new ResourceNotFoundException("Album", id))
                .toModel();
    }

    @Override
    public List<Album> saveAll(List<Album> albums) {
        List<AlbumEntity> albumEntities = albums.stream().map(AlbumEntity::fromModel).toList();
        return albumJpaRepository.saveAll(albumEntities).stream().map(AlbumEntity::toModel).toList();
    }

    public List<Album> findAllByIds(List<Long> ids) {
        return albumJpaRepository.findAllById(ids).stream().map(AlbumEntity::toModel).toList();
    }

    @Override
    public List<Album> findByBabyId(Long babyId) {
        return albumJpaRepository.findByBabyId(babyId).stream().map(AlbumEntity::toModel).toList();
    }
}
