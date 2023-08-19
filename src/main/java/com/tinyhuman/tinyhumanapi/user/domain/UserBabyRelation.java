package com.tinyhuman.tinyhumanapi.user.domain;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.UserBabyRole;
import lombok.Builder;

public record UserBabyRelation(User user, Baby baby, FamilyRelation relation, UserBabyRole userBabyRole, boolean isDeleted) {

    @Builder
    public UserBabyRelation {
    }

    public boolean hasReadRole() {
        if (this.userBabyRole == UserBabyRole.UNKNOWN) {
            return false;
        }
        return true;
    }
}
