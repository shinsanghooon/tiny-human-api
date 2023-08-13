package com.tinyhuman.tinyhumanapi.baby.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BabyJpaRepository extends JpaRepository<BabyEntity, Long> {

}
