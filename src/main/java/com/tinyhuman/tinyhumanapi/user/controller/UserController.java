package com.tinyhuman.tinyhumanapi.user.controller;

import com.tinyhuman.tinyhumanapi.user.controller.port.UserService;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "UserController", description = "사용자에 대한 작업 처리를 위한 컨트롤러입니다.")
public class UserController {

    private final UserService userService;

    @Builder
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "사용자 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공")})
    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") @Valid @Min(1) Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUser(userId));
    }

}
