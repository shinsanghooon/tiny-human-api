package com.tinyhuman.tinyhumanapi.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPushTokenJpaRepository extends JpaRepository<UserPushTokenEntity, Long> {

    List<UserPushTokenEntity> findByUserId(Long userId);

    Optional<UserPushTokenEntity> findByUserIdAndAndDeviceInfo(Long userId, String deviceInfo);

    List<UserPushTokenEntity> findByUserIdIn(List<Long> userIds);

}
