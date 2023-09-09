package com.tinyhuman.tinyhumanapi.user.service;

import com.tinyhuman.tinyhumanapi.auth.mock.FakeAuthService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import com.tinyhuman.tinyhumanapi.user.mock.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceImplTest {
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void init() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        FakeAuthService fakeAuthService = new FakeAuthService();
        this.userServiceImpl = new UserServiceImpl(fakeUserRepository, passwordEncoder, fakeAuthService);

        UserCreate userCreate1 = UserCreate.builder()
                .name("홈버그")
                .email("homebug@tinyhuman.com")
                .password("1234")
                .build();

        UserCreate userCreate2 = UserCreate.builder()
                .name("겐지")
                .email("genzi@tinyhuman.com")
                .password("1234")
                .build();

        fakeUserRepository.save(User.fromCreate(userCreate1));
        fakeUserRepository.save(User.fromCreate(userCreate2));
    }

    @Test
    @DisplayName("UserCreate를 이용하여 회원가입을 할 수 있다.")
    void registerUser() {
        UserCreate userCreate = UserCreate.builder()
                .name("나어른")
                .email("na@tinyhuman.com")
                .password("1234")
                .build();

        UserResponse userResponse = userServiceImpl.registerUser(userCreate);

        assertThat(userResponse.id()).isNotNull();
        assertThat(userResponse.email()).isEqualTo(userCreate.email());
        assertThat(userResponse.name()).isEqualTo(userCreate.name());
        assertThat(userResponse.status()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userResponse.lastLoginAt()).isNull();
    }

    @Test
    @DisplayName("UserId를 통해 사용자 정보를 조회할 수 있다.")
    void getUser() {
        Long userId = 1L;

        UserResponse userResponse = userServiceImpl.getUser(userId);

        assertThat(userResponse.id()).isEqualTo(userId);
        assertThat(userResponse.email()).isEqualTo("homebug@tinyhuman.com");
        assertThat(userResponse.name()).isEqualTo("홈버그");
        assertThat(userResponse.status()).isEqualTo(UserStatus.ACTIVE);

    }

}