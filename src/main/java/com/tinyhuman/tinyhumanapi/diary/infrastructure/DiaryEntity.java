package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.baby.infrastructure.BabyEntity;
import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
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

    @Column(name="writer")
    private String writer;

    @Column(name="like_count")
    private int likeCount;

    @OneToMany(mappedBy = "diary")
    private final List<SentenceEntity> sentences = new ArrayList<>();

    @OneToMany(mappedBy = "diary")
    private final List<PictureEntity> pictures = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "baby_id")
    private BabyEntity baby;

    @Column(name="is_deleted")
    private Boolean isDeleted = false;

    @Builder
    public DiaryEntity(Long id, int daysAfterBirth, String writer, int likeCount, boolean isDeleted, BabyEntity baby) {
        this.id = id;
        this.daysAfterBirth = daysAfterBirth;
        this.writer = writer;
        this.likeCount = likeCount;
        this.baby = setBaby(baby);
        this.isDeleted = isDeleted;
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
                .writer(diary.writer())
                .likeCount(diary.likeCount())
                .baby(BabyEntity.fromModel(diary.baby()))
                .isDeleted(diary.isDeleted())
                .build();
    }

    public Diary toModel() {
        return Diary.builder()
                .id(this.id)
                .daysAfterBirth(this.daysAfterBirth)
                .writer(this.writer)
                .likeCount(this.likeCount)
                .created_at(this.getCreatedAt())
                .isDeleted(this.isDeleted)
                .baby(this.baby.toModel())
                .build();
    }

    public void addSentence(SentenceEntity sentence) {
        sentences.add(sentence);
    }

    public void addPicture(PictureEntity picture) {
        pictures.add(picture);
    }

}
