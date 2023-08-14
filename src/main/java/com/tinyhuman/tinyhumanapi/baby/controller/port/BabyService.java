package com.tinyhuman.tinyhumanapi.baby.controller.port;

import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BabyService {

    BabyResponse register(BabyCreate babyCreate, MultipartFile file);
}
