package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Table(name = "sentences")
@Where(clause = "is_deleted=false")
@NoArgsConstructor
public class SentenceEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sentence", length = 900)
    private String sentence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private DiaryEntity diary;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public SentenceEntity(Long id, String sentence, DiaryEntity diary, boolean isDeleted) {
        this.id = id;
        this.sentence = sentence;
        this.diary = setDiary(diary);
        this.isDeleted = isDeleted;
    }

    private DiaryEntity setDiary(DiaryEntity diary) {
        if (this.diary != null) {
            this.diary.getSentences().remove(this);
        }
        this.diary = diary;
        diary.addSentence(this);

        return diary;
    }

    public static SentenceEntity fromModel(Sentence sentence, Diary diary) {
        return SentenceEntity.builder()
                .id(sentence.id())
                .sentence(sentence.sentence())
                .diary(DiaryEntity.fromModel(diary))
                .isDeleted(sentence.isDeleted())
                .build();
    }


    public Sentence toModel() {
        return Sentence.builder()
                .id(this.id)
                .sentence(this.sentence)
                .diaryId(this.diary.getId())
                .isDeleted(this.isDeleted)
                .build();
    }
}
