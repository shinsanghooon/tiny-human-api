package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.common.service.port.ClockHolder;
import com.tinyhuman.tinyhumanapi.common.utils.FileUtils;
import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.domain.*;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.PictureRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;
import com.tinyhuman.tinyhumanapi.integration.service.port.ImageService;
import com.tinyhuman.tinyhumanapi.common.utils.ImageUtils;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.infrastructure.UserBabyMappingId;
import com.tinyhuman.tinyhumanapi.user.service.port.UserBabyRelationRepository;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final AuthService authService;

    private final ClockHolder clockHolder;

    @Builder
    public DiaryServiceImpl(DiaryRepository diaryRepository, SentenceRepository sentenceRepository, PictureRepository pictureRepository,
                            BabyRepository babyRepository, UserRepository userRepository, ImageService imageService, UserBabyRelationRepository userBabyRelationRepository, AuthService authService, ClockHolder clockHolder) {
        this.diaryRepository = diaryRepository;
        this.sentenceRepository = sentenceRepository;
        this.pictureRepository = pictureRepository;
        this.babyRepository = babyRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.userBabyRelationRepository = userBabyRelationRepository;
        this.authService = authService;
        this.clockHolder = clockHolder;
    }

    private final String DIARY_IMAGE_UPLOAD_PATH = "baby/babyId/diary/diaryId/";

    @Transactional
    public DiaryResponse create(DiaryCreate diaryCreate) {

        Baby baby = getBaby(diaryCreate);
        User user = getUser(diaryCreate);

        Diary savedDiary = registerDiary(diaryCreate, baby, user);

        List<Sentence> savedSentences = registerSentenceToDiary(diaryCreate, savedDiary);
        savedDiary = savedDiary.addSentences(savedSentences);

        List<PictureCreate> files = diaryCreate.files();

        if (files != null) {
            List<Picture> savedPicture = registerPictures(files, savedDiary);
            savedDiary = savedDiary.addPictures(savedPicture);
        }

        diaryRepository.save(savedDiary);

        return DiaryResponse.fromModel(savedDiary);
    }

    private List<Picture> registerPictures(List<PictureCreate> files, Diary savedDiary) {
        List<Picture> pictures = createPictureList(files, savedDiary);

        Map<String, String> preSignedUrlMap = new HashMap<>();
        for (Picture picture : pictures) {
            preSignedUrlMap.put(picture.keyName(), picture.preSignedUrl());
        }

        List<Picture> savedPictures = pictureRepository.saveAll(pictures, savedDiary);

        return savedPictures.stream()
                .map(p -> {
                    String preSignedUrl = preSignedUrlMap.get(p.keyName());
                    return p.addPreSignedUrl(preSignedUrl);
                })
                .toList();
    }

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

        Diary diaryWithPreSignedUrl = addPreSignedUrlToDiary(diary);

        return DiaryResponse.fromModel(diaryWithPreSignedUrl);
    }

    private Diary addPreSignedUrlToDiary(Diary diary) {
        List<Picture> pictures = diary.pictures();
        List<Picture> picturesWithPreSignedUrl = pictures.stream().map(p -> {
            String preSignedUrl = imageService.getPreSignedUrlForRead(p.keyName(), 1000);
            return p.addPreSignedUrl(preSignedUrl);
        }).toList();

        return Diary.builder()
                .id(diary.id())
                .isDeleted(diary.isDeleted())
                .likeCount(diary.likeCount())
                .daysAfterBirth(diary.daysAfterBirth())
                .created_at(diary.created_at())
                .baby(diary.baby())
                .sentences(diary.sentences())
                .pictures(picturesWithPreSignedUrl)
                .user(diary.user())
                .build();
    }

    @Override
    public List<DiaryResponse> getMyDiariesByBaby(Long babyId) {
        User user = authService.getUserOutOfSecurityContextHolder();
        Long userId = user.id();

        UserBabyRelation userBabyRelation = userBabyRelationRepository.findById(new UserBabyMappingId(userId, babyId))
                .orElseThrow(() -> new ResourceNotFoundException("UserBabyRelation", userId + "-" + babyId));

        if (!userBabyRelation.hasReadRole()) {
            throw new UnauthorizedAccessException("Baby", babyId);
        }

        List<Diary> babyDiaries = diaryRepository.findByBabyId(babyId);

        List<Diary> diariesWithPreSignedUrl = babyDiaries.stream().map(diary -> {
            if (diary.pictures() == null || diary.pictures().size() == 0) {
                return diary;
            }
            return addPreSignedUrlToDiary(diary);
        }).toList();

        return diariesWithPreSignedUrl.stream()
                .map(DiaryResponse::fromModel)
                .toList();
    }

    private List<Picture> createPictureList(List<PictureCreate> files, Diary savedDiary) {
        List<Picture> pictures = new ArrayList<>();
        Long babyId = savedDiary.baby().id();

        boolean isMainPicture = true;
        for (PictureCreate pictureCreate : files) {

            String fileName = pictureCreate.fileName();
            String fileNameWithEpoch = FileUtils.generateFileNameWithEpochTime(fileName, clockHolder);
            String keyName = FileUtils.addBabyIdAndAlbumIdToImagePath(DIARY_IMAGE_UPLOAD_PATH, babyId, savedDiary.id(), fileNameWithEpoch);
            String mimeType = ImageUtils.guessMimeType(fileName);

            String preSignedUrl = imageService.getPreSignedUrlForUpload(keyName, mimeType);
            ContentType contentType = ImageUtils.getContentType(mimeType);

            Picture picture = Picture.builder()
                    .isMainPicture(isMainPicture)
                    .contentType(contentType)
                    .keyName(keyName)
                    .preSignedUrl(preSignedUrl)
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


    private User getUser(DiaryCreate diaryCreate) {
        return userRepository.findById(diaryCreate.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", diaryCreate.userId()));
    }

    private Baby getBaby(DiaryCreate diaryCreate) {
        return babyRepository.findById(diaryCreate.babyId())
                .orElseThrow(() -> new ResourceNotFoundException("Babies", diaryCreate.babyId()));
    }
}
