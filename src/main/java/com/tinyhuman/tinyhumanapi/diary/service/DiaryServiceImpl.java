package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import com.tinyhuman.tinyhumanapi.common.exception.NotSupportedContentTypeException;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.common.service.port.UuidHolder;
import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import com.tinyhuman.tinyhumanapi.common.utils.PageCursor;
import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.*;
import com.tinyhuman.tinyhumanapi.diary.domain.CounselorLetter;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;
import com.tinyhuman.tinyhumanapi.diary.service.port.CounselorLetterRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.PictureRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.SentenceRepository;
import com.tinyhuman.tinyhumanapi.integration.service.port.ImageService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.infrastructure.UserBabyMappingId;
import com.tinyhuman.tinyhumanapi.user.service.port.UserBabyRelationRepository;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tinyhuman.tinyhumanapi.common.utils.FileUtils.*;

@Service
@Transactional
@Slf4j
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    private final SentenceRepository sentenceRepository;

    private final PictureRepository pictureRepository;

    private final BabyRepository babyRepository;

    private final ImageService imageService;

    private final UserRepository userRepository;

    private final UserBabyRelationRepository userBabyRelationRepository;

    private final CounselorLetterRepository counselorLetterJpaRepository;

    private final AuthService authService;

    private final UuidHolder uuidHolder;

    @Builder
    public DiaryServiceImpl(DiaryRepository diaryRepository, SentenceRepository sentenceRepository, PictureRepository pictureRepository,
                            BabyRepository babyRepository, UserRepository userRepository, ImageService imageService,
                            UserBabyRelationRepository userBabyRelationRepository, CounselorLetterRepository counselorLetterJpaRepository, AuthService authService, UuidHolder uuidHolder) {
        this.diaryRepository = diaryRepository;
        this.sentenceRepository = sentenceRepository;
        this.pictureRepository = pictureRepository;
        this.babyRepository = babyRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.userBabyRelationRepository = userBabyRelationRepository;
        this.counselorLetterJpaRepository = counselorLetterJpaRepository;
        this.authService = authService;
        this.uuidHolder = uuidHolder;
    }

    private final String DIARY_IMAGE_UPLOAD_PATH = "baby/babyId/diary/diaryId/";

    @Transactional
    public DiaryPreSignedUrlResponse create(DiaryCreate diaryCreate) {

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
        return DiaryPreSignedUrlResponse.fromModel(savedDiary);
    }

    private List<Picture> registerPictures(List<PictureCreate> files, Diary savedDiary) {
        List<Picture> pictures = createPictureList(files, savedDiary);

        Map<String, KeyMappingPreSignedUrl> keyMappingPreSignedUrls = new HashMap<>();
        for (Picture picture : pictures) {
            keyMappingPreSignedUrls.put(picture.keyName(), new KeyMappingPreSignedUrl(picture.keyName(), picture.preSignedUrl(), picture.fileName()));
        }

        List<Picture> savedPictures = pictureRepository.saveAll(pictures, savedDiary);

        return savedPictures.stream()
                .map(p -> {
                    KeyMappingPreSignedUrl keyMappingPreSignedUrl = keyMappingPreSignedUrls.get(p.keyName());
                    String preSignedUrl = keyMappingPreSignedUrl.preSignedUrl;
                    String fileName = keyMappingPreSignedUrl.fileName;
                    return p.addPreSignedUrl(preSignedUrl).addFileName(fileName);
                })
                .toList();
    }

    @Override
    public Diary delete(Long id) {
        Diary diary = findDiaryById(id);

        Diary deletedDiary = diary.delete();
        return diaryRepository.save(deletedDiary);
    }

    @Override
    public DiaryResponse findById(Long id) {
        Diary diary = findDiaryById(id);
        return DiaryResponse.fromModel(diary);
    }

    private Diary findDiaryById(Long id) {
        return diaryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(Diary) - diaryId:{}", id);
                    return new ResourceNotFoundException("Diary", id);
                });
    }


    @Override
    public List<DiaryResponse> findByDate(Long babyId, String date) {
        User user = authService.getUserOutOfSecurityContextHolder();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        return diaryRepository.findByDate(localDate, user.id(), babyId).stream()
                .map(DiaryResponse::fromModel)
                .toList();
    }

    @Override
    public PageCursor<DiaryResponse> getMyDiariesByBaby(Long babyId, CursorRequest cursorRequest) {
        Long userId = authService.getUserOutOfSecurityContextHolder().id();

        UserBabyRelation userBabyRelation = userBabyRelationRepository.findById(new UserBabyMappingId(userId, babyId))
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(UserBabyRelation) - userId:{},babyId{}",  userId, babyId);
                    return new ResourceNotFoundException("UserBabyRelation", userId + " " + babyId);
                });

        if (!userBabyRelation.hasReadRole()) {
            log.error("UnauthorizedAccessException(Baby) - userId:{},babyId{}",  userId, babyId);
            throw new UnauthorizedAccessException("Baby", babyId);
        }

        List<DiaryResponse> diaryResponses = diaryRepository.findByBabyId(babyId, cursorRequest).stream()
                .map(DiaryResponse::fromModel)
                .toList();

        List<CounselorLetter> counselorLetters = counselorLetterJpaRepository.findByBabyId(babyId);

        // DiaryResponse 리스트 업데이트
        List<DiaryResponse> diaryResponsesWithLetter = diaryResponses.stream()
                .map(diaryResponse -> {
                    // CounselorLetter 리스트에서 일치하는 diaryId 찾기
                    String letter = counselorLetters.stream()
                            .filter(counselorLetter -> counselorLetter.diaryId().equals(diaryResponse.id()))
                            .findFirst()
                            .map(CounselorLetter::contents)
                            .orElse(diaryResponse.letter()); // 일치하는 항목이 없으면 기존 값을 사용

                    // 새로운 DiaryResponse 인스턴스 생성
                    return diaryResponse.addLetter(letter);
                })
                .toList();

        // 결과 출력
        diaryResponsesWithLetter.forEach(diaryResponse -> {
            System.out.println("Diary ID: " + diaryResponse.id() + ", Letter: " + diaryResponse.letter());
        });

        long nextKey = getNextKey(diaryResponses);
        return new PageCursor<>(cursorRequest.next(nextKey), diaryResponsesWithLetter);
    }

    private static long getNextKey(List<DiaryResponse> diaryResponses) {
        return diaryResponses.stream()
                .mapToLong(DiaryResponse::id)
                .min()
                .orElse(CursorRequest.NONE_KEY);
    }

    private List<Picture> createPictureList(List<PictureCreate> files, Diary savedDiary) {
        List<Picture> pictures = new ArrayList<>();
        Long babyId = savedDiary.baby().id();

        boolean isMainPicture = true;
        for (PictureCreate pictureCreate : files) {
            String fileName = pictureCreate.fileName();
            FileInfo fileInfo = getFileInfo(fileName, uuidHolder.random());

            String mimeType = fileInfo.mimeType();
            if (isNotImage(mimeType)) {
                log.error("NotSupportedContentTypeException - MimeType:{}", mimeType);
                throw new NotSupportedContentTypeException(mimeType);
            }

            String keyName = addBabyIdAndAlbumIdToImagePath(DIARY_IMAGE_UPLOAD_PATH, babyId, savedDiary.id(), fileInfo.fileNameWithEpochTime());
            String preSignedUrl = imageService.getPreSignedUrlForUpload(keyName, fileInfo.mimeType());

            Picture picture = Picture.builder()
                    .isMainPicture(isMainPicture)
                    .contentType(fileInfo.contentType())
                    .fileName(fileName)
                    .keyName(keyName)
                    .preSignedUrl(preSignedUrl)
                    .diaryId(savedDiary.id())
                    .build();

            isMainPicture = false;

            pictures.add(picture);
        }

        return pictures;
    }

    private static boolean isNotImage(String mimeType) {
        return !mimeType.startsWith("image");
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
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - userId:{}", diaryCreate.userId());
                    return new ResourceNotFoundException("User", diaryCreate.userId());
                });
    }

    private Baby getBaby(DiaryCreate diaryCreate) {
        return babyRepository.findById(diaryCreate.babyId())
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(Baby) - babyId:{}", diaryCreate.babyId());
                    return new ResourceNotFoundException("Baby", diaryCreate.babyId());
                });
    }

    private record KeyMappingPreSignedUrl(String keyName, String preSignedUrl, String fileName) {
    }
}
