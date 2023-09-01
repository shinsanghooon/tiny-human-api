package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "pictures")
@NoArgsConstructor
public class PictureEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_main_picture")
    private boolean isMainPicture;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "key_name")
    private String keyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private DiaryEntity diary;

    @Builder
    public PictureEntity(Long id, boolean isMainPicture, ContentType contentType, String keyName, DiaryEntity diary) {
        this.id = id;
        this.isMainPicture = isMainPicture;
        this.contentType = contentType;
        this.keyName = keyName;
        this.diary = setDiary(diary);
    }

    private DiaryEntity setDiary(DiaryEntity diary) {
        if (this.diary != null) {
            this.diary.getPictures().remove(this);
        }
        this.diary = diary;
        diary.addPicture(this);

        return diary;
    }

    public static PictureEntity fromModel(Picture picture, Diary diary) {
        return PictureEntity.builder()
                .id(picture.id())
                .contentType(picture.contentType())
                .isMainPicture(picture.isMainPicture())
                .keyName(picture.keyName())
                .diary(DiaryEntity.fromModel(diary))
                .build();
    }

    public Picture toModel() {
        return Picture.builder()
                .id(this.id)
                .isMainPicture(this.isMainPicture)
                .contentType(this.contentType)
                .keyName(this.keyName)
                .diaryId(this.diary.getId())
                .build();
    }
}
