package com.tinyhuman.tinyhumanapi.user.service.port;

import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.infrastructure.UserBabyMappingId;

import java.util.List;
import java.util.Optional;

public interface UserBabyRelationRepository {

    UserBabyRelation save(UserBabyRelation userBabyRelation);

    List<UserBabyRelation> findByUser(User user);

    Optional<UserBabyRelation> findById(UserBabyMappingId userBabyMappingId);
}
