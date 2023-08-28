package com.tinyhuman.tinyhumanapi.auth.service;

import com.tinyhuman.tinyhumanapi.auth.config.jwt.CustomTokenProvider;
import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.auth.domain.LoginRequest;
import com.tinyhuman.tinyhumanapi.auth.domain.RefreshToken;
import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.auth.service.port.RefreshTokenRepository;
import com.tinyhuman.tinyhumanapi.common.domain.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final CustomTokenProvider customTokenProvider;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenResponse login(LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Users-email", authentication.getName()));

        TokenResponse tokenResponse = customTokenProvider.generationToken(user, Duration.ofHours(2));

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.id())
                .refreshToken(tokenResponse.refreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenResponse;
    }

    public User getUserOutOfSecurityContextHolder() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User-email", email));
    }
}

