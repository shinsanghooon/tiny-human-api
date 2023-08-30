package com.tinyhuman.tinyhumanapi.auth.controller;


import com.tinyhuman.tinyhumanapi.auth.controller.port.TokenService;
import com.tinyhuman.tinyhumanapi.auth.domain.CreateAccessTokenRequest;
import com.tinyhuman.tinyhumanapi.auth.domain.CreateAccessTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/token")
@RequiredArgsConstructor
@Tag(name = "TokenController", description = "토큰 생성을 위한 컨트롤러입니다.")
public class TokenController {

    private final TokenService tokenService;

    @Operation(summary = "액세스 토큰 발급 API", responses = {
            @ApiResponse(responseCode = "201", description = "리프레스 토큰을 이용해 액세스 토큰 재발행 성공")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAccessTokenResponse createAccessTokenResponse(@RequestBody CreateAccessTokenRequest createAccessTokenRequest) {
        return tokenService.createNewAccessToken(createAccessTokenRequest.refreshToken());
    }
}
