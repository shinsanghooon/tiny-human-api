package com.tinyhuman.tinyhumanapi.diary.controller.port;

import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryPreSignedUrlResponse;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryResponse;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;

import java.util.List;

public interface DiaryService {

    DiaryPreSignedUrlResponse create(DiaryCreate diaryCreate);

    Diary delete(Long id);

    DiaryResponse findById(Long id);

    List<DiaryResponse> getMyDiariesByBaby(Long userId);
}
