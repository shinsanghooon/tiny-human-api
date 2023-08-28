package com.tinyhuman.tinyhumanapi.diary.service.port;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository {

    Diary save(Diary diary);

    Optional<Diary> findById(Long id);

    List<Diary> findByBaby(Baby baby);

    List<Diary> findByBabyId(Long babyId);

    List<Diary> findByIdAndUserId(Long diaryId, Long userId);

}
