package com.tinyhuman.tinyhumanapi.common.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "BasicController", description = "기본적인 처리하기 위한 컨트롤러입니다.")
public class BasicController {

    @GetMapping("health")
    public String health() {
        return "I'm healthy!";
    }
}
