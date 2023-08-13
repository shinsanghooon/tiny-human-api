package com.tinyhuman.tinyhumanapi.baby.service.port;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;

public interface BabyRepository {

    Baby save(Baby baby);
}
