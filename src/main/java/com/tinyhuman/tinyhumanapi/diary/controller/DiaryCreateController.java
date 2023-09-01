package com.tinyhuman.tinyhumanapi.diary.controller;


import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
@Tag(name = "DiaryCreateController", description = "일기 생성을 위한 컨트롤러입니다.")
public class DiaryCreateController {

    private final DiaryService diaryService;

    @Operation(summary = "일기 생성 API", responses = {
            @ApiResponse(responseCode = "201", description = "일기 생성 성공")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public DiaryResponse createDiary(@RequestBody @Valid DiaryCreate diaryCreate) {
        return diaryService.create(diaryCreate);
    }

}
