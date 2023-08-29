package com.tinyhuman.tinyhumanapi.album.infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumJpaRepository extends JpaRepository<AlbumEntity, Long> {

}
