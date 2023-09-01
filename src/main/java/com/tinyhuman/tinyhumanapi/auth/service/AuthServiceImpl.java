package com.tinyhuman.tinyhumanapi.auth.service;

import com.tinyhuman.tinyhumanapi.auth.config.jwt.CustomTokenProviderImpl;
import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.auth.domain.LoginRequest;
import com.tinyhuman.tinyhumanapi.auth.domain.RefreshToken;
import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.auth.service.port.RefreshTokenRepository;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final CustomTokenProviderImpl customTokenProvider;

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

        RefreshToken newRefreshToken = generateNewRefreshToken(user, tokenResponse);

        refreshTokenRepository.save(newRefreshToken);

        return tokenResponse;
    }

    private RefreshToken generateNewRefreshToken(User user, TokenResponse tokenResponse) {
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUserId(user.id());

        RefreshToken newRefreshToken;
        if (findRefreshToken.isPresent()) {
            RefreshToken refreshToken = findRefreshToken.get();
            newRefreshToken = refreshToken.update(tokenResponse.refreshToken());
        } else {
            newRefreshToken = RefreshToken.builder()
                    .userId(user.id())
                    .refreshToken(tokenResponse.refreshToken())
                    .build();
        }
        return newRefreshToken;
    }

    public User getUserOutOfSecurityContextHolder() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User-email", email));
    }
}

