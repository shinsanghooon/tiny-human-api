package com.tinyhuman.tinyhumanapi.diary.controller.port;

import com.tinyhuman.tinyhumanapi.diary.domain.DiaryResponse;
import com.tinyhuman.tinyhumanapi.diary.domain.SentenceCreate;

public interface DiaryDetailService {

    public DiaryResponse updateSentence(Long diaryId, Long sentenceId, SentenceCreate sentences);

    public void deleteSentence(Long diaryId, Long sentenceId);

    public void changeMainPicture(Long diaryId, Long currentMainPictureId, Long newMainPictureId);

    public void deletePicture(Long diaryId, Long pictureId);

}
