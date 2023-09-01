package com.tinyhuman.tinyhumanapi.integration.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String sendImage(MultipartFile file, String s3UploadPath);

    String getPreSignedUrlForUpload(String keyName, String mimeType);

    String getPreSignedUrlForRead(String keyName);

    String getPreSignedUrlForReadFromKeyName(String keyName);

}
