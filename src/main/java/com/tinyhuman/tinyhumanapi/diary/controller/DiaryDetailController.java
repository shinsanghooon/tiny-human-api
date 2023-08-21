package com.tinyhuman.tinyhumanapi.diary.controller;


import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryDetailService;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;
import com.tinyhuman.tinyhumanapi.diary.domain.SentenceCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryDetailController {

    private final DiaryDetailService diaryDetailService;
    @PatchMapping("{diaryId}/sentences/{sentenceId}")
    public DiaryResponse updateDiarySentence(@PathVariable("diaryId") Long diaryId, @PathVariable("sentenceId") Long sentenceId,
                                             @RequestBody SentenceCreate sentence) {
        return diaryDetailService.updateSentence(diaryId, sentenceId, sentence);
    }

}
