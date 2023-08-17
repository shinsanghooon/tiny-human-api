package com.tinyhuman.tinyhumanapi.diary.service.port;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;

import java.util.List;

public interface DiaryRepository {

    Diary save(Diary diary);

    List<Diary> findByBaby(Baby baby);

}
