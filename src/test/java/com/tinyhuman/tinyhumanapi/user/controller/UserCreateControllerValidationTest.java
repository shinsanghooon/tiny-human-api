package com.tinyhuman.tinyhumanapi.user.controller;

import com.tinyhuman.tinyhumanapi.common.exception.CommonExceptionHandler;
import com.tinyhuman.tinyhumanapi.common.utils.MockMvcUtil;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserService;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.domain.exception.UserExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.tinyhuman.tinyhumanapi.common.utils.MockMvcUtil.OBJECT_MAPPER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserCreateControllerValidationTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserCreateController userCreateController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        Object[] controllerAdvices = {new CommonExceptionHandler(), new UserExceptionHandler()};
        mockMvc = MockMvcUtil.createMockMvc(userCreateController, controllerAdvices);
    }

    @DisplayName("이름은 null이거나 비어 있으면 400 응답을 받는다.")
    @ParameterizedTest
    @NullAndEmptySource
    void registerUserWithNullAndEmptyName(String invalidName) throws Exception {
        // given
        UserCreate request = UserCreate.builder()
                .name(invalidName)
                .email("unit@unit.com")
                .password("unit")
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/users")
                        .content(OBJECT_MAPPER.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print());
    }

    @DisplayName("이름의 길이가 20자를 초과하면 400 응답을 받는다.")
    @ParameterizedTest
    @ValueSource(strings = {"controller-service-repository", "df&&#nuhlzubl&^%uzjdfnjadf!", "이름은 20자를 초과할 수 없습니다를 테스트합니다"})
    void registerUserWithNameOver20(String invalidName) throws Exception {
        // given
        UserCreate request = UserCreate.builder()
                .name(invalidName)
                .email("unit@unit.com")
                .password("unit")
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/users")
                        .content(OBJECT_MAPPER.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print());
    }

    @DisplayName("이메일은 null이거나 비어 있으면 400 응답을 받는다.")
    @ParameterizedTest
    @NullAndEmptySource
    void registerUserWithNullAndEmptyEmail(String invaildEmail) throws Exception {
        // given
        UserCreate request = UserCreate.builder()
                .name("유닛")
                .email(invaildEmail)
                .password("unit")
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/users")
                        .content(OBJECT_MAPPER.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print());
    }

    @DisplayName("이메일 형식이 맞지 않는 경우 400 응답을 받는다.")
    @ParameterizedTest
    @ValueSource(strings = {"controller-service-repository", "abced@abced", "abced.com"})
    void registerUserWithInvalidEmailFormat(String invalidEmail) throws Exception {
        // given
        UserCreate request = UserCreate.builder()
                .name("유닛")
                .email(invalidEmail)
                .password("unit")
                .build();

        ResultActions result = mockMvc.perform(
                post("/api/v1/users")
                        .content(OBJECT_MAPPER.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print());
    }
}

