package com.tinyhuman.tinyhumanapi.helpchat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HelpChatJpaRepository extends JpaRepository<HelpRequestEntity, Long> {

    List<HelpRequestEntity> findByUserId(Long userId);

    Optional<HelpRequestEntity> findByIdAndUserId(Long id, Long userID);
}
