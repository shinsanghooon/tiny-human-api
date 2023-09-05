package com.tinyhuman.tinyhumanapi.integration.service.port;

public interface ImageService {

    String getPreSignedUrlForUpload(String keyName, String mimeType);

}
