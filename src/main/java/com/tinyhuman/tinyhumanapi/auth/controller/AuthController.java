package com.tinyhuman.tinyhumanapi.auth.controller;

import com.tinyhuman.tinyhumanapi.auth.domain.LoginRequest;
import com.tinyhuman.tinyhumanapi.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("login")
    public void login(@Valid @RequestBody LoginRequest loginRequest) {
        authService.login(loginRequest);
    }

}

