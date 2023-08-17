package com.tinyhuman.tinyhumanapi.user.infrastructure;


import com.tinyhuman.tinyhumanapi.baby.infrastructure.BabyEntity;
import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.FamilyRelation;
import com.tinyhuman.tinyhumanapi.user.enums.UserBabyRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Table(name = "user_baby_relations")
@Where(clause = "is_deleted=false")
@NoArgsConstructor
public class UserBabyRelationEntity extends BaseEntity {

    @EmbeddedId
    private UserBabyMappingId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("babyId")
    @JoinColumn(name = "baby_id")
    private BabyEntity baby;

    @Column(name="relation")
    @Enumerated(EnumType.STRING)
    private FamilyRelation relation;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private UserBabyRole role;

    @Column(name="is_deleted")
    private boolean isDeleted = false;

    @Builder
    public UserBabyRelationEntity(UserBabyMappingId id, UserEntity user, BabyEntity baby, FamilyRelation relation, UserBabyRole role, boolean isDeleted) {
        this.id = id;
        this.user = setUser(user);
        this.baby = setBaby(baby);
        this.relation = relation;
        this.role = role;
        this.isDeleted = isDeleted;
    }

    public UserBabyRelation toModel() {
        return UserBabyRelation.builder()
                .user(this.user.toModel())
                .baby(this.baby.toModel())
                .relation(this.relation)
                .userBabyRole(this.role)
                .isDeleted(this.isDeleted)
                .build();
    }

    public static UserBabyRelationEntity fromModel(UserBabyRelation userBabyRelation) {
        return UserBabyRelationEntity.builder()
                .id(UserBabyMappingId.builder()
                        .userId(userBabyRelation.user().id())
                        .babyId(userBabyRelation.baby().id())
                        .build())
                .user(UserEntity.fromModel(userBabyRelation.user()))
                .baby(BabyEntity.fromModel(userBabyRelation.baby()))
                .relation(userBabyRelation.relation())
                .role(userBabyRelation.userBabyRole())
                .isDeleted(userBabyRelation.isDeleted())
                .build();
    }

    public boolean isAdmin() {
        return this.role.equals(UserBabyRole.ADMIN);
    }

    private UserEntity setUser(UserEntity user) {
        if (this.user != null) {
            this.user.getUserBabyRelations().remove(this);
        }
        this.user = user;
        user.addRelation(this);

        return user;
    }

    private BabyEntity setBaby(BabyEntity baby) {
        if (this.baby != null) {
            this.baby.getUserBabyRelations().remove(this);
        }
        this.baby = baby;
        baby.addRelation(this);

        return baby;
    }

}
