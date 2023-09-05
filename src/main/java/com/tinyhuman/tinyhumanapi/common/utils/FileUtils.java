package com.tinyhuman.tinyhumanapi.common.utils;

import com.tinyhuman.tinyhumanapi.common.service.port.ClockHolder;

public class FileUtils {

    public static String addUserIdToImagePath(String path, Long userId, String fileName) {
        return path.replace("userId", String.valueOf(userId)) + fileName;
    }

    public static String addBabyIdToImagePath(String path, Long babyId, String fileName) {
        return path.replace("babyId", String.valueOf(babyId)) + fileName;
    }

    public static String addBabyIdAndAlbumIdToImagePath(String path, Long babyId, Long albumId, String fileName) {
        return path.replace("babyId", String.valueOf(babyId))
                .replace("albumId", String.valueOf(albumId)) +
                fileName;
    }

    public static String generateFileNameWithEpochTime(String fileName, ClockHolder clockHolder) {
        long epochMilli = clockHolder.epochMilli();
        return epochMilli + "_" + fileName;
    }

    public static String extractFileNameFromPath(String keyName) {
        String[] keyNameSplit = keyName.split("/");
        return keyNameSplit[keyNameSplit.length - 1];
    }

}
