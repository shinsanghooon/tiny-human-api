package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
    public List<Diary> findByDate(LocalDate date, Long userId, Long babyId) {
        return mapToModels(diaryJpaRepository.findByDateAndUserIdAndBabyId(date, userId, babyId));
    }

    @Override
    public List<Diary> findByBabyId(Long babyId) {
        return mapToModels(diaryJpaRepository.findByBabyId(babyId));
    }

    @Override
    public List<Diary> findByBabyId(Long babyId, CursorRequest cursorRequest) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id"); // 정렬 정보 설정
        Pageable pageable = PageRequest.of(0, cursorRequest.size(), sort);

        if (cursorRequest.hasKey()) {
            return mapToModels(diaryJpaRepository.findByBabyIdAndIdLessThan(babyId, cursorRequest.key(), pageable));
        }
        return mapToModels(diaryJpaRepository.findByBabyId(babyId, pageable));
    }

    private List<Diary> mapToModels(List<DiaryEntity> diaryEntities) {
        return diaryEntities.stream()
                .map(DiaryEntity::toModel)
                .toList();
    }
}
