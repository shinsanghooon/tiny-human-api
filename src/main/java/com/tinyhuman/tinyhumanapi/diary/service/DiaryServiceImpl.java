package com.tinyhuman.tinyhumanapi.diary.service;

import com.tinyhuman.tinyhumanapi.diary.controller.port.DiaryService;
import com.tinyhuman.tinyhumanapi.diary.domain.Diary;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryCreate;
import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;
import com.tinyhuman.tinyhumanapi.diary.service.port.DiaryRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Builder
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    // 베이비 정보도 미리 있으면 좋을 것 같음
    // TODO 사실 diary, sentences, pictures 데이터가 한 번에 와야함
    public DiaryResponse create(DiaryCreate diaryCreate) {
        Diary newDiary = Diary.from(diaryCreate);
        return DiaryResponse.fromModel(diaryRepository.save(newDiary));
    }
}
