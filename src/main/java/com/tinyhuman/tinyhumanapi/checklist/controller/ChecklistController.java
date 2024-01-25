package com.tinyhuman.tinyhumanapi.checklist.controller;

import com.tinyhuman.tinyhumanapi.checklist.controller.port.ChecklistService;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistCreate;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/checklist")
@Tag(name = "ChecklistController", description = "체크리스트를 처리하기 위한 컨트롤러입니다.")
public class ChecklistController {

    private final ChecklistService checklistService;

    @Builder
    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @PostMapping
    public ResponseEntity<ChecklistResponse> registerChecklist(@RequestBody @Valid ChecklistCreate checklistCreate) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(checklistService.register(checklistCreate));
    }


}
