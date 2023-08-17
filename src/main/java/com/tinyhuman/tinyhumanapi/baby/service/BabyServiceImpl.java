package com.tinyhuman.tinyhumanapi.baby.service;

import com.tinyhuman.tinyhumanapi.baby.controller.port.BabyService;
import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import com.tinyhuman.tinyhumanapi.common.domain.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.integration.service.ImageService;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserBabyRelationService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class BabyServiceImpl implements BabyService {

    private final BabyRepository babyRepository;

    private final ImageService imageService;

    private final DiaryRepository diaryRepository;

    private final UserRepository userRepository;

    private final UserBabyRelationService userBabyRelationService;

    @Builder
    public BabyServiceImpl(BabyRepository babyRepository, ImageService imageService, DiaryRepository diaryRepository,
                           UserRepository userRepository, UserBabyRelationService userBabyRelationService) {
        this.babyRepository = babyRepository;
        this.imageService = imageService;
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
        this.userBabyRelationService = userBabyRelationService;
    }

    @Value("${aws.s3.path.profile}")
    private String s3UploadPath;

    @Override
    public BabyResponse register(BabyCreate babyCreate, MultipartFile file) {

        // TODO: Login 구현 후 작업
        Long userId = 1L;
        User user = findUser(userId);

        String s3FullPath = imageService.sendImage(file, s3UploadPath);
        Baby baby = babyRepository.save(Baby.fromCreate(babyCreate, s3FullPath));

        userBabyRelationService.establishRelationUserToBaby(babyCreate, user, baby);

        return BabyResponse.fromModel(baby);
    }

    @Override
    public BabyResponse findById(Long id) {
        Baby baby = babyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Baby", id));
        return BabyResponse.fromModel(baby);
    }

    @Override
    public List<BabyResponse> getMyBabies() {
        // TODO: Login 구현 후 작업
        Long userId = 1L;

        User user = findUser(userId);
        List<UserBabyRelation> myBabies = userBabyRelationService.findByUser(user);

        return myBabies.stream()
                .map(UserBabyRelation::baby)
                .map(BabyResponse::fromModel)
                .toList();

    }

    @Override
    public void delete(Long id) {
        Baby baby = babyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Baby", id));
        Baby deletedBaby = baby.delete();
        babyRepository.save(deletedBaby);

        List<Diary> diaries = diaryRepository.findByBaby(baby);

        diaries.forEach(diary -> {
            Diary deletedDiary = diary.delete();
            diaryRepository.save(deletedDiary);
        });
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

}
