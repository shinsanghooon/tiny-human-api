package com.tinyhuman.tinyhumanapi.auth.controller.port;

import com.tinyhuman.tinyhumanapi.auth.domain.CreateAccessTokenResponse;

public interface TokenService {

    CreateAccessTokenResponse createNewAccessToken(String refreshToken);


}
