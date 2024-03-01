package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CounselorLetterJpaRepository extends JpaRepository<CounselorLetterEntity, Long> {

    List<CounselorLetterEntity> findByBabyId(Long babyId);

}
