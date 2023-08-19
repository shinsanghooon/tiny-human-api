package com.tinyhuman.tinyhumanapi.user.infrastructure;

import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.service.port.UserBabyRelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserBabyRelationRelationRepositoryImpl implements UserBabyRelationRepository {

    private final UserBabyRelationJpaRepository userBabyRelationJpaRepository;

    @Override
    public UserBabyRelation save(UserBabyRelation userBabyRelation) {
        UserBabyRelationEntity userBabyRelationEntity = UserBabyRelationEntity.fromModel(userBabyRelation);
        UserBabyRelationEntity relation = userBabyRelationJpaRepository.save(userBabyRelationEntity);
        return relation.toModel();
    }

    @Override
    public List<UserBabyRelation> findByUser(User user) {
        return userBabyRelationJpaRepository.findByUser(UserEntity.fromModel(user)).stream()
                .map(UserBabyRelationEntity::toModel)
                .toList();
    }

    @Override
    public Optional<UserBabyRelation> findById(UserBabyMappingId userBabyMappingId) {
        return userBabyRelationJpaRepository.findById(userBabyMappingId)
                .map(UserBabyRelationEntity::toModel);

    }
}
