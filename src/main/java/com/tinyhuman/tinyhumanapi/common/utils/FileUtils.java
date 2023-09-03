package com.tinyhuman.tinyhumanapi.common.utils;

import com.tinyhuman.tinyhumanapi.common.service.port.ClockHolder;

public class FileUtils {

    public static String addUserIdToImagePath(String path, Long userId, String fileName) {
        StringBuilder s3FullPathBuilder = new StringBuilder();
        return s3FullPathBuilder.append(path.replace("userId", String.valueOf(userId))).append(fileName).toString();
    }

    public static String addBabyIdToImagePath(String path, Long babyId, String fileName) {
        StringBuilder s3FullPathBuilder = new StringBuilder();
        return s3FullPathBuilder
                .append(path.replace("babyId", String.valueOf(babyId)))
                .append(fileName).toString();
    }

    public static String addBabyIdAndAlbumIdToImagePath(String path, Long babyId, Long albumId, String fileName) {
        StringBuilder s3FullPathBuilder = new StringBuilder();
        return s3FullPathBuilder
                .append(path.replace("babyId", String.valueOf(babyId))
                        .replace("albumId", String.valueOf(albumId)))
                .append(fileName).toString();
    }

    public static String generateFileNameWithEpochTime(String fileName, ClockHolder clockHolder) {
        long epochMilli = clockHolder.epochMilli();
        return epochMilli + "_" + fileName;
    }

}
