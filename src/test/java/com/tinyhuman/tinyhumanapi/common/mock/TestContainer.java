package com.tinyhuman.tinyhumanapi.common.mock;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.auth.mock.FakeAuthService;
import com.tinyhuman.tinyhumanapi.baby.controller.BabyController;
import com.tinyhuman.tinyhumanapi.baby.controller.port.BabyService;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeBabyRepository;
import com.tinyhuman.tinyhumanapi.baby.mock.FakeImageService;
import com.tinyhuman.tinyhumanapi.baby.service.BabyServiceImpl;
import com.tinyhuman.tinyhumanapi.baby.service.port.BabyRepository;
import com.tinyhuman.tinyhumanapi.common.service.port.UuidHolder;
import com.tinyhuman.tinyhumanapi.diary.mock.FakeDiaryRepository;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import com.tinyhuman.tinyhumanapi.integration.service.port.ImageService;
import com.tinyhuman.tinyhumanapi.user.controller.UserController;
import com.tinyhuman.tinyhumanapi.user.controller.UserCreateController;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserBabyRelationService;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserService;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserBabyRelationRepository;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserRepository;
import com.tinyhuman.tinyhumanapi.user.service.UserBabyRelationServiceImpl;
import com.tinyhuman.tinyhumanapi.user.service.UserServiceImpl;
import com.tinyhuman.tinyhumanapi.user.service.port.UserBabyRelationRepository;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestContainer {

    public final UserService userService;
    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final AuthService authService;
    public final UserController userController;
    public final UserCreateController userCreateController;

    public final BabyRepository babyRepository;
    public final ImageService imageService;
    public final DiaryRepository diaryRepository;
    public final UserBabyRelationService userBabyRelationService;
    public final UserBabyRelationRepository userBabyRelationRepository;
    public final BabyService babyService;
    public final BabyController babyController;

    @Builder
    public TestContainer(UuidHolder uuidHolder) {
        // for UserController
        this.userRepository = new FakeUserRepository();
        this.passwordEncoder = new TestPasswordEncoder();
        this.authService = new FakeAuthService();
        this.userService = UserServiceImpl.builder()
                .userRepository(this.userRepository)
                .passwordEncoder(this.passwordEncoder)
                .authService(this.authService)
                .build();
        this.userController = UserController.builder()
                .userService(this.userService)
                .build();
        this.userCreateController = UserCreateController.builder()
                .userService(userService)
                .build();

        // for BabyController
        this.babyRepository = new FakeBabyRepository();
        this.imageService = new FakeImageService();
        this.diaryRepository = new FakeDiaryRepository();
        this.userBabyRelationRepository = new FakeUserBabyRelationRepository();
        this.userBabyRelationService = UserBabyRelationServiceImpl.builder()
                .userBabyRelationRepository(userBabyRelationRepository)
                .build();
        this.babyService = BabyServiceImpl.builder()
                .babyRepository(this.babyRepository)
                .userBabyRelationService(this.userBabyRelationService)
                .authService(this.authService)
                .imageService(this.imageService)
                .diaryRepository(this.diaryRepository)
                .userRepository(this.userRepository)
                .uuidHolder(uuidHolder)
                .build();
        this.babyController = BabyController.builder()
                .babyService(this.babyService)
                .build();
    }
}
