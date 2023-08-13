package com.tinyhuman.tinyhumanapi.baby.controller.port;

import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;

public interface BabyService {

    BabyResponse register(BabyCreate babyCreate);
}
