package com.tinyhuman.tinyhumanapi.diary.service.port;

import com.tinyhuman.tinyhumanapi.diary.domain.CounselorLetter;

import java.util.List;

public interface CounselorLetterRepository {

    List<CounselorLetter> findByBabyId(Long babyId);

}
