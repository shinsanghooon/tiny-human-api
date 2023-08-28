package com.tinyhuman.tinyhumanapi.auth.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "refresh_token")
@NoArgsConstructor
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Builder
    public RefreshTokenEntity(Long id, Long userId, String refreshToken) {
        this.id = id;
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public static RefreshTokenEntity fromModel(RefreshToken refreshToken) {
        return RefreshTokenEntity.builder()
                .id(refreshToken.id())
                .userId(refreshToken.userId())
                .refreshToken(refreshToken.refreshToken())
                .build();
    }

    public RefreshToken toModel() {
        return RefreshToken.builder()
                .id(this.id)
                .userId(this.userId)
                .refreshToken((this.refreshToken))
                .build();
    }
}
