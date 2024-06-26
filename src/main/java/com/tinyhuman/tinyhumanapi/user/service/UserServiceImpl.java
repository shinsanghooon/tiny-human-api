package com.tinyhuman.tinyhumanapi.user.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserService;
import com.tinyhuman.tinyhumanapi.user.domain.NotificationSettingsUpdate;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;
import com.tinyhuman.tinyhumanapi.user.domain.exception.EmailDuplicateException;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;

    @Builder
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @Override
    public UserResponse registerUser(UserCreate userCreate) {

        checkEmailDuplicated(userCreate.email());

        UserCreate encryptedUserCreate = UserCreate.builder()
                .name(userCreate.name())
                .password(passwordEncoder.encode(userCreate.password()))
                .email(userCreate.email())
                .build();

        User user = userRepository.save(User.fromCreate(encryptedUserCreate));
        return UserResponse.fromModel(user);
    }

    @Override
    public UserResponse getUser(Long userId) {
        User securityUser = authService.getUserOutOfSecurityContextHolder();
        if (!securityUser.id().equals(userId)) {
            log.error("UnauthorizedAccessException(User) - userId:{}",  userId);
            throw new UnauthorizedAccessException("User", userId);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - userId:{}", userId);
                    return new ResourceNotFoundException("User", userId);
                });
        return UserResponse.fromModel(user);
    }

    @Override
    public void checkEmailDuplicated(String email) {
        if(userRepository.existsByEmail(email)){
            throw new EmailDuplicateException();
        }
    }

    @Override
    public UserResponse updateNotificationSettings(Long userId, NotificationSettingsUpdate notificationSettingsUpdate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - userId:{}", userId);
                    return new ResourceNotFoundException("User", userId);
                });

        User update_user = userRepository.save(User.builder()
                .id(user.id())
                .name(user.name())
                .email(user.email())
                .password(user.password())
                .status(user.status())
                .socialMedia(user.socialMedia())
                .lastLoginAt(user.lastLoginAt())
                .isDeleted(user.isDeleted())
                .isAllowChatNotifications(notificationSettingsUpdate.isAllowChatNotifications())
                .isAllowDiaryNotifications(notificationSettingsUpdate.isAllowDiaryNotifications())
                .build());

        return UserResponse.fromModel(update_user);
    }
}
