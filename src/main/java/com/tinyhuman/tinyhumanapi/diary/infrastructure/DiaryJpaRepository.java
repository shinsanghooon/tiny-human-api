package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryJpaRepository extends JpaRepository<DiaryEntity, Long> {
}
