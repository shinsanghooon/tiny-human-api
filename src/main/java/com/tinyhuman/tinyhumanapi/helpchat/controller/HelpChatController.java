package com.tinyhuman.tinyhumanapi.helpchat.controller;

import com.tinyhuman.tinyhumanapi.helpchat.controller.port.HelpChatService;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.HelpRequestService;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/helpchat")
@Tag(name = "HelpChatController", description = "HelpChat를 처리하기 위한 컨트롤러입니다.")
public class HelpChatController {

    private final HelpRequestService helpRequestService;

    private final HelpChatService helpChatService;

    @Builder
    public HelpChatController(HelpRequestService helpRequestService, HelpChatService helpChatService) {
        this.helpRequestService = helpRequestService;
        this.helpChatService = helpChatService;
    }

    @PostMapping("help-request")
    public ResponseEntity<HelpRequestResponse> registerHelpRequest(@RequestBody HelpRequestCreate helpRequestCreate) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(helpRequestService.register(helpRequestCreate));
    }

    @GetMapping("help-request")
    public ResponseEntity<List<HelpRequestResponse>> getHelpRequests() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(helpRequestService.getHelpRequest());
    }

    @PostMapping()
    public ResponseEntity<HelpChatResponse> registerHelpChat(@RequestBody HelpChatCreate helpChatCreate) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(helpChatService.register(helpChatCreate));
    }

    @GetMapping()
    public ResponseEntity<List<HelpChatResponse>> getHelpChats() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(helpChatService.getHelpChats());
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updateLatestMessage(@PathVariable("id") Long helpChatId,
                                                    @RequestBody HelpChatLatestMessage helpChatLatestMessage){
        helpChatService.updateLatestMessage(helpChatId, helpChatLatestMessage);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
