package com.tinyhuman.tinyhumanapi.diary.controller.port;

import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.*;
import com.tinyhuman.tinyhumanapi.diary.domain.Picture;
import com.tinyhuman.tinyhumanapi.diary.domain.Sentence;

import java.util.List;

public interface DiaryDetailService {

    public DiaryResponse updateSentence(Long diaryId, Long sentenceId, SentenceCreate sentences);

    public Sentence deleteSentence(Long diaryId, Long sentenceId);

    public List<Picture> changeMainPicture(Long diaryId, Long currentMainPictureId, Long newMainPictureId);

    public Picture deletePicture(Long diaryId, Long pictureId);

    public DiaryPreSignedUrlResponse addPictures(Long diaryId, List<PictureCreate> pictureCreates);

    public DiaryResponse changeDate(Long diaryId, ChangeDate changeDate);
}
