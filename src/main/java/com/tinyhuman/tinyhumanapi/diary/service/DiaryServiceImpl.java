package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import com.tinyhuman.tinyhumanapi.common.domain.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.domain.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.domain.*;
import com.tinyhuman.tinyhumanapi.diary.enums.ContentType;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.PictureRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;
import com.tinyhuman.tinyhumanapi.integration.service.ImageService;
import com.tinyhuman.tinyhumanapi.integration.util.ImageUtil;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.infrastructure.UserBabyMappingId;
import com.tinyhuman.tinyhumanapi.user.service.port.UserBabyRelationRepository;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    private final SentenceRepository sentenceRepository;

    private final PictureRepository pictureRepository;

    private final BabyRepository babyRepository;

    private final ImageService imageService;
    
    private final UserRepository userRepository;

    private final UserBabyRelationRepository userBabyRelationRepository;

    @Builder
    public DiaryServiceImpl(DiaryRepository diaryRepository, SentenceRepository sentenceRepository, PictureRepository pictureRepository, BabyRepository babyRepository, UserRepository userRepository, ImageService imageService, UserBabyRelationRepository userBabyRelationRepository) {
        this.diaryRepository = diaryRepository;
        this.sentenceRepository = sentenceRepository;
        this.pictureRepository = pictureRepository;
        this.babyRepository = babyRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.userBabyRelationRepository = userBabyRelationRepository;
    }

    @Value("${aws.s3.path.diary}")
    private String s3UploadPath;

    @Transactional
    public DiaryResponse create(DiaryCreate diaryCreate, List<MultipartFile> files) {

        Baby baby = getBaby(diaryCreate);
        User user = getUser(diaryCreate);

        Diary savedDiary = registerDiary(diaryCreate, baby, user);

        List<Sentence> savedSentences = registerSentenceToDiary(diaryCreate, savedDiary);
        savedDiary = savedDiary.addSentences(savedSentences);

        if (files != null) {
            List<Picture> savedPicture = registerPictures(files, savedDiary);
            savedDiary = savedDiary.addPictures(savedPicture);
        }

        diaryRepository.save(savedDiary);

        return DiaryResponse.fromModel(savedDiary);
    }

    private List<Picture> registerPictures(List<MultipartFile> files, Diary savedDiary) {
        List<Picture> pictures = createPictureList(files, savedDiary);
        List<Picture> savedPicture = pictureRepository.saveAll(pictures, savedDiary);
        return savedPicture;
    }

    private User getUser(DiaryCreate diaryCreate) {
        return userRepository.findById(diaryCreate.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", diaryCreate.userId()));
    }

    private Baby getBaby(DiaryCreate diaryCreate) {
        return babyRepository.findById(diaryCreate.babyId())
                .orElseThrow(() -> new ResourceNotFoundException("Babies", diaryCreate.babyId()));
    }

    @Transactional
    @Override
    public Diary delete(Long id) {
        Diary diary = diaryRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Diary", id));

        Diary deletedDiary = diary.delete();
        return diaryRepository.save(deletedDiary);
    }

    @Override
    public DiaryResponse findById(Long id) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diary", id));

        return DiaryResponse.fromModel(diary);
    }

    @Override
    public List<DiaryResponse> getMyDiariesByBaby(Long babyId) {
        // TODO
        Long userId = 1L;

        UserBabyRelation userBabyRelation = userBabyRelationRepository.findById(new UserBabyMappingId(userId, babyId))
                .orElseThrow(() -> new ResourceNotFoundException("UserBabyRelation", userId + "-" + babyId));

        if (!userBabyRelation.hasReadRole()) {
            throw new UnauthorizedAccessException("Baby", babyId);
        }

        List<Diary> babyDiaries = diaryRepository.findByBabyId(babyId);
        return babyDiaries.stream()
                .map(DiaryResponse::fromModel)
                .toList();
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
                    .diaryId(savedDiary.id())
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
                        .diaryId(savedDiary.id())
                        .build())
                .toList();

        return sentenceRepository.saveAll(sentenceModels, savedDiary);
    }

    private Diary registerDiary(DiaryCreate diaryCreate, Baby baby, User user) {
        Diary newDiary = Diary.fromCreate(diaryCreate, baby, user);
        return diaryRepository.save(newDiary);
    }
}
