package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.diary.domain.CounselorLetter;
import com.tinyhuman.tinyhumanapi.diary.service.port.CounselorLetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CounselorLetterRepositoryImpl implements CounselorLetterRepository {

    private final CounselorLetterJpaRepository counselorLetterJpaRepository;


    @Override
    public List<CounselorLetter> findByBabyId(Long babyId) {
        return mapToModels(counselorLetterJpaRepository.findByBabyId(babyId));
    }

    private List<CounselorLetter> mapToModels(List<CounselorLetterEntity> counselorLetterEntities) {
        return counselorLetterEntities.stream()
                .map(CounselorLetterEntity::toModel)
                .toList();
    }
}
