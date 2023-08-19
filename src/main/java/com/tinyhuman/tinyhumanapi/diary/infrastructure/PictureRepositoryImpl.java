package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import com.tinyhuman.tinyhumanapi.diary.service.port.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PictureRepositoryImpl implements PictureRepository {

    private final PictureJpaRepository pictureJpaRepository;

    @Override
    public List<Picture> saveAll(List<Picture> pictures, Diary savedDiary) {
        List<PictureEntity> pictureEntities = pictureJpaRepository.saveAll(
                pictures.stream()
                        .map(p -> PictureEntity.fromModel(p, savedDiary))
                        .toList());

        return pictureEntities.stream()
                .map(PictureEntity::toModel)
                .toList();
    }

}
