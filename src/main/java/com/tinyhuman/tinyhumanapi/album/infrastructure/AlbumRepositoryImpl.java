package com.tinyhuman.tinyhumanapi.album.infrastructure;


import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.album.service.port.AlbumRepository;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class AlbumRepositoryImpl implements AlbumRepository {

    private final AlbumJpaRepository albumJpaRepository;


    @Override
    public List<Album> saveAll(List<Album> albums) {
        List<AlbumEntity> albumEntities = albums.stream()
                .map(AlbumEntity::fromModel)
                .toList();
        return albumJpaRepository.saveAll(albumEntities).stream()
                .map(AlbumEntity::toModel)
                .toList();
    }

    // TODO: 얘는 왜 여기서 예외를 던지지?
    @Override
    public Album findByIdAndBabyId(Long id, Long babyId) {
        return albumJpaRepository.findByIdAndBabyId(id, babyId)
                .orElseThrow(() -> new ResourceNotFoundException("Album", id))
                .toModel();
    }

    public List<Album> findAllByIds(List<Long> ids) {
        return albumJpaRepository.findAllById(ids).stream()
                .map(AlbumEntity::toModel)
                .toList();
    }

    @Override
    public List<Album> findByBabyId(Long babyId, CursorRequest cursorRequest) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id"); // 정렬 정보 설정
        Pageable pageable = PageRequest.of(0, cursorRequest.size(), sort);

        if (cursorRequest.hasKey()) {
            return albumJpaRepository.findByBabyIdAndIdLessThan(babyId, cursorRequest.key(), pageable).stream()
                    .map(AlbumEntity::toModel)
                    .toList();
        }
        return albumJpaRepository.findByBabyId(babyId, pageable).stream()
                .map(AlbumEntity::toModel)
                .toList();
    }

    @Override
    public List<Album> findByBabyId(Long babyId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id"); // 정렬 정보 설정
        Pageable pageable = PageRequest.of(0, 10, sort);

        return albumJpaRepository.findByBabyId(babyId, pageable).stream()
                .map(AlbumEntity::toModel)
                .toList();
    }

}
