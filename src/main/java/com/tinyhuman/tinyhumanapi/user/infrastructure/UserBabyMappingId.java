package com.tinyhuman.tinyhumanapi.user.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
public class UserBabyMappingId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "baby_id")
    private Long babyId;

    @Builder
    public UserBabyMappingId(Long userId, Long babyId) {
        this.userId = userId;
        this.babyId = babyId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserBabyMappingId) obj;
        return Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.babyId, that.babyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, babyId);
    }
}

