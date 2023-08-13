package com.tinyhuman.tinyhumanapi.baby.enums;

public enum Gender {

    MALE("Male", "남자"),
    FEMALE("Female", "여자");

    private final String englishName;
    private final String koreanName;

    Gender(String englishName, String koreanName) {
        this.englishName = englishName;
        this.koreanName = koreanName;
    }

    public String getEnglish() {
        return englishName;
    }

    public String getKorean() {
        return koreanName;
    }
}
