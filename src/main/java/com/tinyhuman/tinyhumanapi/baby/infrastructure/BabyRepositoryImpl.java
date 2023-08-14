package com.tinyhuman.tinyhumanapi.baby.infrastructure;


import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BabyRepositoryImpl implements BabyRepository {

    private final BabyJpaRepository babyJpaRepository;

    @Override
    public Baby save(Baby baby) {
        return babyJpaRepository.save(BabyEntity.fromModel(baby)).toModel();
    }

    @Override
    public Optional<Baby> findById(Long id) {
        return babyJpaRepository.findById(id).map(BabyEntity::toModel);
    }
}
