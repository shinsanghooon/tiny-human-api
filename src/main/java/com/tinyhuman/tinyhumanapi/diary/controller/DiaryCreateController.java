package com.tinyhuman.tinyhumanapi.diary.controller;


import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryCreateController {

    private final DiaryService diaryServiceImpl;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public DiaryResponse createDiary(@RequestPart @Valid DiaryCreate diaryCreate,
                                     @RequestPart @Size(max = 5, message="사진 및 동영상은 최대 업로드 개수는 5개입니다.") List<MultipartFile> files) {
        return diaryServiceImpl.create(diaryCreate, files);
    }

}
