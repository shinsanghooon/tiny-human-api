package com.tinyhuman.tinyhumanapi.diary.controller;


import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteDiary(@PathVariable("id") Long id) {
        diaryService.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public DiaryResponse getDiary(@PathVariable("id") Long id) {
        return diaryService.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/babies/{babyId}")
    public List<DiaryResponse> getMyDiaries(@PathVariable("babyId") Long babyId) {
        return diaryService.getMyDiariesByBaby(babyId);
    }


}
