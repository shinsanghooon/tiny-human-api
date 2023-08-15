package com.tinyhuman.tinyhumanapi.baby.infrastructure;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.enums.Gender;
import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.diary.infrastructure.DiaryEntity;
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

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "baby")
    private final List<DiaryEntity> diaries = new ArrayList<>();

    @Builder
    public BabyEntity(Long id, String name, LocalDate dayOfBirth, int timeOfBirth, Gender gender,
                      String nickName, String profileImgUrl, Long userId) {
        this.id = id;
        this.name = name;
        this.dayOfBirth = dayOfBirth;
        this.timeOfBirth = timeOfBirth;
        this.gender = gender;
        this.nickName = nickName;
        this.profileImgUrl = profileImgUrl;
        this.userId = userId;
    }

    public static BabyEntity fromModel(Baby baby) {
        return BabyEntity.builder()
                .id(baby.id())
                .name(baby.name())
                .dayOfBirth(baby.dayOfBirth())
                .timeOfBirth(baby.timeOfBirth())
                .gender(baby.gender())
                .nickName(baby.nickName())
                .profileImgUrl(baby.profileImgUrl())
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
                .profileImgUrl(this.profileImgUrl)
                .build();
    }

    public void addDiary(DiaryEntity diary) {
        diaries.add(diary);
    }
}
