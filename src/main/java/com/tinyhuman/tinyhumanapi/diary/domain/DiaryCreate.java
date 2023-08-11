package com.tinyhuman.tinyhumanapi.diary.domain;

import lombok.Getter;

@Getter
public class DiaryCreate {

    private int daysAfterBirth;

    private String writer;

    private Boolean isPublic;

    private int likeCount;
}
