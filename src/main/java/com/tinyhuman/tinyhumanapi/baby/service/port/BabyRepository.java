package com.tinyhuman.tinyhumanapi.baby.service.port;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;

import java.util.Optional;

public interface BabyRepository {

    Baby save(Baby baby);

    Optional<Baby> findById(Long id);
}
