package com.tinyhuman.tinyhumanapi.user.infrastructure;

import com.tinyhuman.tinyhumanapi.common.infrastructure.BaseEntity;
import com.tinyhuman.tinyhumanapi.user.domain.UserPushToken;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_push_tokens")
@NoArgsConstructor
public class UserPushTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "device_info")
    private String deviceInfo;

    @Builder
    public UserPushTokenEntity(Long id, Long userId, String fcmToken, String deviceInfo) {
        this.id = id;
        this.userId = userId;
        this.fcmToken = fcmToken;
        this.deviceInfo = deviceInfo;
    }

    public static UserPushTokenEntity fromModel(UserPushToken userPushToken) {
        return UserPushTokenEntity.builder()
                .id(userPushToken.id())
                .userId(userPushToken.userId())
                .fcmToken(userPushToken.fcmToken())
                .deviceInfo(userPushToken.deviceInfo())
                .build();
    }

    public UserPushToken toModel() {
        return UserPushToken.builder()
                .id(this.id)
                .userId(this.userId)
                .fcmToken(this.fcmToken)
                .deviceInfo(this.deviceInfo)
                .build();
    }
}
