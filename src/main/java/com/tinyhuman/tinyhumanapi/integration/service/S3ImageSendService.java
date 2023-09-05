package com.tinyhuman.tinyhumanapi.integration.service;

import com.tinyhuman.tinyhumanapi.common.utils.FileUtils;
import com.tinyhuman.tinyhumanapi.integration.aws.S3Utils;
import com.tinyhuman.tinyhumanapi.integration.service.port.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3ImageSendService implements ImageService {

    @Value("${aws.s3.raw-bucketName}")
    private String RAW_BUCKET_NAME;

    @Value("${aws.s3.thumbnail-bucketName}")
    private String THUMBNAIL_BUCKET_NAME;

    private final S3AsyncClient s3AsyncClient;

    private final S3Presigner s3Presigner;

    private int MAX_FILE_SIZE = 15 * 1024 * 1024;

    @Override
    public String sendImage(MultipartFile file, String s3UploadPath) {

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new MaxUploadSizeExceededException(file.getSize());
        }

        byte[] imageBytes = getImageBytes(file);

        String fileName = file.getOriginalFilename();
        Long babyId = 1L;
        String s3FullPath = FileUtils.addUserIdToImagePath(s3UploadPath, babyId, fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(RAW_BUCKET_NAME)
                .key(s3FullPath)
                .build();

        s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromBytes(imageBytes))
                .whenComplete((response, exception) -> {
                            if (exception != null) {
                                throw new RuntimeException("Failed to upload image.", exception);
                            }
                            System.out.println("Image uploaded to S3: " + s3FullPath);
                        });

        return S3Utils.getAccessUrl(RAW_BUCKET_NAME, s3FullPath);
    }

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

    @Override
    public String getPreSignedUrlForRead(String keyName, int size) {

        // convert keyname to ThumbnailKeyName
        String thumbnailKeyName = convertThumbnailKeyName(keyName, size);

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(THUMBNAIL_BUCKET_NAME)
                .key(thumbnailKeyName)
                .build();

        GetObjectPresignRequest preSignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest preSignedRequest = s3Presigner.presignGetObject(preSignRequest);

        return preSignedRequest.url().toString();
    }

    private String convertThumbnailKeyName(String keyName, int size) {
        int lastDotIndex = keyName.lastIndexOf(".");
        String extension = keyName.substring(lastDotIndex);
        String fileNameWithoutExtension = keyName.substring(0, lastDotIndex);
        return fileNameWithoutExtension + "_" + size + "." + extension;
    }


    private byte[] getImageBytes(MultipartFile file) {
        byte[] imageBytes;

        try {
            imageBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file bytes.", e);
        }
        return imageBytes;
    }

}
