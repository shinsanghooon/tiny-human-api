package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.infrastructure.BabyEntity;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryImpl implements DiaryRepository {

    private final DiaryJpaRepository diaryJpaRepository;

    @Override
    public Diary save(Diary diary) {
        return diaryJpaRepository.save(DiaryEntity.fromModel(diary)).toModel();
    }

    @Override
    public Optional<Diary> findById(Long id) {
        return diaryJpaRepository.findById(id)
                .map(DiaryEntity::toModel);
    }

    @Override
    public List<Diary> findByBaby(Baby baby) {
        return mapToModels(diaryJpaRepository.findByBaby(BabyEntity.fromModel(baby)));
    }

    @Override
    public List<Diary> findByBabyId(Long babyId) {
        return mapToModels(diaryJpaRepository.findByBabyId(babyId));

    }

    private List<Diary> mapToModels(List<DiaryEntity> diaryEntities) {
        return diaryEntities.stream()
                .map(DiaryEntity::toModel)
                .toList();
    }
}
