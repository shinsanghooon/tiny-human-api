package com.tinyhuman.tinyhumanapi.album.infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumJpaRepository extends JpaRepository<AlbumEntity, Long> {

    Optional<AlbumEntity> findByIdAndBabyId(Long id, Long babyId);

}
