package com.tinyhuman.tinyhumanapi.user.controller.port;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;

import java.util.List;

public interface UserBabyRelationService {

    UserBabyRelation establishRelationUserToBaby(BabyCreate babyCreate, User user, Baby baby);

    List<UserBabyRelation> findByUser(User user);

    UserBabyRelation findByMappingId(User user, Baby baby);

    UserBabyRelation update(UserBabyRelation userBabyRelation);
}
