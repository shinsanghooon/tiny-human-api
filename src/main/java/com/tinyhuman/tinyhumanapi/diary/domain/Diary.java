package com.tinyhuman.tinyhumanapi.diary.domain;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.diary.controller.port.dto.DiaryCreate;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public record Diary(Long id, int daysAfterBirth, User user, int likeCount,
                    LocalDate date, Baby baby, boolean isDeleted,
                    List<Sentence> sentences, List<Picture> pictures) {

    @Builder
    public Diary {
    }

    public static Diary fromCreate(DiaryCreate diaryCreate, Baby baby, User user) {
        return Diary.builder()
                .daysAfterBirth(diaryCreate.daysAfterBirth())
                .user(user)
                .date(diaryCreate.date())
                .likeCount(diaryCreate.likeCount())
                .baby(baby)
                .build();
    }

    public Diary addSentences(List<Sentence> sentences) {
        return Diary.builder()
                .id(this.id)
                .daysAfterBirth(this.daysAfterBirth)
                .user(this.user)
                .likeCount(this.likeCount)
                .baby(this.baby)
                .date(this.date)
                .sentences(sentences)
                .pictures(this.pictures)
                .build();
    }

    public Diary addPictures(List<Picture> pictures) {
        return Diary.builder()
                .id(this.id)
                .daysAfterBirth(this.daysAfterBirth)
                .user(this.user)
                .likeCount(this.likeCount)
                .date(this.date)
                .baby(this.baby)
                .sentences(this.sentences)
                .pictures(pictures)
                .build();
    }

    public Diary delete() {
        return Diary.builder()
                .id(this.id)
                .daysAfterBirth(this.daysAfterBirth)
                .user(this.user)
                .likeCount(this.likeCount)
                .date(this.date)
                .baby(this.baby)
                .isDeleted(true)
                .build();
    }
}
