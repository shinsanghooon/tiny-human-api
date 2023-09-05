package com.tinyhuman.tinyhumanapi.diary.controller;


import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
@Tag(name = "DiaryController", description = "일기 작업을 처리하기 위한 컨트롤러입니다.")
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "일기 삭제 API", responses = {
            @ApiResponse(responseCode = "204", description = "일기 삭제 성공")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteDiary(@PathVariable("id") Long id) {
        diaryService.delete(id);
    }

    @Operation(summary = "일기 단건 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "일기 단건 조회 성공")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public DiaryResponse getDiary(@PathVariable("id") Long id) {
        return diaryService.findById(id);
    }

    @Operation(summary = "아기에 대한 일기 전체 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "일기 조회 성공")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/babies/{babyId}")
    public List<DiaryResponse> getMyDiaries(@PathVariable("babyId") Long babyId) {
        return diaryService.getMyDiariesByBaby(babyId);
    }


}
