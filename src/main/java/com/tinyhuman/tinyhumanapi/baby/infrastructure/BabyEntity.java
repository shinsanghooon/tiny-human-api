package com.tinyhuman.tinyhumanapi.baby.infrastructure;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.diary.infrastructure.DiaryEntity;
import com.tinyhuman.tinyhumanapi.user.infrastructure.UserBabyRelationEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "babies")
@Where(clause = "is_deleted=false")
@NoArgsConstructor
public class BabyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "day_of_birth")
    private LocalDate dayOfBirth;

    @Column(name = "time_of_birth")
    private int timeOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "profile_img_key_name")
    private String profileImgKeyName;

    @Column(name = "description", length = 900)
    private String description;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "baby")
    private final List<DiaryEntity> diaries = new ArrayList<>();

    @Column(name="is_deleted")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "baby")
    private final List<UserBabyRelationEntity> userBabyRelations = new ArrayList<>();

    @Builder
    public BabyEntity(Long id, String name, LocalDate dayOfBirth, int timeOfBirth, Gender gender, String nickName, String profileImgKeyName, String description, Long userId, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.dayOfBirth = dayOfBirth;
        this.timeOfBirth = timeOfBirth;
        this.gender = gender;
        this.nickName = nickName;
        this.profileImgKeyName = profileImgKeyName;
        this.description = description;
        this.userId = userId;
        this.isDeleted = isDeleted;
    }

    public static BabyEntity fromModel(Baby baby) {
        return BabyEntity.builder()
                .id(baby.id())
                .name(baby.name())
                .dayOfBirth(baby.dayOfBirth())
                .timeOfBirth(baby.timeOfBirth())
                .gender(baby.gender())
                .nickName(baby.nickName())
                .profileImgKeyName(baby.profileImgKeyName())
                .description(baby.description())
                .userId(baby.userId())
                .isDeleted(baby.isDeleted())
                .build();
    }

    public Baby toModel() {
        return Baby.builder()
                .id(this.id)
                .name(this.name)
                .dayOfBirth(this.dayOfBirth)
                .timeOfBirth(this.timeOfBirth)
                .gender(this.gender)
                .nickName(this.nickName)
                .profileImgKeyName(this.profileImgKeyName)
                .description(this.description)
                .userId(this.userId)
                .isDeleted(this.isDeleted)
                .build();
    }

    public void addDiary(DiaryEntity diary) {
        diaries.add(diary);
    }

    public void addRelation(UserBabyRelationEntity userBabyRelation) {
        userBabyRelations.add(userBabyRelation);
    }
}
