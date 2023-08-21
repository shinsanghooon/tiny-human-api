package com.tinyhuman.tinyhumanapi.diary.service.port;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;

import java.util.List;
import java.util.Optional;


public interface PictureRepository {

    List<Picture> saveAll(List<Picture> pictures, Diary savedDiary);

    Optional<Picture> findById(Long id);
}
