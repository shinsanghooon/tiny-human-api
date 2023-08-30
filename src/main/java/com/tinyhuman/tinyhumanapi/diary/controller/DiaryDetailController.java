package com.tinyhuman.tinyhumanapi.diary.controller;


import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryDetailService;
import com.tinyhuman.tinyhumanapi.diary.domain.ChangeMainPicture;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import com.tinyhuman.tinyhumanapi.diary.domain.SentenceCreate;
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
@Tag(name = "DiaryDetailController", description = "일기에 포함되는 글, 사진에 대한 처리를 위한 컨트롤러입니다.")
public class DiaryDetailController {

    private final DiaryDetailService diaryDetailService;

    @Operation(summary = "일기 글 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "글 수정 성공")})
    @PatchMapping("{diaryId}/sentences/{sentenceId}")
    @ResponseStatus(HttpStatus.OK)
    public DiaryResponse updateDiarySentence(@PathVariable("diaryId") Long diaryId, @PathVariable("sentenceId") Long sentenceId,
                                             @RequestBody SentenceCreate sentence) {
        return diaryDetailService.updateSentence(diaryId, sentenceId, sentence);
    }

    @Operation(summary = "일기 글 삭제 API", responses = {
            @ApiResponse(responseCode = "204", description = "글 삭제 성공")})
    @DeleteMapping("{diaryId}/sentences/{sentenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDiarySentence(@PathVariable("diaryId") Long diaryId, @PathVariable("sentenceId") Long sentenceId) {
        diaryDetailService.deleteSentence(diaryId, sentenceId);
    }

    @Operation(summary = "일기 메인 사진 변경 API", responses = {
            @ApiResponse(responseCode = "200", description = "메인 사진 변경 성공")})
    @PatchMapping("{diaryId}/pictures/main")
    @ResponseStatus(HttpStatus.OK)
    public List<Picture> changeMainPicture(@PathVariable("diaryId") Long diaryId, @RequestBody ChangeMainPicture changeMainPicture) {
        return diaryDetailService.changeMainPicture(diaryId, changeMainPicture.currentPictureId(), changeMainPicture.newPictureId());
    }
}
