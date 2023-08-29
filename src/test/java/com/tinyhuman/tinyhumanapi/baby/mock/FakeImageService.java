package com.tinyhuman.tinyhumanapi.baby.mock;

import com.tinyhuman.tinyhumanapi.integration.service.ImageService;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

public class FakeImageService implements ImageService {

    @Override
    public String sendImage(MultipartFile file, String s3UploadPath) {

        int MAX_FILE_SIZE = 15 * 1024 * 1024;

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new MaxUploadSizeExceededException(file.getSize());
        }

        s3UploadPath = "test";
        return "http://localhost:8080/" + s3UploadPath + "/" + file.getOriginalFilename();
    }
}
