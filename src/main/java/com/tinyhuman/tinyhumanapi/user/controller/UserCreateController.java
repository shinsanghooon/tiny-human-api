package com.tinyhuman.tinyhumanapi.user.controller;

import com.tinyhuman.tinyhumanapi.user.controller.port.UserPushService;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserService;
import com.tinyhuman.tinyhumanapi.user.domain.*;
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

    private final UserPushService userPushService;

    @Builder
    public UserCreateController(UserService userService, UserPushService userPushService) {
        this.userService = userService;
        this.userPushService = userPushService;
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
    public ResponseEntity<Void> checkEmailDuplicated(
            @RequestBody @Valid EmailDuplicateCheck emailDuplicateCheck) {
        userService.checkEmailDuplicated(emailDuplicateCheck.email());
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(summary = "푸시 정보 등록 API", responses = {
            @ApiResponse(responseCode = "201", description = "푸시 정보 등록 성공")})
    @PostMapping("/tokens")
    public ResponseEntity<UserPushToken> registerPushInfo(@RequestBody @Valid UserPushTokenCreate userPushTokenCreate) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userPushService.registerToken(userPushTokenCreate));
    }

    @PatchMapping("/{userId}/settings/notifications")
    public ResponseEntity<UserResponse> updateNotificationSettings(@PathVariable Long userId, @RequestBody NotificationSettingsUpdate notificationSettingsUpdate) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateNotificationSettings(userId, notificationSettingsUpdate));

    }

}
