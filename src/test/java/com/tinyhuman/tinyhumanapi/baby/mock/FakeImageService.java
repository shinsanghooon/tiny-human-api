package com.tinyhuman.tinyhumanapi.baby.mock;

import com.tinyhuman.tinyhumanapi.integration.service.ImageService;
import org.springframework.web.multipart.MultipartFile;

public class FakeImageService implements ImageService {

    @Override
    public String sendImage(MultipartFile file, String s3UploadPath) {
        s3UploadPath = "test/image";
        return "http://localhost:8080/" + s3UploadPath;
    }
}
