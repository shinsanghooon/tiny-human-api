package com.tinyhuman.tinyhumanapi.user.controller;

import com.tinyhuman.tinyhumanapi.common.mock.TestContainer;
import com.tinyhuman.tinyhumanapi.user.domain.EmailDuplicateCheck;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;
import com.tinyhuman.tinyhumanapi.user.domain.exception.EmailDuplicateException;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserCreateControllerTest {
    @Test
    @DisplayName("사용자는 회원 가입을 할 수 있다.")
    void registerUser() {
        TestContainer testContainer = TestContainer.builder().build();

        UserCreate userCreate = UserCreate.builder()
                .name("유닛")
                .email("unit@unit.com")
                .password("unit")
                .build();

        ResponseEntity<UserResponse> result = testContainer.userCreateController.registerUser(userCreate);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody().id()).isEqualTo(1L);
        assertThat(result.getBody().name()).isEqualTo("유닛");
        assertThat(result.getBody().email()).isEqualTo("unit@unit.com");
        assertThat(result.getBody().status()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("가입하려는 이메일이 사용 가능한 이메일이면 200을 응답한다.")
    void checkEmailDuplicatedSuccess() {
        TestContainer testContainer = TestContainer.builder().build();

        UserCreate userCreate = UserCreate.builder()
                .name("유닛")
                .email("unit@unit.com")
                .password("unit")
                .build();

        testContainer.userCreateController.registerUser(userCreate);

        ResponseEntity<Void> result = testContainer.userCreateController.checkEmailDuplicated(new EmailDuplicateCheck("test@unit.com"));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

    }

    @Test
    @DisplayName("가입하려는 이메일이 중복된 이메일이면 예외를 던진다.")
    void checkEmailDuplicatedFail() {
        TestContainer testContainer = TestContainer.builder().build();

        UserCreate userCreate = UserCreate.builder()
                .name("유닛")
                .email("unit@unit.com")
                .password("unit")
                .build();

        testContainer.userCreateController.registerUser(userCreate);

        assertThatThrownBy(() ->
                testContainer.userCreateController.checkEmailDuplicated(new EmailDuplicateCheck("unit@unit.com")))
                .isInstanceOf(EmailDuplicateException.class)
                .hasMessageContaining("중복된 이메일");
    }

}