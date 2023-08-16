package com.tinyhuman.tinyhumanapi.user.service;

import com.tinyhuman.tinyhumanapi.common.domain.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.user.controller.port.UserService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public UserResponse registerUser(UserCreate userCreate) {
        User user = userRepository.save(User.fromCreate(userCreate));
        return UserResponse.fromModel(user);
    }

    @Override
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return UserResponse.fromModel(user);
    }
}
