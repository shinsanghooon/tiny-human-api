package com.tinyhuman.tinyhumanapi.user.mock;

import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.service.port.UserBabyRelationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class FakeUserBabyRelationRepository implements UserBabyRelationRepository {

    private final List<UserBabyRelation> data = new ArrayList<>();
    @Override
    public UserBabyRelation save(UserBabyRelation userBabyRelation) {
        Predicate<UserBabyRelation> userEquals = u -> Objects.equals(u.user().id(), userBabyRelation.user().id());
        Predicate<UserBabyRelation> babyEquals = u -> Objects.equals(u.baby().id(), userBabyRelation.baby().id());
        data.removeIf(u -> userEquals.test(u) && babyEquals.test(u));
        data.add(userBabyRelation);
        return userBabyRelation;
    }

    @Override
    public List<UserBabyRelation> findByUser(User user) {
        return data.stream()
                .filter(r -> r.user().id().equals(user.id()))
                .toList();
    }
}
