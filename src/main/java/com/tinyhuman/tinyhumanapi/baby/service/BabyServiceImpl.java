package com.tinyhuman.tinyhumanapi.baby.service;

import com.tinyhuman.tinyhumanapi.baby.controller.port.BabyService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BabyServiceImpl implements BabyService {

    private final BabyRepository babyRepository;
    @Override
    public BabyResponse register(BabyCreate babyCreate) {
        Baby newbaby = Baby.builder()
                .name(babyCreate.name())
                .dayOfBirth(babyCreate.dayOfBirth())
                .timeOfBirth(babyCreate.timeOfBirth())
                .nickName(babyCreate.nickName())
                .gender(babyCreate.gender())
                .profileImgUrl(babyCreate.profileImgUrl())
                .build();

        return BabyResponse.fromModel(babyRepository.save(newbaby));
    }
}
