package com.tinyhuman.tinyhumanapi.auth.controller;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.auth.domain.LoginRequest;
import com.tinyhuman.tinyhumanapi.auth.domain.SocialLoginRequest;
import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인 API", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("login")
    public TokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Operation(summary = "구글 로그인 API", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("google")
    public TokenResponse googleLogin(@Valid @RequestBody SocialLoginRequest socialLoginRequest) {
        return authService.googleLogin(socialLoginRequest);
    }

}

