package com.tinyhuman.tinyhumanapi.checklist.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistJpaRepository extends JpaRepository<ChecklistEntity, Long> {

    List<ChecklistEntity> findByUserId(Long userId);
}
