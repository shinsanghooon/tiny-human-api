package com.tinyhuman.tinyhumanapi.baby.service;

import com.tinyhuman.tinyhumanapi.baby.controller.port.BabyService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import com.tinyhuman.tinyhumanapi.integration.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BabyServiceImpl implements BabyService {

    private final BabyRepository babyRepository;

    private final ImageService imageService;

    @Value("${aws.s3.path.profile}")
    private String s3UploadPath;

    @Override
    public BabyResponse register(BabyCreate babyCreate, MultipartFile file) {

        String s3FullPath = imageService.sendImage(file, s3UploadPath);
        Baby newBaby = Baby.fromCreate(babyCreate, s3FullPath);

        return BabyResponse.fromModel(babyRepository.save(newBaby));
    }

    @Override
    public List<BabyResponse> getMyBabies() {
        // TODO: Login 구현 후 작업
        Long userId = 1L;
        return null;
    }
}
