package com.tinyhuman.tinyhumanapi.integration.service;

import com.tinyhuman.tinyhumanapi.integration.service.port.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3ImageSendService implements ImageService {

    @Value("${spring.aws.s3.raw-bucketName}")
    private String RAW_BUCKET_NAME;

    private final S3Presigner s3Presigner;

    @Override
    public String getPreSignedUrlForUpload(String keyName, String mimeType) {

        // keyName is filename including directory path
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(RAW_BUCKET_NAME)
                .key(keyName)
                .contentType(mimeType)
                .build();

        PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(2))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest preSignedRequest = s3Presigner.presignPutObject(preSignRequest);

        return preSignedRequest.url().toString();
    }

}
