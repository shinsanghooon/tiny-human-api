package com.tinyhuman.tinyhumanapi.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBabyRelationJpaRepository extends JpaRepository<UserBabyRelationEntity, UserBabyMappingId> {

    List<UserBabyRelationEntity> findByUser(UserEntity user);

    Optional<UserBabyRelationEntity> findById(UserBabyMappingId userBabyMappingId);

}
