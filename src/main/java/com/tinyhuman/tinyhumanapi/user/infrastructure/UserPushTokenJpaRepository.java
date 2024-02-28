package com.tinyhuman.tinyhumanapi.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPushTokenJpaRepository extends JpaRepository<UserPushTokenEntity, Long> {

    List<UserPushTokenEntity> findByUserId(Long userId);


}
