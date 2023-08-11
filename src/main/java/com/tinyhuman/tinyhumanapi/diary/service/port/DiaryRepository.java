package com.tinyhuman.tinyhumanapi.diary.service.port;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;

public interface DiaryRepository {

    Diary save(Diary diary);
}
