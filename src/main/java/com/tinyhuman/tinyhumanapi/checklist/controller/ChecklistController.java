package com.tinyhuman.tinyhumanapi.checklist.controller;

import com.tinyhuman.tinyhumanapi.checklist.controller.port.ChecklistDetailService;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.ChecklistService;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistCreate;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistResponse;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ToggleAllUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<ChecklistResponse>> getAllChecklist() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(checklistService.getChecklist());
    }

    @PatchMapping
    public ResponseEntity<ChecklistResponse> update(@RequestBody @Valid ChecklistCreate checklistCreate) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(checklistService.update(checklistCreate));
    }

    @PatchMapping("{checklist_id}/detail/{checklist_detail_id}/toggle")
    public ResponseEntity<Void> toggleCheckDetail(@PathVariable("checklist_id") Long checklistId,
                                                  @PathVariable("checklist_detail_id") Long checklist_detail_id) {
        checklistDetailService.toggleCheckDetail(checklistId, checklist_detail_id);
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @PatchMapping("{checklist_id}/toggle-all")
    public ResponseEntity<Void> toggleAllCheckDetail(@PathVariable("checklist_id") Long checklistId,
                                                     @RequestBody ToggleAllUpdateRequest toggleAllUpdateRequest) {
        checklistDetailService.toggleAllCheckDetail(checklistId, toggleAllUpdateRequest.targetChecked());
        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @DeleteMapping("{checklist_id}")
    public ResponseEntity<Void> deleteChecklist(@PathVariable("checklist_id") Long checklistId) {
        checklistService.delete(checklistId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT).build();
    }


}
