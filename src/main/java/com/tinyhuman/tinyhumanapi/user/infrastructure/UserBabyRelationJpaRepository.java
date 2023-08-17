package com.tinyhuman.tinyhumanapi.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBabyRelationJpaRepository extends JpaRepository<UserBabyRelationEntity, UserBabyMappingId> {

    List<UserBabyRelationEntity> findByUser(UserEntity user);

}
