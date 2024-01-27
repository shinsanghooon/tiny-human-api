package com.tinyhuman.tinyhumanapi.checklist.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChecklistJpaRepository extends JpaRepository<ChecklistEntity, Long> {

    List<ChecklistEntity> findByUserId(Long userId);

    Optional<ChecklistEntity> findByIdAndUserId(Long id, Long userID);
}
