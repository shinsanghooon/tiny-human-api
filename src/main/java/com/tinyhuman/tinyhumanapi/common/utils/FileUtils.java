package com.tinyhuman.tinyhumanapi.common.utils;

import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.NotSupportedContentTypeException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
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

    public static String generateFileNameWithUUID(String fileName, String uuid) {
        return uuid + "_" + fileName;
    }

    public static String extractFileNameFromPath(String keyName) {
        String[] keyNameSplit = keyName.split("/");
        return keyNameSplit[keyNameSplit.length - 1];
    }

    public static ContentType getContentType(String mimeType) {
        ContentType contentType;
        if (mimeType.startsWith("image")) {
            contentType = ContentType.PHOTO;
        } else if (mimeType.startsWith("video")) {
            contentType = ContentType.VIDEO;
        } else {
            log.error("Not supported ContentType - ContentType:{}", mimeType);
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

    public static FileInfo getFileInfo(String fileName, String uuid) {
        String newFileName = generateFileNameWithUUID(fileName, uuid);
        String mimeType = guessMimeType(fileName);
        ContentType contentType = getContentType(mimeType);
        return FileInfo.builder()
                .fileNameWithEpochTime(newFileName)
                .mimeType(mimeType)
                .contentType(contentType)
                .build();
    }
}
