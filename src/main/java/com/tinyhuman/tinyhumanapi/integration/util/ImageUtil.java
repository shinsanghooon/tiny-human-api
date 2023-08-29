package com.tinyhuman.tinyhumanapi.integration.util;

import com.tinyhuman.tinyhumanapi.common.domain.exception.NotSupportedContentTypeException;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import org.springframework.web.multipart.MultipartFile;

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
}
