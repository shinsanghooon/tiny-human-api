package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public record DiaryCreate(int daysAfterBirth, String writer, Boolean isPublic, int likeCount) {

}
