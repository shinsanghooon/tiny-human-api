package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SentenceJpaRepository extends JpaRepository<SentenceEntity, Long> {

}
