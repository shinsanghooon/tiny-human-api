package com.tinyhuman.tinyhumanapi.integration.util;

import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.NotSupportedContentTypeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageUtil {
    public static ContentType getContentType(MultipartFile file) {
        String fileContentType = file.getContentType();
        ContentType contentType;
        if (fileContentType.startsWith("image")) {
            contentType = ContentType.PICTURE;
        } else if (fileContentType.startsWith("video")) {
            contentType = ContentType.VIDEO;
        } else {
            throw new NotSupportedContentTypeException(fileContentType);
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

}
