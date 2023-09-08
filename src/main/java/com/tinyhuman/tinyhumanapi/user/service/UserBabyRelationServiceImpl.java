package com.tinyhuman.tinyhumanapi.user.service;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserBabyRelationService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.UserBabyRole;
import com.tinyhuman.tinyhumanapi.user.service.port.UserBabyRelationRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
}
