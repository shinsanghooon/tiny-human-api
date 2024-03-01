package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.diary.domain.CounselorLetter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "counselor_letter")
@NoArgsConstructor
public class CounselorLetterEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "baby_id")
    private Long babyId;

    @Column(name = "diary_id")
    private Long diaryId;

    @Column(name = "contents")
    private String contents;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public CounselorLetterEntity(Long id, Long babyId, Long diaryId, String contents, LocalDateTime createdAt) {
        this.id = id;
        this.babyId = babyId;
        this.diaryId = diaryId;
        this.contents = contents;
        this.createdAt = createdAt;
    }

    public CounselorLetter toModel() {
        return CounselorLetter.builder()
                .id(this.id)
                .babyId(this.babyId)
                .diaryId(this.diaryId)
                .contents(this.contents)
                .createdAt(this.createdAt)
                .build();
    }
}
