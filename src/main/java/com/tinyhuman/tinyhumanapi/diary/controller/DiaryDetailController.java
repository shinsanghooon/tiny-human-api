package com.tinyhuman.tinyhumanapi.diary.controller;


import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryDetailService;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.*;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
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
@Tag(name = "DiaryDetailController", description = "일기에 포함되는 글, 사진에 대한 처리를 위한 컨트롤러입니다.")
public class DiaryDetailController {

    private final DiaryDetailService diaryDetailService;

    @Builder
    public DiaryDetailController(DiaryDetailService diaryDetailService) {
        this.diaryDetailService = diaryDetailService;
    }

    @Operation(summary = "일기 글 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "글 수정 성공")})
    @PatchMapping("{diaryId}/sentences/{sentenceId}")
    public ResponseEntity<DiaryResponse> updateDiarySentence(@PathVariable("diaryId") Long diaryId, @PathVariable("sentenceId") Long sentenceId,
                                                             @RequestBody SentenceCreate sentence) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(diaryDetailService.updateSentence(diaryId, sentenceId, sentence));
    }

    @Operation(summary = "일기 글 삭제 API", responses = {
            @ApiResponse(responseCode = "204", description = "글 삭제 성공")})
    @DeleteMapping("{diaryId}/sentences/{sentenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteDiarySentence(@PathVariable("diaryId") Long diaryId, @PathVariable("sentenceId") Long sentenceId) {
        diaryDetailService.deleteSentence(diaryId, sentenceId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "일기 메인 사진 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "메인 사진 변경 성공")})
    @PatchMapping("{diaryId}/pictures/main")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Picture>> changeMainPicture(@PathVariable("diaryId") Long diaryId, @RequestBody ChangeMainPicture changeMainPicture) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(diaryDetailService.changeMainPicture(diaryId, changeMainPicture.currentPictureId(), changeMainPicture.newPictureId()));
    }

    @Operation(summary = "일기 사진 삭제 API", responses = {
            @ApiResponse(responseCode = "204", description = "사진 삭제 성공")})
    @DeleteMapping("{diaryId}/pictures/{pictureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteDiaryPicture(@PathVariable("diaryId") Long diaryId, @PathVariable("pictureId") Long pictureId) {
        diaryDetailService.deletePicture(diaryId, pictureId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "일기 사진 추가 API", responses = {
            @ApiResponse(responseCode = "201", description = "사진 삭제 성공")})
    @PostMapping("{diaryId}/pictures")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DiaryPreSignedUrlResponse> addDiaryPicture(@PathVariable("diaryId") Long diaryId, @RequestBody List<PictureCreate> files) {
        DiaryPreSignedUrlResponse diaryPreSignedUrlResponse = diaryDetailService.addPictures(diaryId, files);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(diaryPreSignedUrlResponse);
    }
}
