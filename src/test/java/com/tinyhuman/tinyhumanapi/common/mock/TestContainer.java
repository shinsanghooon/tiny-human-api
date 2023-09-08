package com.tinyhuman.tinyhumanapi.common.mock;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.auth.mock.FakeAuthService;
import com.tinyhuman.tinyhumanapi.user.controller.UserController;
import com.tinyhuman.tinyhumanapi.user.controller.UserCreateController;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserService;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserRepository;
import com.tinyhuman.tinyhumanapi.user.service.UserServiceImpl;
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

    @Builder
    public TestContainer() {
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
    }
}
