package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.baby.infrastructure.BabyEntity;
import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.user.infrastructure.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "diaries")
@Where(clause = "is_deleted=false")
@NoArgsConstructor
public class DiaryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "days_after_birth")
    private int daysAfterBirth;

    @Column(name="like_count")
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "diary")
    private final List<SentenceEntity> sentences = new ArrayList<>();

    @OneToMany(mappedBy = "diary")
    private final List<PictureEntity> pictures = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "baby_id")
    private BabyEntity baby;

    @Column(name="is_deleted")
    private boolean isDeleted = false;

    @Builder
    public DiaryEntity(Long id, int daysAfterBirth, UserEntity user, int
            likeCount, boolean isDeleted, BabyEntity baby) {
        this.id = id;
        this.daysAfterBirth = daysAfterBirth;
        this.user = setUser(user);
        this.likeCount = likeCount;
        this.baby = setBaby(baby);
        this.isDeleted = isDeleted;
    }

    private UserEntity setUser(UserEntity user) {
        if (this.user != null) {
            this.user.getDiaries().remove(this);
        }
        this.user = user;
        user.addDiary(this);

        return user;
    }

    private BabyEntity setBaby(BabyEntity baby) {
        if (this.baby != null) {
            this.baby.getDiaries().remove(this);
        }
        this.baby = baby;
        baby.addDiary(this);

        return baby;
    }

    public static DiaryEntity fromModel(Diary diary) {
        return DiaryEntity.builder()
                .id(diary.id())
                .daysAfterBirth(diary.daysAfterBirth())
                .user(UserEntity.fromModel(diary.user()))
                .likeCount(diary.likeCount())
                .baby(BabyEntity.fromModel(diary.baby()))
                .isDeleted(diary.isDeleted())
                .build();
    }

    public Diary toModel() {
        return Diary.builder()
                .id(this.id)
                .daysAfterBirth(this.daysAfterBirth)
                .user(this.user.toModel())
                .likeCount(this.likeCount)
                .created_at(this.getCreatedAt())
                .isDeleted(this.isDeleted)
                .baby(this.baby.toModel())
                .sentences(this.sentences.stream().map(SentenceEntity::toModel).toList())
                .pictures(this.pictures.stream().map(PictureEntity::toModel).toList())
                .build();
    }

    public void addSentence(SentenceEntity sentence) {
        sentences.add(sentence);
    }

    public void addPicture(PictureEntity picture) {
        pictures.add(picture);
    }

}
