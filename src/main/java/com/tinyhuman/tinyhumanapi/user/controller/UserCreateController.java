package com.tinyhuman.tinyhumanapi.user.controller;

import com.tinyhuman.tinyhumanapi.user.controller.port.UserService;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserCreateController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponse registerUser(@RequestBody @Valid UserCreate userCreate) {
        return userService.registerUser(userCreate);
    }

}
