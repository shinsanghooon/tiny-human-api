package com.tinyhuman.tinyhumanapi.user.controller;

import com.tinyhuman.tinyhumanapi.user.controller.port.UserService;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "사용자에 대한 작업 처리를 위한 컨트롤러입니다.")
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public UserResponse getUser(@PathVariable("id") Long userId) {
        return userService.getUser(userId);
    }

}
