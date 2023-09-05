package com.tinyhuman.tinyhumanapi.baby.controller.port;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyPreSignedUrlResponse;
import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyUpdate;

import java.util.List;

public interface BabyService {

    BabyPreSignedUrlResponse register(BabyCreate babyCreate);

    BabyResponse findById(Long id);

    List<BabyResponse> getMyBabies();

    void delete(Long id);

    BabyResponse update(Long id, BabyUpdate babyUpdate);

    BabyPreSignedUrlResponse updateProfileImage(Long id, String fileName);

}
