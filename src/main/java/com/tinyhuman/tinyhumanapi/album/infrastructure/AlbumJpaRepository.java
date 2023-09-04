package com.tinyhuman.tinyhumanapi.album.infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AlbumJpaRepository extends JpaRepository<AlbumEntity, Long> {

    Optional<AlbumEntity> findByIdAndBabyId(Long id, Long babyId);

    List<AlbumEntity> findByBabyId(Long babyId);

    List<AlbumEntity> findByBabyIdAndKeyNameIn(Long babyId, Set<String> keyName);

}
