package com.tinyhuman.tinyhumanapi.diary.controller.port;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryResponse;

import java.util.List;

public interface DiaryService {

    DiaryResponse create(DiaryCreate diaryCreate);

    Diary delete(Long id);

    DiaryResponse findById(Long id);

    List<DiaryResponse> getMyDiariesByBaby(Long userId);
}
