package com.tinyhuman.tinyhumanapi.album.infrastructure;

import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

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

    @Column(name = "original_created_at")
    private LocalDateTime originalCreatedAt;

    @Column(name = "gps_lat")
    private Double gpsLat;

    @Column(name = "gps_lon")
    private Double gpsLon;

    @Column(name="is_deleted")
    private boolean isDeleted = false;

    @Builder
    public AlbumEntity(Long id, Long babyId, ContentType contentType, String keyName, LocalDateTime originalCreatedAt, Double gpsLat, Double gpsLon, boolean isDeleted) {
        this.id = id;
        this.babyId = babyId;
        this.contentType = contentType;
        this.keyName = keyName;
        this.originalCreatedAt = originalCreatedAt;
        this.gpsLat = gpsLat;
        this.gpsLon = gpsLon;
        this.isDeleted = isDeleted;
    }

    public static AlbumEntity fromModel(Album album) {
        return AlbumEntity.builder()
                .id(album.id())
                .babyId(album.babyId())
                .contentType(album.contentType())
                .keyName(album.keyName())
                .originalCreatedAt(album.originalCreatedAt())
                .gpsLat(album.gpsLat())
                .gpsLon(album.gpsLon())
                .isDeleted(album.isDeleted())
                .build();
    }

    public Album toModel() {
        return Album.builder()
                .id(this.id)
                .babyId(this.babyId)
                .contentType(this.contentType)
                .keyName(this.keyName)
                .createdAt(this.getCreatedAt())
                .originalCreatedAt(this.originalCreatedAt)
                .gpsLat(this.gpsLat)
                .gpsLon(this.gpsLon)
                .isDeleted(this.isDeleted)
                .build();
    }
}
