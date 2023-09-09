package com.tinyhuman.tinyhumanapi.user.controller;

import com.tinyhuman.tinyhumanapi.common.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.common.mock.TestContainer;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserControllerTest {

    @Test
    @DisplayName("사용자는 비밀번호를 제외한 본인의 정보를 조회할 수 있다.")
    void getMyProfile() {
        TestContainer testContainer = TestContainer.builder().build();

        testContainer.userRepository.save(User.builder()
                .name("유닛")
                .email("unit@unit.com")
                .password("unit")
                .build());

        ResponseEntity<UserResponse> result = testContainer.userController.getUser(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("유닛");
        assertThat(result.getBody().email()).isEqualTo("unit@unit.com");
        assertThat(result.getBody().status()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("사용자는 본인 외의 다른 사용자의 정보를 조회할 수 없다.")
    void cannotGetOtherProfile() {
        TestContainer testContainer = TestContainer.builder().build();

        testContainer.userRepository.save(User.builder()
                .name("홈버그")
                .email("homebug@tinyhuman.com")
                .password("unit")
                .build());

        assertThatThrownBy(() -> {
            testContainer.userController.getUser(9999L);
        }).isInstanceOf(UnauthorizedAccessException.class);
    }
}