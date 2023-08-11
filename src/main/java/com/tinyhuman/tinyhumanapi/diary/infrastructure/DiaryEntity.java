package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

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

    @Column(name="is_public")
    private Boolean isPublic;

    @Column(name="likeCount")
    private int likeCount;

    @Builder
    public DiaryEntity(Long id, int daysAfterBirth, String writer, Boolean isPublic, int likeCount) {
        this.id = id;
        this.daysAfterBirth = daysAfterBirth;
        this.writer = writer;
        this.isPublic = isPublic;
        this.likeCount = likeCount;
    }

    public static DiaryEntity fromModel(Diary diary) {
        return DiaryEntity.builder()
                .id(diary.getId())
                .daysAfterBirth(diary.getDaysAfterBirth())
                .writer(diary.getWriter())
                .isPublic(diary.getIsPublic())
                .likeCount(diary.getLikeCount())
                .build();
    }

    public Diary toModel() {
        return Diary.builder()
                .id(this.id)
                .daysAfterBirth(this.daysAfterBirth)
                .writer(this.writer)
                .isPublic(this.isPublic)
                .likeCount(this.likeCount)
                .created_at(this.getCreatedAt())
                .build();

    }

}
