package com.tinyhuman.tinyhumanapi.baby.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.baby.controller.port.BabyService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyUpdate;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.service.port.ClockHolder;
import com.tinyhuman.tinyhumanapi.common.utils.FileUtils;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.integration.service.port.ImageService;
import com.tinyhuman.tinyhumanapi.common.utils.ImageUtils;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserBabyRelationService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BabyServiceImpl implements BabyService {

    private final BabyRepository babyRepository;

    private final ImageService imageService;

    private final DiaryRepository diaryRepository;

    private final UserRepository userRepository;

    private final UserBabyRelationService userBabyRelationService;

    private final AuthService authService;

    private final ClockHolder clockHolder;

    @Builder
    public BabyServiceImpl(BabyRepository babyRepository, ImageService imageService, DiaryRepository diaryRepository,
                           UserRepository userRepository, UserBabyRelationService userBabyRelationService, AuthService authService, ClockHolder clockHolder) {
        this.babyRepository = babyRepository;
        this.imageService = imageService;
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
        this.userBabyRelationService = userBabyRelationService;
        this.authService = authService;
        this.clockHolder = clockHolder;
    }

    private final String BABY_PROFILE_UPLOAD_PATH = "baby/babyId/profile/";

    @Override
    public BabyResponse register(BabyCreate babyCreate) {

        User user = authService.getUserOutOfSecurityContextHolder();

        String fileName = babyCreate.fileName();
        String fileNameWithEpoch = FileUtils.generateFileNameWithEpochTime(fileName, clockHolder);

        String mimeType = ImageUtils.guessMimeType(fileName);
        String keyName = FileUtils.addBabyIdToImagePath(BABY_PROFILE_UPLOAD_PATH, user.id(), fileNameWithEpoch);

        Baby baby = babyRepository.save(Baby.fromCreate(babyCreate, keyName));
        userBabyRelationService.establishRelationUserToBaby(babyCreate, user, baby);

        String preSignedUrl = imageService.getPreSignedUrlForUpload(keyName, mimeType);

        return BabyResponse.fromModel(baby, preSignedUrl);
    }

    @Override
    public BabyResponse findById(Long id) {
        Baby baby = findBaby(id);
        String profileImgKeyName = baby.profileImgKeyName();
        String preSignedUrlForRead = imageService.getPreSignedUrlForRead(profileImgKeyName, 1000);
        return BabyResponse.fromModel(baby, preSignedUrlForRead);
    }

    @Override
    public List<BabyResponse> getMyBabies() {
        User user = authService.getUserOutOfSecurityContextHolder();
        List<UserBabyRelation> myBabies = userBabyRelationService.findByUser(user);

        return myBabies.stream()
                .map(UserBabyRelation::baby)
                .map(b -> {
                    String preSignedUrlForRead = imageService.getPreSignedUrlForRead(b.profileImgKeyName(), 1000);
                    return BabyResponse.fromModel(b, preSignedUrlForRead);
                })
                .toList();
    }

    @Override
    public void delete(Long id) {
        Baby baby = findBaby(id);
        Baby deletedBaby = baby.delete();
        babyRepository.save(deletedBaby);

        List<Diary> diaries = diaryRepository.findByBaby(baby);

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
        String preSignedUrlForRead = imageService.getPreSignedUrlForRead(savedBaby.profileImgKeyName(), 1000);

        return BabyResponse.fromModel(savedBaby, preSignedUrlForRead);
    }

    @Override
    public BabyResponse updateProfileImage(Long id, String fileName) {

        Baby baby = findBaby(id);
        User user = authService.getUserOutOfSecurityContextHolder();

        String fileNameWithEpoch = FileUtils.generateFileNameWithEpochTime(fileName, clockHolder);

        String mimeType = ImageUtils.guessMimeType(fileName);
        String keyName = FileUtils.addUserIdToImagePath(BABY_PROFILE_UPLOAD_PATH, user.id(), fileNameWithEpoch);

        String preSignedUrl = imageService.getPreSignedUrlForUpload(keyName, mimeType);

        Baby updatedBaby = baby.updateOnlyImage(keyName);
        Baby savedBaby = babyRepository.save(updatedBaby);

        return BabyResponse.fromModel(savedBaby, preSignedUrl);

    }


    private Baby findBaby(Long id) {
        return babyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Baby", id));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

}
