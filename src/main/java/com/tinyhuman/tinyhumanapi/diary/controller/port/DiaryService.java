package com.tinyhuman.tinyhumanapi.diary.controller.port;

import com.tinyhuman.tinyhumanapi.diary.domain.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;

public interface DiaryService {

    DiaryResponse create(DiaryCreate diaryCreate);
}
