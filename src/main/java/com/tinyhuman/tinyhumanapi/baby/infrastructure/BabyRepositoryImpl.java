package com.tinyhuman.tinyhumanapi.baby.infrastructure;


import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BabyRepositoryImpl implements BabyRepository {

    private final BabyJpaRepository babyJpaRepository;

    @Override
    public Baby save(Baby baby) {
        return babyJpaRepository.save(BabyEntity.fromModel(baby)).toModel();
    }
}
