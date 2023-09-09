package com.tinyhuman.tinyhumanapi.diary.controller;


import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryPreSignedUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/diaries")
@Tag(name = "DiaryCreateController", description = "일기 생성을 위한 컨트롤러입니다.")
public class DiaryCreateController {

    private final DiaryService diaryService;

    @Builder
    public DiaryCreateController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @Operation(summary = "일기 생성 API", responses = {
            @ApiResponse(responseCode = "201", description = "일기 생성 성공")})
    @PostMapping
    public ResponseEntity<DiaryPreSignedUrlResponse> createDiary(@RequestBody @Valid DiaryCreate diaryCreate) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(diaryService.create(diaryCreate));
    }

}
