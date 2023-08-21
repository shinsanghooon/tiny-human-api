package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.common.domain.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryDetailService;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;
import com.tinyhuman.tinyhumanapi.diary.domain.SentenceCreate;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.PictureRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Diary", diaryId));

        Sentence sentence = sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Sentences", sentenceId));

        Sentence updatedSentence = sentence.update(newSentence);
        sentenceRepository.save(updatedSentence, diary);

        Diary updatedDiary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Diary", diaryId));

        return DiaryResponse.fromModel(updatedDiary);
    }

    @Override
    public Sentence deleteSentence(Long diaryId, Long sentenceId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Diary", diaryId));

        Sentence sentence = sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Sentences", sentenceId));

        Sentence deletedSentence = sentence.delete();
        return sentenceRepository.save(deletedSentence, diary);
    }

    @Override
    public void changeMainPicture(Long diaryId, Long currentMainPictureId, Long newMainPictureId) {

    }

    @Override
    public void deletePicture(Long diaryId, Long pictureId) {

    }
}
