package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.common.domain.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryDetailService;
import com.tinyhuman.tinyhumanapi.diary.domain.*;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.PictureRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
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

        Sentence sentence = sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Sentences", sentenceId));

        Sentence updatedSentence = sentence.update(newSentence);
        sentenceRepository.save(updatedSentence, diary);

        Diary updatedDiary = getDiary(diaryId);

        return DiaryResponse.fromModel(updatedDiary);
    }

    @Override
    public Sentence deleteSentence(Long diaryId, Long sentenceId) {
        Diary diary = getDiary(diaryId);

        Sentence sentence = sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Sentences", sentenceId));

        Sentence deletedSentence = sentence.delete();
        return sentenceRepository.save(deletedSentence, diary);
    }


    @Override
    public List<Picture> changeMainPicture(Long diaryId, Long currentMainPictureId, Long newMainPictureId) {
        Diary diary = getDiary(diaryId);
        Picture currentMainPicture = pictureRepository.findById(currentMainPictureId)
                .orElseThrow(() -> new ResourceNotFoundException("Picture", currentMainPictureId));

        Picture newMainPicture = pictureRepository.findById(newMainPictureId)
                .orElseThrow(() -> new ResourceNotFoundException("Picture", currentMainPictureId));

        Picture mainToNormalPicture = currentMainPicture.assignToNormal();
        Picture normalToMainPicture = newMainPicture.assignToMain();

        return pictureRepository.saveAll(List.of(mainToNormalPicture, normalToMainPicture), diary);
    }

    @Override
    public void deletePicture(Long diaryId, Long pictureId) {

    }


    private Diary getDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Diary", diaryId));
        return diary;
    }
}
