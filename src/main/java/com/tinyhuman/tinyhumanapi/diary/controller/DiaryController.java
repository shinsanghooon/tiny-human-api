package com.tinyhuman.tinyhumanapi.diary.controller;


import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import com.tinyhuman.tinyhumanapi.common.utils.PageCursor;
import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diaries")
@Tag(name = "DiaryController", description = "일기 작업을 처리하기 위한 컨트롤러입니다.")
public class DiaryController {

    private final DiaryService diaryService;

    @Builder
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @Operation(summary = "일기 삭제 API", responses = {
            @ApiResponse(responseCode = "204", description = "일기 삭제 성공")})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDiary(@PathVariable("id") Long id) {
        diaryService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "일기 단건 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "일기 단건 조회 성공")})
    @GetMapping("{id}")
    public ResponseEntity<DiaryResponse> getDiary(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(diaryService.findById(id));
    }

    @Operation(summary = "일기 날짜 검색 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "일기 단건 조회 성공")})
    @GetMapping("/babies/{babyId}/search")
    public ResponseEntity<List<DiaryResponse>> getDiaryByDate(@PathVariable("babyId") Long babyId, @RequestParam("date") String date) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(diaryService.findByDate(babyId, date));
    }

    // TODO: Pagination 적용
    @Operation(summary = "아기에 대한 일기 전체 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "일기 조회 성공")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/babies/{babyId}")
    public ResponseEntity<PageCursor<DiaryResponse>> getMyDiaries(@PathVariable("babyId") Long babyId,
                                                                  @RequestParam(value = "key", required = false) Long key, @RequestParam("size") int size) {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(diaryService.getMyDiariesByBaby(babyId, cursorRequest));
    }


}
