package com.tinyhuman.tinyhumanapi.album.infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumJpaRepository extends JpaRepository<AlbumEntity, Long> {


}
