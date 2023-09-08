package com.tinyhuman.tinyhumanapi.user.controller;

import com.tinyhuman.tinyhumanapi.user.controller.port.UserService;
import com.tinyhuman.tinyhumanapi.user.domain.EmailDuplicateCheck;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "UserCreateController", description = "사용자 생성을 위한 컨트롤러입니다.")
public class UserCreateController {

    private final UserService userService;

    @Builder
    public UserCreateController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원 가입 API", responses = {
            @ApiResponse(responseCode = "201", description = "회원 가입 성공")})
    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserCreate userCreate) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registerUser(userCreate));
    }

    @Operation(summary = "이메일 중복 체크 API", responses = {
      @ApiResponse(responseCode = "200", description = "이메일 중복 계정 없음")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/email/duplicate-check")
    public void checkEmailDuplicated(
            @RequestBody @Valid EmailDuplicateCheck emailDuplicateCheck) {
        userService.checkEmailDuplicated(emailDuplicateCheck.email());
    }
}
