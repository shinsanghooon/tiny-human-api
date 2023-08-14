package com.tinyhuman.tinyhumanapi.integration.aws;

public class S3Util {

    public static String addUserIdToImagePath(String path, Long userId, String fileName) {
        StringBuffer s3FullPathBuffer = new StringBuffer();
        String id = String.valueOf(userId);
        return s3FullPathBuffer.append(path.replace("babyId", id)).append(fileName).toString();
    }

    public static String getAccessUrl(String bucketName, String s3FullPath) {
        StringBuffer accessUrl = new StringBuffer();
        return accessUrl.append("https://")
                .append(bucketName)
                .append(".s3.ap-northeast-2.amazonaws.com")
                .append("/")
                .append(s3FullPath)
                .toString();
    }
}
