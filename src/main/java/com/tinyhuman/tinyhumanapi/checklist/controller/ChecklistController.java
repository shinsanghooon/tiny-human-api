package com.tinyhuman.tinyhumanapi.checklist.controller;

import com.tinyhuman.tinyhumanapi.checklist.controller.port.ChecklistDetailService;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.ChecklistService;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistCreate;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checklist")
@Tag(name = "ChecklistController", description = "체크리스트를 처리하기 위한 컨트롤러입니다.")
public class ChecklistController {

    private final ChecklistService checklistService;

    private final ChecklistDetailService checklistDetailService;

    @Builder
    public ChecklistController(ChecklistService checklistService, ChecklistDetailService checklistDetailService) {
        this.checklistService = checklistService;
        this.checklistDetailService = checklistDetailService;
    }

    @PostMapping
    public ResponseEntity<ChecklistResponse> registerChecklist(@RequestBody @Valid ChecklistCreate checklistCreate) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(checklistService.register(checklistCreate));
    }

    @PostMapping("/{checklist_id}/detail/{detail_id}")
    public ResponseEntity<Void> checkUpdate(@PathVariable("checklist_id") Long checklistId, @PathVariable("detail_id") Long detailId) {
        checklistDetailService.checkUpdate(checklistId, detailId);

        return ResponseEntity
                .status(HttpStatus.OK).build();
    }


}
