package com.tinyhuman.tinyhumanapi.auth.service;

import com.tinyhuman.tinyhumanapi.auth.config.jwt.CustomTokenProvider;
import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.auth.controller.port.dto.GoogleInfoResponse;
import com.tinyhuman.tinyhumanapi.auth.domain.LoginRequest;
import com.tinyhuman.tinyhumanapi.auth.domain.RefreshToken;
import com.tinyhuman.tinyhumanapi.auth.domain.SocialLoginRequest;
import com.tinyhuman.tinyhumanapi.auth.domain.TokenResponse;
import com.tinyhuman.tinyhumanapi.auth.eum.SocialMedia;
import com.tinyhuman.tinyhumanapi.auth.service.port.RefreshTokenRepository;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.enums.UserStatus;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final CustomTokenProvider customTokenProvider;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Builder
    public AuthServiceImpl(AuthenticationManager authenticationManager, CustomTokenProvider customTokenProvider, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.customTokenProvider = customTokenProvider;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public TokenResponse login(LoginRequest loginRequest) {

        String email = loginRequest.email();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(email, loginRequest.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - userEmail:{}", email);
                    return new ResourceNotFoundException("User", " " + email);
                });

        TokenResponse tokenResponse = customTokenProvider.generationToken(user, Duration.ofHours(1));

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
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - userEmail:{}", email);
                    return new ResourceNotFoundException("User", " " + email);
                });
    }

    @Override
    public TokenResponse googleLogin(SocialLoginRequest socialLoginRequest) {
        String email = socialLoginRequest.email();
        String accessToken = socialLoginRequest.socialAccessToken();
        String name = socialLoginRequest.name();
        String photoUrl = socialLoginRequest.photoUrl();

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        ResponseEntity<GoogleInfoResponse> infoResponse = restTemplate.postForEntity("https://www.googleapis.com/oauth2/v1/tokeninfo",
                map, GoogleInfoResponse.class);

        String responseEmail = infoResponse.getBody().email();
        boolean isVerifiedEmail = infoResponse.getBody().verified_email();

        if (!isVerifiedEmail) {
            log.error("Google Login Fail to Verify (User) - userEmail:{}", email);
            throw new UnauthorizedAccessException("구글 로그인", email);
        }

        if (!email.equals(responseEmail)) {
            log.error("Google Login Fail to Verify (User) - userEmail:{}", email);
            throw new UnauthorizedAccessException("구글 로그인", email);
        }

        boolean isEmailExists = userRepository.existsByEmailAndSocialMedia(email, SocialMedia.GOOGLE);

        User user;
        if (isEmailExists) {
            user = userRepository.findByEmailAndSocialMedia(email, SocialMedia.GOOGLE).get();
        } else {
            user = userRepository.save(User.builder()
                    .name(name)
                    .email(email)
                    .password(accessToken)
                    .status(UserStatus.ACTIVE)
                    .socialMedia(SocialMedia.GOOGLE)
                    .build());
        }

        TokenResponse tokenResponse = customTokenProvider.generationToken(user, Duration.ofHours(1));

        RefreshToken newRefreshToken = generateNewRefreshToken(user, tokenResponse);

        refreshTokenRepository.save(newRefreshToken);

        return tokenResponse;
    }
}

