package com.tinyhuman.tinyhumanapi.user.service.port;

import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;

import java.util.List;

public interface UserBabyRelationRepository {

    UserBabyRelation save(UserBabyRelation userBabyRelation);

    List<UserBabyRelation> findByUser(User user);
}
