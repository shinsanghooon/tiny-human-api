package com.tinyhuman.tinyhumanapi.auth.controller.port;

import com.tinyhuman.tinyhumanapi.auth.domain.LoginRequest;
import com.tinyhuman.tinyhumanapi.auth.domain.SocialLoginRequest;
import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.user.domain.User;

public interface AuthService {

    TokenResponse login(LoginRequest loginRequest);

    User getUserOutOfSecurityContextHolder();

    TokenResponse googleLogin(SocialLoginRequest socialLoginRequest);
}
