package com.tinyhuman.tinyhumanapi.diary.domain;

import com.tinyhuman.tinyhumanapi.baby.domain.Baby;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record Diary(Long id, int daysAfterBirth, User user, int likeCount,
                    LocalDateTime created_at, Baby baby, boolean isDeleted,
                    List<Sentence> sentences, List<Picture> pictures) {

    @Builder
    public Diary {
    }

    public static Diary fromCreate(DiaryCreate diaryCreate, Baby baby, User user) {
        return Diary.builder()
                .daysAfterBirth(diaryCreate.daysAfterBirth())
                .user(user)
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
                .created_at(this.created_at)
                .baby(this.baby)
                .isDeleted(true)
                .build();
    }
}
