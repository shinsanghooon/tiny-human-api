package com.tinyhuman.tinyhumanapi.diary.controller;


import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryCreateController {

    private final DiaryService diaryServiceImpl;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public DiaryResponse createDiary(@RequestBody DiaryCreate diaryCreate) {
        return diaryServiceImpl.create(diaryCreate);
    }

}
