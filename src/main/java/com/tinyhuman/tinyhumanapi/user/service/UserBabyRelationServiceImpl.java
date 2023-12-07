package com.tinyhuman.tinyhumanapi.user.service;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserBabyRelationService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.UserBabyRole;
import com.tinyhuman.tinyhumanapi.user.infrastructure.UserBabyMappingId;
import com.tinyhuman.tinyhumanapi.user.service.port.UserBabyRelationRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional
public class UserBabyRelationServiceImpl implements UserBabyRelationService {
    private final UserBabyRelationRepository userBabyRelationRepository;

    @Builder
    public UserBabyRelationServiceImpl(UserBabyRelationRepository userBabyRelationRepository) {
        this.userBabyRelationRepository = userBabyRelationRepository;
    }

    @Override
    public UserBabyRelation establishRelationUserToBaby(BabyCreate babyCreate, User user, Baby baby) {
        UserBabyRelation userBabyRelation = UserBabyRelation.builder()
                .user(user)
                .baby(baby)
                .relation(babyCreate.relation())
                .userBabyRole(UserBabyRole.ADMIN)
                .build();

        return userBabyRelationRepository.save(userBabyRelation);
    }

    @Override
    public List<UserBabyRelation> findByUser(User user) {
        return userBabyRelationRepository.findByUser(user);
    }

    @Override
    public UserBabyRelation findByMappingId(User user, Baby baby) {
        UserBabyMappingId mappingId = UserBabyMappingId.builder()
                .userId(user.id())
                .babyId(baby.id())
                .build();
        return userBabyRelationRepository.findById(mappingId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - babyId:{}", baby.id());
                    return new ResourceNotFoundException("Baby", baby.id());
                });
    }

    @Override
    public UserBabyRelation update(UserBabyRelation userBabyRelation) {
        return userBabyRelationRepository.save(userBabyRelation);
    }
}
