package com.tinyhuman.tinyhumanapi.baby.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyPreSignedUrlResponse;
import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.controller.dto.BabyUpdate;
import com.tinyhuman.tinyhumanapi.baby.controller.port.BabyService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.service.port.UuidHolder;
import com.tinyhuman.tinyhumanapi.common.utils.FileUtils;
import com.tinyhuman.tinyhumanapi.common.utils.FileUtils.FileInfo;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.integration.service.port.ImageService;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserBabyRelationService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class BabyServiceImpl implements BabyService {

    private final BabyRepository babyRepository;

    private final ImageService imageService;

    private final DiaryRepository diaryRepository;

    private final UserBabyRelationService userBabyRelationService;

    private final AuthService authService;

    private final UuidHolder uuidHolder;

    @Builder
    public BabyServiceImpl(BabyRepository babyRepository, ImageService imageService, DiaryRepository diaryRepository, UserBabyRelationService userBabyRelationService, AuthService authService, UuidHolder uuidHolder) {
        this.babyRepository = babyRepository;
        this.imageService = imageService;
        this.diaryRepository = diaryRepository;
        this.userBabyRelationService = userBabyRelationService;
        this.authService = authService;
        this.uuidHolder = uuidHolder;
    }

    private final String BABY_PROFILE_UPLOAD_PATH = "baby/babyId/profile/";

    @Override
    public BabyPreSignedUrlResponse register(BabyCreate babyCreate) {

        User user = authService.getUserOutOfSecurityContextHolder();

        FileInfo fileInfo = FileUtils.getFileInfo(babyCreate.fileName(), uuidHolder.random());

        Baby baby = babyRepository.save(Baby.fromCreate(babyCreate, user.id()));

        String keyName = FileUtils.addBabyIdToImagePath(BABY_PROFILE_UPLOAD_PATH, baby.id(), fileInfo.fileNameWithEpochTime());

        Baby babyWithKeyName = babyRepository.save(baby.with(keyName));
        userBabyRelationService.establishRelationUserToBaby(babyCreate, user, babyWithKeyName);

        String preSignedUrl = imageService.getPreSignedUrlForUpload(keyName, fileInfo.mimeType());

        return BabyPreSignedUrlResponse.fromModel(babyWithKeyName, preSignedUrl);
    }

    @Override
    public BabyResponse findById(Long id) {
        return BabyResponse.fromModel(findBaby(id));
    }

    @Override
    public List<BabyResponse> getMyBabies() {
        User user = authService.getUserOutOfSecurityContextHolder();
        List<UserBabyRelation> myBabies = userBabyRelationService.findByUser(user);

        return myBabies.stream()
                .map(UserBabyRelation::baby)
                .map(BabyResponse::fromModel)
                .toList();
    }

    @Override
    public void delete(Long id) {
        User user = authService.getUserOutOfSecurityContextHolder();
        Baby baby = findBaby(id);
        Baby deletedBaby = baby.delete();
        babyRepository.save(deletedBaby);

        UserBabyRelation deletedUserBabyRelation = userBabyRelationService.findByMappingId(user, baby).delete();
        userBabyRelationService.update(deletedUserBabyRelation);

        List<Diary> diaries = diaryRepository.findByBabyId(baby.id());

        diaries.forEach(diary -> {
            Diary deletedDiary = diary.delete();
            diaryRepository.save(deletedDiary);
        });
    }

    @Override
    public BabyResponse update(Long id, BabyUpdate babyUpdate) {

        Baby baby = findBaby(id);
        Baby updatedBaby = baby.update(babyUpdate);
        Baby savedBaby = babyRepository.save(updatedBaby);

        return BabyResponse.fromModel(savedBaby);
    }

    @Override
    public BabyPreSignedUrlResponse updateProfileImage(Long id, String fileName) {

        Baby baby = findBaby(id);
        User user = authService.getUserOutOfSecurityContextHolder();

        FileInfo fileInfo = FileUtils.getFileInfo(fileName, uuidHolder.random());
        String keyName = FileUtils.addUserIdToImagePath(BABY_PROFILE_UPLOAD_PATH, user.id(), fileInfo.fileNameWithEpochTime());

        String preSignedUrl = imageService.getPreSignedUrlForUpload(keyName, fileInfo.mimeType());

        Baby updatedBaby = baby.updateOnlyImage(keyName);
        Baby savedBaby = babyRepository.save(updatedBaby);

        return BabyPreSignedUrlResponse.fromModel(savedBaby, preSignedUrl);
    }

    private Baby findBaby(Long id) {
        User user = authService.getUserOutOfSecurityContextHolder();
        return babyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(Baby) - userId:{},babyId:{}", user.id(), id);
                    return new ResourceNotFoundException("Baby", id);
                });
    }
}
