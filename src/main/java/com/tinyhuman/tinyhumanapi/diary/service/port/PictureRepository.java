package com.tinyhuman.tinyhumanapi.diary.service.port;

import com.tinyhuman.tinyhumanapi.diary.domain.Picture;

import java.util.List;


public interface PictureRepository {

    List<Picture> saveAll(List<Picture> pictures);
}
