package com.tinyhuman.tinyhumanapi.baby.mock;

import com.tinyhuman.tinyhumanapi.integration.service.port.ImageService;

public class FakeImageService implements ImageService {
    @Override
    public String getPreSignedUrlForUpload(String keyName, String mimeType) {
        return keyName;
    }

}
