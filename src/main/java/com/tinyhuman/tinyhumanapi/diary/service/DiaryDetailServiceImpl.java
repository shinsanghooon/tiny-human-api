package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryDetailService;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryResponse;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.SentenceCreate;
import com.tinyhuman.tinyhumanapi.diary.domain.*;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.PictureRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@Slf4j
public class DiaryDetailServiceImpl implements DiaryDetailService {

    private final DiaryRepository diaryRepository;

    private final SentenceRepository sentenceRepository;

    private final PictureRepository pictureRepository;

    @Builder
    public DiaryDetailServiceImpl(DiaryRepository diaryRepository, SentenceRepository sentenceRepository, PictureRepository pictureRepository) {
        this.diaryRepository = diaryRepository;
        this.sentenceRepository = sentenceRepository;
        this.pictureRepository = pictureRepository;
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

        Picture deletedPicture = picture.delete();
        return pictureRepository.save(deletedPicture, diary);

    }

    private Picture findPictureById(Long pictureId) {
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
}
