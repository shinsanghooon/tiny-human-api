package com.tinyhuman.tinyhumanapi.diary.enums;

public enum ContentType {
    PICTURE("Picture", "사진"),
    VIDEO("Video", "영상");

    private final String englishName;
    private final String koreanName;

    ContentType(String englishName, String koreanName) {
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
