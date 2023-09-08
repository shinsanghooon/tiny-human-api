package com.tinyhuman.tinyhumanapi.user.controller;

import com.tinyhuman.tinyhumanapi.common.mock.TestContainer;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

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

}