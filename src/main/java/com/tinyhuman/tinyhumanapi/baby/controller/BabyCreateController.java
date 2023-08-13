package com.tinyhuman.tinyhumanapi.baby.controller;

import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.service.BabyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/babies")
@RequiredArgsConstructor
public class BabyCreateController {

    private final BabyServiceImpl babyService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BabyResponse register(@RequestBody BabyCreate babyCreate) {

        return babyService.register(babyCreate);

    }
}
