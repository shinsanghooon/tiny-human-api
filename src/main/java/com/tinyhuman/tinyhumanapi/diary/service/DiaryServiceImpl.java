package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import com.tinyhuman.tinyhumanapi.common.domain.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.domain.*;
import com.tinyhuman.tinyhumanapi.diary.enums.ContentType;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.PictureRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;
import com.tinyhuman.tinyhumanapi.integration.service.ImageService;
import com.tinyhuman.tinyhumanapi.integration.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    private final SentenceRepository sentenceRepository;

    private final PictureRepository pictureRepository;

    private final BabyRepository babyRepository;

    private final ImageService imageService;

    @Value("${aws.s3.path.diary}")
    private String s3UploadPath;

    @Transactional
    public DiaryResponse create(DiaryCreate diaryCreate, List<MultipartFile> files) {

        Long babyId = diaryCreate.babyId();
        Baby baby = babyRepository.findById(babyId).orElseThrow(() -> new ResourceNotFoundException("Babies", babyId));

        Diary savedDiary = registerDiary(diaryCreate, baby);
        List<Sentence> savedSentences = registerSentenceToDiary(diaryCreate, savedDiary);

        List<Picture> pictures = createPictureList(files, savedDiary);
        List<Picture> savedPicture = pictureRepository.saveAll(pictures);

        return DiaryResponse.fromModel(savedDiary, savedSentences, savedPicture);
    }


    private List<Picture> createPictureList(List<MultipartFile> files, Diary savedDiary) {
        List<Picture> pictures = new ArrayList<>();

        boolean isMainPicture = true;
        for (MultipartFile file : files) {

            String s3Url = imageService.sendImage(file, s3UploadPath);

            ContentType contentType = ImageUtil.getContentType(file);

            Picture picture = Picture.builder()
                    .isMainPicture(isMainPicture)
                    .contentType(contentType)
                    .originalS3Url(s3Url)
                    .diary(savedDiary)
                    .build();
            isMainPicture = false;

            pictures.add(picture);
        }
        return pictures;
    }

    private List<Sentence> registerSentenceToDiary(DiaryCreate diaryCreate, Diary savedDiary) {
        List<SentenceCreate> sentences = Sentence.from(diaryCreate);
        List<Sentence> sentenceModels = sentences.stream()
                .map(s -> Sentence.builder()
                        .sentence(s.sentence())
                        .diary(savedDiary)
                        .build())
                .toList();

        return sentenceRepository.saveAll(sentenceModels);
    }

    private Diary registerDiary(DiaryCreate diaryCreate, Baby baby) {
        Diary newDiary = Diary.from(diaryCreate, baby);
        return diaryRepository.save(newDiary);
    }
}
