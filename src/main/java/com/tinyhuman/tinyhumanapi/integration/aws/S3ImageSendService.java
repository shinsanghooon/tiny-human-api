package com.tinyhuman.tinyhumanapi.integration.aws;

import com.tinyhuman.tinyhumanapi.integration.service.ImageService;
import com.tinyhuman.tinyhumanapi.integration.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3ImageSendService implements ImageService {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    private final S3AsyncClient s3AsyncClient;

    @Override
    public String sendImage(MultipartFile file, String s3UploadPath) {

        byte[] imageBytes = getImageBytes(file);

        String fileName = file.getOriginalFilename();
        Long userId = 1L;
        String s3FullPath = S3Util.addUserIdToImagePath(s3UploadPath, userId, fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3FullPath)
                .build();

        s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromBytes(imageBytes))
                .whenComplete((response, exception) -> {
                            if (exception != null) {
                                throw new RuntimeException("Failed to upload image.", exception);
                            }
                            System.out.println("Image uploaded to S3: " + s3FullPath);
                        });

        return S3Util.getAccessUrl(bucketName, s3FullPath);
    }

    private static byte[] getImageBytes(MultipartFile file) {
        byte[] imageBytes;

        try {
            imageBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file bytes.", e);
        }
        return imageBytes;
    }
}
