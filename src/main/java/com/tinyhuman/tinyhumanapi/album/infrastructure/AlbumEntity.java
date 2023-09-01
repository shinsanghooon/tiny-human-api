package com.tinyhuman.tinyhumanapi.album.infrastructure;

import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Table(name = "albums")
@Where(clause = "is_deleted=false")
@NoArgsConstructor
public class AlbumEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "baby_id")
    private Long babyId;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "key_name")
    private String keyName;

    @Column(name="is_deleted")
    private boolean isDeleted = false;

    @Builder
    public AlbumEntity(Long id, Long babyId, ContentType contentType, String keyName, boolean isDeleted) {
        this.id = id;
        this.babyId = babyId;
        this.contentType = contentType;
        this.keyName = keyName;
        this.isDeleted = isDeleted;
    }

    public static AlbumEntity fromModel(Album album) {
        return AlbumEntity.builder()
                .id(album.id())
                .babyId(album.babyId())
                .contentType(album.contentType())
                .keyName(album.keyName())
                .isDeleted(album.isDeleted())
                .build();
    }

    public Album toModel() {
        return Album.builder()
                .id(this.id)
                .babyId(this.babyId)
                .contentType(this.contentType)
                .keyName(this.keyName)
                .isDeleted(this.isDeleted)
                .build();
    }
}
