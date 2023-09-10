package com.tinyhuman.tinyhumanapi.diary.service.port;

import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository {

    Diary save(Diary diary);

    Optional<Diary> findById(Long id);

    List<Diary> findByDate(LocalDate date, Long userId, Long babyId);

    List<Diary> findByBabyId(Long babyId);

    List<Diary> findByBabyId(Long babyId, CursorRequest cursorRequest);

}
