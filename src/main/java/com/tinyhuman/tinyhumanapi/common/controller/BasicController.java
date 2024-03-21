package com.tinyhuman.tinyhumanapi.common.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "BasicController", description = "기본적인 처리하기 위한 컨트롤러입니다.")
public class BasicController {

    @GetMapping("health")
    @ResponseStatus(HttpStatus.OK)
    public String health() {
        return "I'm healthy!";
    }
}
