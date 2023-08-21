package com.tinyhuman.tinyhumanapi.diary.infrastructure;

import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import com.tinyhuman.tinyhumanapi.diary.service.port.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Picture> findById(Long id) {
        return pictureJpaRepository.findById(id).map(PictureEntity::toModel);
    }

}
