package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryImpl implements DiaryRepository {

    private final DiaryJpaRepository diaryJpaRepository;

    @Override
    public Diary save(Diary diary) {
        return diaryJpaRepository.save(DiaryEntity.fromModel(diary)).toModel();
    }

}
