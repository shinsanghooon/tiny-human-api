package com.tinyhuman.tinyhumanapi.auth.controller.port;

import com.tinyhuman.tinyhumanapi.auth.domain.RefreshToken;

public interface RefreshTokenService {

    public RefreshToken findByRefreshToken(String refreshToken);
}
