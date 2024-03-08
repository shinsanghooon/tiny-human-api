package com.tinyhuman.tinyhumanapi.helpchat.controller;

import com.tinyhuman.tinyhumanapi.helpchat.controller.port.HelpRequestService;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatResponse;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpRequestCreate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/helpchat")
@Tag(name = "HelpChatController", description = "HelpChat를 처리하기 위한 컨트롤러입니다.")
public class HelpChatController {

    private final HelpRequestService helpRequestService;

    @Builder
    public HelpChatController(HelpRequestService helpRequestService) {
        this.helpRequestService = helpRequestService;
    }

    @PostMapping
    public ResponseEntity<HelpChatResponse> registerChecklist(@RequestBody HelpRequestCreate helpRequestCreate) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(helpRequestService.register(helpRequestCreate));
    }
}
