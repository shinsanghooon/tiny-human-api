package com.tinyhuman.tinyhumanapi.diary.controller;


import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryDetailService;
import com.tinyhuman.tinyhumanapi.diary.domain.ChangeMainPicture;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import com.tinyhuman.tinyhumanapi.diary.domain.SentenceCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryDetailController {

    private final DiaryDetailService diaryDetailService;

    @PatchMapping("{diaryId}/sentences/{sentenceId}")
    @ResponseStatus(HttpStatus.OK)
    public DiaryResponse updateDiarySentence(@PathVariable("diaryId") Long diaryId, @PathVariable("sentenceId") Long sentenceId,
                                             @RequestBody SentenceCreate sentence) {
        return diaryDetailService.updateSentence(diaryId, sentenceId, sentence);
    }

    @DeleteMapping("{diaryId}/sentences/{sentenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDiarySentence(@PathVariable("diaryId") Long diaryId, @PathVariable("sentenceId") Long sentenceId) {
        diaryDetailService.deleteSentence(diaryId, sentenceId);
    }

    @PatchMapping("{diaryId}/pictures/main")
    @ResponseStatus(HttpStatus.OK)
    public List<Picture> changeMainPicture(@PathVariable("diaryId") Long diaryId, @RequestBody ChangeMainPicture changeMainPicture) {
        return diaryDetailService.changeMainPicture(diaryId, changeMainPicture.currentPictureId(), changeMainPicture.newPictureId());
    }
}
