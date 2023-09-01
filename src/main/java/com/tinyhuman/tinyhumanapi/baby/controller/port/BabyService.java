package com.tinyhuman.tinyhumanapi.baby.controller.port;

import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyUpdate;

import java.util.List;

public interface BabyService {

    BabyResponse register(BabyCreate babyCreate);

    BabyResponse findById(Long id);

    List<BabyResponse> getMyBabies();

    void delete(Long id);

    BabyResponse update(Long id, BabyUpdate babyUpdate);

    BabyResponse updateProfileImage(Long id, String fileName);

}
