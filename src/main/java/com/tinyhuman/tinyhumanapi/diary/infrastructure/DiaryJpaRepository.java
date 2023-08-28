package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.baby.infrastructure.BabyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryJpaRepository extends JpaRepository<DiaryEntity, Long> {
    List<DiaryEntity> findByBaby(BabyEntity baby);

    List<DiaryEntity> findByBabyId(Long babyId);

    List<DiaryEntity> findByIdAndUserId(Long diaryId, Long userId);
}
