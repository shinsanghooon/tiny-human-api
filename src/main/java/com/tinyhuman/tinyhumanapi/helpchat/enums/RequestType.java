package com.tinyhuman.tinyhumanapi.helpchat.enums;

public enum RequestType {

    KEYWORD("Keyword", "관심사"),
    LOCATION("Location", "위치");

    private final String englishName;
    private final String koreanName;

    RequestType(String englishName, String koreanName) {
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
