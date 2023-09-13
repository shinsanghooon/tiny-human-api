package com.tinyhuman.tinyhumanapi.common.enums;

public enum ContentType {
    PHOTO("Photo", "사진"),
    VIDEO("Video", "영상"),

    UNKNOWN("Unknown", "알 수 없음");
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
