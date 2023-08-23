package com.tinyhuman.tinyhumanapi.user.service;

import com.tinyhuman.tinyhumanapi.common.domain.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;
import com.tinyhuman.tinyhumanapi.user.domain.exception.EmailDuplicateException;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(UserCreate userCreate) {

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return UserResponse.fromModel(user);
    }

    @Override
    public void checkEmailDuplicated(String email) {
        if(userRepository.existsByEmail(email)){
            throw new EmailDuplicateException();
        }
    }
}
