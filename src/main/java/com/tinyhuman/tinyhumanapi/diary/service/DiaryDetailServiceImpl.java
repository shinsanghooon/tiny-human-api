package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.common.exception.NotSupportedContentTypeException;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.service.port.UuidHolder;
import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryDetailService;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryPreSignedUrlResponse;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryResponse;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.PictureCreate;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.SentenceCreate;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.PictureRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;
import com.tinyhuman.tinyhumanapi.integration.service.port.ImageService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.tinyhuman.tinyhumanapi.common.utils.FileUtils.*;


@Service
@Transactional
@Slf4j
public class DiaryDetailServiceImpl implements DiaryDetailService {

    private final DiaryRepository diaryRepository;

    private final SentenceRepository sentenceRepository;

    private final PictureRepository pictureRepository;

    private final ImageService imageService;

    private final UuidHolder uuidHolder;

    private final String DIARY_IMAGE_UPLOAD_PATH = "baby/babyId/diary/diaryId/";

    @Builder
    public DiaryDetailServiceImpl(DiaryRepository diaryRepository, SentenceRepository sentenceRepository, PictureRepository pictureRepository, ImageService imageService, UuidHolder uuidHolder) {
        this.diaryRepository = diaryRepository;
        this.sentenceRepository = sentenceRepository;
        this.pictureRepository = pictureRepository;
        this.imageService = imageService;
        this.uuidHolder = uuidHolder;
    }

    @Override
    public DiaryResponse updateSentence(Long diaryId, Long sentenceId, SentenceCreate newSentence) {
        Diary diary = getDiary(diaryId);

        Sentence sentence = findSentenceById(sentenceId);

        Sentence updatedSentence = sentence.update(newSentence);
        sentenceRepository.save(updatedSentence, diary);

        Diary updatedDiary = getDiary(diaryId);

        return DiaryResponse.fromModel(updatedDiary);
    }

    private Sentence findSentenceById(Long sentenceId) {
        return sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(Sentence) - sentenceId:{}", sentenceId);
                    return new ResourceNotFoundException("Sentence", sentenceId);
                });
    }

    @Override
    public Sentence deleteSentence(Long diaryId, Long sentenceId) {
        Diary diary = getDiary(diaryId);

        Sentence sentence = findSentenceById(sentenceId);

        Sentence deletedSentence = sentence.delete();
        return sentenceRepository.save(deletedSentence, diary);
    }


    @Override
    public List<Picture> changeMainPicture(Long diaryId, Long currentMainPictureId, Long newMainPictureId) {
        Diary diary = getDiary(diaryId);
        Picture currentMainPicture = pictureRepository.findById(currentMainPictureId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(Picture) - pictureId:{}", currentMainPictureId);
                    return new ResourceNotFoundException("Picture", currentMainPictureId);
                });

        Picture newMainPicture = pictureRepository.findById(newMainPictureId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(Picture) - pictureId:{}", newMainPictureId);
                    return new ResourceNotFoundException("Picture", newMainPictureId);
                });

        Picture mainToNormalPicture = currentMainPicture.assignToNormal();
        Picture normalToMainPicture = newMainPicture.assignToMain();

        return pictureRepository.saveAll(List.of(mainToNormalPicture, normalToMainPicture), diary);
    }

    @Override
    public Picture deletePicture(Long diaryId, Long pictureId) {
        Diary diary = getDiary(diaryId);

        Picture picture = findPictureById(pictureId);

        if (diary.pictures().size() > 1) {
            if (picture.isMainPicture()) {
                Picture newMainPicture = diary.pictures().stream()
                        .filter(p -> !p.id().equals(pictureId))
                        .min(Comparator.comparingLong(Picture::id))
                        .orElseThrow(() -> {
                            log.error("IllegalStateException(Picture) - pictureId:{}", pictureId);
                            return new IllegalStateException("사진 삭제 작업 중 에러가 발생했습니다.");
                        })
                        .assignToMain();
                pictureRepository.save(newMainPicture, diary);
            }
        }

        Picture deletedPicture = picture.delete();
        return pictureRepository.save(deletedPicture, diary);

    }

    @Override
    public DiaryPreSignedUrlResponse addPictures(Long diaryId, List<PictureCreate> pictureCreates) {
        Diary diary = getDiary(diaryId);
        List<Picture> pictures = registerPictures(pictureCreates, diary);

        Map<String, KeyMappingPreSignedUrl> keyMappingPreSignedUrls = new HashMap<>();
        for (Picture picture : pictures) {
            keyMappingPreSignedUrls.put(picture.keyName(), new KeyMappingPreSignedUrl(picture.keyName(), picture.preSignedUrl(), picture.fileName()));
        }

        List<Picture> newPictures = pictureRepository.saveAll(pictures, diary).stream()
                .map(p -> {
                    KeyMappingPreSignedUrl keyMappingPreSignedUrl = keyMappingPreSignedUrls.get(p.keyName());
                    String preSignedUrl = keyMappingPreSignedUrl.preSignedUrl;
                    String fileName = keyMappingPreSignedUrl.fileName;
                    return p.addPreSignedUrl(preSignedUrl).addFileName(fileName);
                })
                .toList();

        Diary diaryWithNewPictures = diary.addPictureToNewPictures(newPictures);
        diaryRepository.save(diaryWithNewPictures);
        return DiaryPreSignedUrlResponse.fromModel(diaryWithNewPictures);
    }

    private List<Picture> registerPictures(List<PictureCreate> pictureCreates, Diary diary) {
        List<Picture> pictures = new ArrayList<>();
        Long babyId = diary.baby().id();

        for (PictureCreate pictureCreate : pictureCreates) {
            String fileName = pictureCreate.fileName();
            FileInfo fileInfo = getFileInfo(fileName, uuidHolder.random());

            String mimeType = fileInfo.mimeType();
            if (isNotImage(mimeType)) {
                log.error("NotSupportedContentTypeException - MimeType:{}", mimeType);
                throw new NotSupportedContentTypeException(mimeType);
            }

            String keyName = addBabyIdAndAlbumIdToImagePath(DIARY_IMAGE_UPLOAD_PATH, babyId, diary.id(), fileInfo.fileNameWithEpochTime());
            String preSignedUrl = imageService.getPreSignedUrlForUpload(keyName, fileInfo.mimeType());

            System.out.println("fileName = " + fileName);
            Picture picture = Picture.builder()
                    .isMainPicture(false)
                    .contentType(fileInfo.contentType())
                    .fileName(fileName)
                    .keyName(keyName)
                    .preSignedUrl(preSignedUrl)
                    .diaryId(diary.id())
                    .build();

            pictures.add(picture);
        }

        return pictures;
    }

    private static boolean isNotImage(String mimeType) {
        return !mimeType.startsWith("image");
    }

    public Picture findPictureById(Long pictureId) {
        return pictureRepository.findById(pictureId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(Picture) - pictureId:{}", pictureId);
                    return new ResourceNotFoundException("Picture", pictureId);
                });
    }

    private Diary getDiary(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(Diary) - diaryId:{}", diaryId);
                    return new ResourceNotFoundException("Diary", diaryId);
                });
    }

    private record KeyMappingPreSignedUrl(String keyName, String preSignedUrl, String fileName) {
    }
}

