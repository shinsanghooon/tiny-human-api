package com.tinyhuman.tinyhumanapi.common.utils;

import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.NotSupportedContentTypeException;
import com.tinyhuman.tinyhumanapi.common.service.port.ClockHolder;
import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public record FileInfo(String fileNameWithEpochTime, String mimeType, ContentType contentType) {
        @Builder
        public FileInfo {
        }
    }

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

    public static ContentType getContentType(String mimeType) {
        ContentType contentType;
        if (mimeType.startsWith("image")) {
            contentType = ContentType.PICTURE;
        } else if (mimeType.startsWith("video")) {
            contentType = ContentType.VIDEO;
        } else {
            throw new NotSupportedContentTypeException(mimeType);
        }
        return contentType;
    }

    public static String guessMimeType(String fileName){
        Path path = new File(fileName).toPath();
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileInfo getFileInfo(String fileName, ClockHolder clockHolder) {
        String newFileName = generateFileNameWithEpochTime(fileName, clockHolder);
        String mimeType = guessMimeType(fileName);
        ContentType contentType = getContentType(mimeType);
        return FileInfo.builder()
                .fileNameWithEpochTime(newFileName)
                .mimeType(mimeType)
                .contentType(contentType)
                .build();
    }
}
