package com.tinyhuman.tinyhumanapi.auth.controller;


import com.tinyhuman.tinyhumanapi.auth.controller.port.TokenService;
import com.tinyhuman.tinyhumanapi.auth.domain.CreateAccessTokenRequest;
import com.tinyhuman.tinyhumanapi.auth.domain.CreateAccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAccessTokenResponse createAccessTokenResponse(@RequestBody CreateAccessTokenRequest createAccessTokenRequest) {
        return tokenService.createNewAccessToken(createAccessTokenRequest.refreshToken());
    }
}
